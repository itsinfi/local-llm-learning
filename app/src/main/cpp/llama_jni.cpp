// app/src/main/cpp/llama_jni.cpp
#include <jni.h>
#include <string>
#include <vector>
#include <mutex>
#include <atomic>
#include <android/log.h>
#include <cstdint>
#include <time.h>

#include "llama.h"

#define LOG_TAG "llama_jni"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,  LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

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

    llama_context_params cparams; // cached init params for fast context recreation

    uint32_t seed = 0;

    int32_t nPast = 0;
    int32_t generated = 0;

    float temperature = 0.2f;
    float topP = 0.95f;
    int32_t maxTokens = 800;

    bool started = false;
    bool finished = false;
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

static std::string applyChatTemplate(EngineState & st, const std::string & userPrompt) {
    llama_chat_message msg;
    msg.role = "user";
    msg.content = userPrompt.c_str();

    const char * tmpl = llama_model_chat_template(st.model, nullptr);
    if (!tmpl || tmpl[0] == '\0') return userPrompt;

    int32_t cap = 4096;
    std::vector<char> buf((size_t) cap);

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
        if (n < cap) return std::string(buf.data(), (size_t) n);

        cap = n + 1;
        buf.resize((size_t) cap);
    }
}

static std::vector<llama_token> tokenize(EngineState & st, const std::string & text) {
    std::vector<llama_token> tokens(1024);

    int32_t n = llama_tokenize(
            st.vocab,
            text.c_str(),
            (int32_t) text.size(),
            tokens.data(),
            (int32_t) tokens.size(),
            true,
            true
    );

    if (n < 0) {
        const int32_t need = -n;
        tokens.resize((size_t) need);

        n = llama_tokenize(
                st.vocab,
                text.c_str(),
                (int32_t) text.size(),
                tokens.data(),
                (int32_t) tokens.size(),
                true,
                true
        );
    }

    if (n <= 0) {
        tokens.clear();
        return tokens;
    }

    tokens.resize((size_t) n);
    return tokens;
}

static std::string tokenToText(EngineState & st, llama_token tok) {
    std::string out;
    out.resize(256);

    const int32_t n = llama_token_to_piece(
            st.vocab,
            tok,
            out.data(),
            (int32_t) out.size(),
            0,
            false
    );

    if (n <= 0) return "";
    out.resize((size_t) n);
    return out;
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

    mparams.use_mmap = true;
    mparams.use_mlock = false;

    cparams.n_ctx = (uint32_t) contextLength;
    cparams.n_batch = (uint32_t) contextLength;
    cparams.n_ubatch = (uint32_t) contextLength;
    cparams.n_seq_max = 1;
    cparams.n_threads = (int32_t) threads;
    cparams.n_threads_batch = (int32_t) threads;

    auto * st = new EngineState();
    st->seed = (uint32_t) seed;
    st->cparams = cparams;

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

    const long long t1 = now_ms();

    LOGI(
            "nativeInit ok. model=%s n_ctx=%d n_batch=%d n_ubatch=%d threads=%d timing_ms total=%lld load_model=%lld init_ctx=%lld",
            mp.c_str(),
            (int) contextLength,
            (int) cparams.n_batch,
            (int) cparams.n_ubatch,
            (int) threads,
            (t1 - t0),
            (t_load1 - t_load0),
            (t_ctx1 - t_ctx0)
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

static bool recreateContextLocked(EngineState & st) {
    if (!st.model) return false;

    if (st.ctx) {
        llama_free(st.ctx);
        st.ctx = nullptr;
    }

    st.ctx = llama_init_from_model(st.model, st.cparams);
    if (!st.ctx) {
        return false;
    }

    // vocab belongs to model, but keep pointer refreshed
    st.vocab = llama_model_get_vocab(st.model);

    // clear memory explicitly to ensure clean start
    llama_memory_t mem = llama_get_memory(st.ctx);
    llama_memory_clear(mem, true);

    st.nPast = 0;
    st.generated = 0;
    return true;
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

    st->temperature = (float) temperature;
    st->topP = (float) topP;
    st->maxTokens = (int32_t) maxTokens;

    // sampler reset
    if (st->sampler) {
        llama_sampler_free(st->sampler);
        st->sampler = nullptr;
    }
    st->sampler = buildSampler(st->seed, st->temperature, st->topP);

    const long long t_reset0 = now_ms();
    if (!recreateContextLocked(*st)) {
        LOGE("Failed to recreate context");
        st->finished = true;
        return -6;
    }
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

    const int32_t n_ctx = (int32_t) llama_n_ctx(st->ctx);
    if ((int32_t) tokens.size() >= n_ctx) {
        LOGE("Prompt too long tokens=%d n_ctx=%d", (int) tokens.size(), (int) n_ctx);
        st->finished = true;
        return -5;
    }

    const int32_t n_batch = (int32_t) llama_n_batch(st->ctx);

    const long long t_dec0 = now_ms();
    int32_t idx = 0;
    while (idx < (int32_t) tokens.size()) {
        const int32_t remaining = (int32_t) tokens.size() - idx;
        const int32_t chunk = remaining < n_batch ? remaining : n_batch;

        llama_batch batch = llama_batch_init(chunk, 0, 1);
        batch.embd = nullptr;
        batch.n_tokens = chunk;

        for (int32_t i = 0; i < chunk; i++) {
            const int32_t pos = idx + i;
            batch.token[i] = tokens[(size_t) pos];
            batch.pos[i] = (llama_pos) pos;
            batch.n_seq_id[i] = 1;
            batch.seq_id[i][0] = 0;
            batch.logits[i] = (pos == (int32_t) tokens.size() - 1) ? 1 : 0;
        }

        const int rc = llama_decode(st->ctx, batch);
        llama_batch_free(batch);

        if (rc != 0) {
            const long long t_dec1 = now_ms();
            LOGE(
                    "Decode prompt failed rc=%d decode_ms=%lld tokens=%d n_batch=%d idx=%d chunk=%d",
                    rc,
                    (t_dec1 - t_dec0),
                    (int) tokens.size(),
                    (int) n_batch,
                    (int) idx,
                    (int) chunk
            );
            st->finished = true;
            return -4;
        }

        idx += chunk;
    }
    const long long t_dec1 = now_ms();

    st->nPast = (int32_t) tokens.size();
    st->generated = 0;

    LOGI(
            "startGenerate timing_ms reset=%lld template=%lld tokenize=%lld decode_prompt=%lld prompt_tokens=%d",
            (t_reset1 - t_reset0),
            (t_fmt1 - t_fmt0),
            (t_tok1 - t_tok0),
            (t_dec1 - t_dec0),
            (int) tokens.size()
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
    batch.embd = nullptr;
    batch.n_tokens = 1;
    batch.token[0] = next;
    batch.pos[0] = (llama_pos) st->nPast;
    batch.n_seq_id[0] = 1;
    batch.seq_id[0][0] = 0;
    batch.logits[0] = 1;

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
