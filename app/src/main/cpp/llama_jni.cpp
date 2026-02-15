#include <jni.h>
#include <string>
#include <vector>
#include <mutex>
#include <atomic>
#include <android/log.h>
#include <cstdint>
#include <time.h>
#include <cstdio>

#include "llama.h"

#define LOG_TAG "llama_jni"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,  LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

/*
    Reset Strategie

    Standard: Snapshot Restore, garantiert kompatibel
    Optional: KV Cache Clear, schneller pro Anfrage, aber nur wenn deine llama.cpp Version die Funktion hat

    Wenn deine llama.cpp Version llama_kv_cache_clear(ctx) hat, setze USE_KV_CACHE_CLEAR auf 1.
    Falls der Build dann fehlschlaegt, setze wieder auf 0 und poste mir die Compiler Fehlermeldung,
    dann passe ich den Reset an die API deiner llama.cpp Version an.
*/
#ifndef USE_KV_CACHE_CLEAR
#define USE_KV_CACHE_CLEAR 0
#endif

static long long now_ms() {
    timespec ts;
    clock_gettime(CLOCK_MONOTONIC, &ts);
    return (long long) ts.tv_sec * 1000LL + (ts.tv_nsec / 1000000LL);
}

static std::once_flag g_backend_once;

struct EngineState {
    std::mutex mtx;
    std::atomic<bool> abort{false};

    llama_model * model = nullptr;
    llama_context * ctx = nullptr;
    const llama_vocab * vocab = nullptr;

    llama_sampler * sampler = nullptr;

    uint32_t seed = 0;

    int32_t nPast = 0;
    int32_t generated = 0;

    float temperature = 0.2f;
    float topP = 0.95f;

    int32_t maxTokens = 700;

    bool started = false;
    bool finished = false;

    std::vector<uint8_t> base_state;
};

static std::string jstringToStdString(JNIEnv * env, jstring s) {
    const char * chars = env->GetStringUTFChars(s, nullptr);
    std::string out = chars ? chars : "";
    if (chars) env->ReleaseStringUTFChars(s, chars);
    return out;
}

static llama_sampler * buildSampler(uint32_t seed, float temperature, float topP) {
    llama_sampler_chain_params params = llama_sampler_chain_default_params();
    llama_sampler * chain = llama_sampler_chain_init(params);

    llama_sampler_chain_add(chain, llama_sampler_init_top_k(50));
    llama_sampler_chain_add(chain, llama_sampler_init_top_p(topP, 1));
    llama_sampler_chain_add(chain, llama_sampler_init_temp(temperature));
    llama_sampler_chain_add(chain, llama_sampler_init_dist(seed));

    return chain;
}

static bool snapshotBaseState(EngineState & st) {
    const size_t sz = llama_state_get_size(st.ctx);
    if (sz == 0) return false;

    st.base_state.resize(sz);
    const size_t got = llama_state_get_data(st.ctx, st.base_state.data(), st.base_state.size());
    return got == st.base_state.size();
}

static bool restoreBaseState(EngineState & st) {
    if (st.base_state.empty()) return false;
    const size_t set = llama_state_set_data(st.ctx, st.base_state.data(), st.base_state.size());
    return set == st.base_state.size();
}

static std::string applyChatTemplate(EngineState & st, const std::string & userPrompt) {
    llama_chat_message msg;
    msg.role = "user";
    msg.content = userPrompt.c_str();

    const char * tmpl = llama_model_chat_template(st.model, nullptr);
    if (!tmpl || tmpl[0] == '\0') return userPrompt;

    int32_t cap = 4096;
    std::vector<char> buf((size_t)cap);

    while (true) {
        const int32_t n = llama_chat_apply_template(
                tmpl,
                &msg,
                1,
                true,
                buf.data(),
                cap
        );

        if (n < 0) return userPrompt;
        if (n < cap) return std::string(buf.data(), (size_t)n);

        cap = n + 1;
        buf.resize((size_t)cap);
    }
}

static std::vector<llama_token> tokenize(EngineState & st, const std::string & text) {
    std::vector<llama_token> tokens(1024);

    int32_t n = llama_tokenize(
            st.vocab,
            text.c_str(),
            (int32_t)text.size(),
            tokens.data(),
            (int32_t)tokens.size(),
            true,
            true
    );

    if (n < 0) {
        const int32_t need = -n;
        tokens.resize((size_t)need);

        n = llama_tokenize(
                st.vocab,
                text.c_str(),
                (int32_t)text.size(),
                tokens.data(),
                (int32_t)tokens.size(),
                true,
                true
        );
    }

    if (n <= 0) {
        tokens.clear();
        return tokens;
    }

    tokens.resize((size_t)n);
    return tokens;
}

static std::string tokenToText(EngineState & st, llama_token tok) {
    std::string out;
    out.resize(256);

    const int32_t n = llama_token_to_piece(
            st.vocab,
            tok,
            out.data(),
            (int32_t)out.size(),
            0,
            false
    );

    if (n <= 0) return "";
    out.resize((size_t)n);
    return out;
}

static void resetContextForNewRequest(EngineState & st) {
#if USE_KV_CACHE_CLEAR
    llama_kv_cache_clear(st.ctx);
#else
    (void) restoreBaseState(st);
#endif
    st.nPast = 0;
    st.generated = 0;
}

extern "C" JNIEXPORT jlong JNICALL
Java_de_raum7_local_1llm_1learning_llm_LlamaNative_nativeInit(
        JNIEnv * env,
        jobject,
        jstring modelPath,
        jint contextLength,
        jint threads,
        jint seed
) {
    const long long t0 = now_ms();

    const std::string mp = jstringToStdString(env, modelPath);
    if (mp.empty()) return 0;

    std::call_once(g_backend_once, []() {
        llama_backend_init();
    });

    llama_model_params mparams = llama_model_default_params();
    llama_context_params cparams = llama_context_default_params();

    mparams.use_mmap  = true;
    mparams.use_mlock = false;

    cparams.n_ctx = (uint32_t)contextLength;
    cparams.n_threads = (int32_t)threads;
    cparams.n_threads_batch = (int32_t)threads;

    auto * st = new EngineState();
    st->seed = (uint32_t)seed;

    const long long t_load0 = now_ms();
    st->model = llama_model_load_from_file(mp.c_str(), mparams);
    const long long t_load1 = now_ms();

    if (!st->model) {
        LOGE("Failed to load model: %s load_ms=%lld", mp.c_str(), (t_load1 - t_load0));
        delete st;
        return 0;
    }

    const long long t_ctx0 = now_ms();
    st->ctx = llama_init_from_model(st->model, cparams);
    const long long t_ctx1 = now_ms();

    if (!st->ctx) {
        LOGE("Failed to create context init_ctx_ms=%lld", (t_ctx1 - t_ctx0));
        llama_model_free(st->model);
        delete st;
        return 0;
    }

    st->vocab = llama_model_get_vocab(st->model);
    st->sampler = buildSampler(st->seed, st->temperature, st->topP);

    const long long t_snap0 = now_ms();
    const bool snapOk = snapshotBaseState(*st);
    const long long t_snap1 = now_ms();

    const long long t1 = now_ms();

    LOGI(
            "nativeInit ok. model=%s n_ctx=%d threads=%d timing_ms total=%lld load_model=%lld init_ctx=%lld snapshot=%lld snap_ok=%d use_kv_clear=%d",
            mp.c_str(),
            (int)contextLength,
            (int)threads,
            (t1 - t0),
            (t_load1 - t_load0),
            (t_ctx1 - t_ctx0),
            (t_snap1 - t_snap0),
            snapOk ? 1 : 0,
            (int)USE_KV_CACHE_CLEAR
    );

    return reinterpret_cast<jlong>(st);
}

extern "C" JNIEXPORT void JNICALL
Java_de_raum7_local_1llm_1learning_llm_LlamaNative_nativeFree(
        JNIEnv *,
        jobject,
        jlong handle
) {
    auto * st = reinterpret_cast<EngineState *>(handle);
    if (!st) return;

    std::lock_guard<std::mutex> lock(st->mtx);

    if (st->sampler) llama_sampler_free(st->sampler);
    st->sampler = nullptr;

    if (st->ctx) llama_free(st->ctx);
    if (st->model) llama_model_free(st->model);

    st->ctx = nullptr;
    st->model = nullptr;
    st->vocab = nullptr;

    delete st;
    LOGI("nativeFree done");
}

extern "C" JNIEXPORT jint JNICALL
Java_de_raum7_local_1llm_1learning_llm_LlamaNative_nativeStartGenerate(
        JNIEnv * env,
        jobject,
        jlong handle,
        jstring prompt,
        jfloat temperature,
        jfloat topP,
        jint maxTokens
) {
    auto * st = reinterpret_cast<EngineState *>(handle);
    if (!st) return -1;

    const std::string p = jstringToStdString(env, prompt);
    if (p.empty()) return -2;

    std::lock_guard<std::mutex> lock(st->mtx);

    st->abort.store(false);
    st->finished = false;
    st->started = true;
    st->generated = 0;
    st->nPast = 0;

    st->temperature = (float)temperature;
    st->topP = (float)topP;
    st->maxTokens = (int32_t)maxTokens;

    if (st->sampler) {
        llama_sampler_free(st->sampler);
        st->sampler = nullptr;
    }
    st->sampler = buildSampler(st->seed, st->temperature, st->topP);

    const long long t_reset0 = now_ms();
    resetContextForNewRequest(*st);
    const long long t_reset1 = now_ms();

    const long long t_fmt0 = now_ms();
    const std::string formatted = applyChatTemplate(*st, p);
    const long long t_fmt1 = now_ms();

    const long long t_tok0 = now_ms();
    std::vector<llama_token> tokens = tokenize(*st, formatted);
    const long long t_tok1 = now_ms();

    if (tokens.empty()) {
        LOGE("Tokenize failed");
        st->finished = true;
        return -3;
    }

    llama_batch batch = llama_batch_init((int32_t)tokens.size(), 0, 1);
    batch.n_tokens = (int32_t)tokens.size();

    for (int32_t i = 0; i < batch.n_tokens; i++) {
        batch.token[i] = tokens[(size_t)i];
        batch.pos[i] = i;
        batch.n_seq_id[i] = 1;
        batch.seq_id[i][0] = 0;
        batch.logits[i] = (i == batch.n_tokens - 1);
    }

    const long long t_dec0 = now_ms();
    const int rc = llama_decode(st->ctx, batch);
    const long long t_dec1 = now_ms();

    llama_batch_free(batch);

    if (rc != 0) {
        LOGE("Decode prompt failed rc=%d decode_ms=%lld", rc, (t_dec1 - t_dec0));
        st->finished = true;
        return -4;
    }

    st->nPast = (int32_t)tokens.size();

    LOGI(
            "startGenerate timing_ms reset=%lld template=%lld tokenize=%lld decode_prompt=%lld prompt_tokens=%d",
            (t_reset1 - t_reset0),
            (t_fmt1 - t_fmt0),
            (t_tok1 - t_tok0),
            (t_dec1 - t_dec0),
            (int)tokens.size()
    );

    return 0;
}

extern "C" JNIEXPORT jstring JNICALL
Java_de_raum7_local_1llm_1learning_llm_LlamaNative_nativeNextToken(
        JNIEnv * env,
        jobject,
        jlong handle
) {
    auto * st = reinterpret_cast<EngineState *>(handle);
    if (!st) return nullptr;

    if (st->abort.load()) return nullptr;

    std::lock_guard<std::mutex> lock(st->mtx);

    if (!st->started || st->finished) return nullptr;

    if (st->generated >= st->maxTokens) {
        st->finished = true;
        return nullptr;
    }

    const llama_token next = llama_sampler_sample(st->sampler, st->ctx, -1);
    llama_sampler_accept(st->sampler, next);

    if (llama_vocab_is_eog(st->vocab, next)) {
        st->finished = true;
        return nullptr;
    }

    llama_batch batch = llama_batch_init(1, 0, 1);
    batch.n_tokens = 1;
    batch.token[0] = next;
    batch.pos[0] = st->nPast;
    batch.n_seq_id[0] = 1;
    batch.seq_id[0][0] = 0;
    batch.logits[0] = true;

    const int rc = llama_decode(st->ctx, batch);
    llama_batch_free(batch);

    if (rc != 0) {
        LOGE("Decode token failed rc=%d", rc);
        st->finished = true;
        return nullptr;
    }

    st->nPast += 1;
    st->generated += 1;

    if (llama_vocab_is_control(st->vocab, next)) {
        return env->NewStringUTF("");
    }

    const std::string piece = tokenToText(*st, next);
    return env->NewStringUTF(piece.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_de_raum7_local_1llm_1learning_llm_LlamaNative_nativeAbort(
        JNIEnv *,
        jobject,
        jlong handle
) {
    auto * st = reinterpret_cast<EngineState *>(handle);
    if (!st) return;
    st->abort.store(true);
}
