#include <jni.h>
#include <string>
#include <vector>
#include <mutex>
#include <atomic>
#include <android/log.h>
#include <cstdint>

#include "llama.h"

// Android Log Tags und Makros
#define LOG_TAG "llama_jni"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,  LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

/**
 * EngineState hält alle nativen Zustände, die über das JNI Handle verwaltet werden:
 * Modell, Kontext, Sampler, Statusvariablen und optional ein Snapshot des Basiszustands.
 */
struct EngineState {
    // Mutex für Thread Safety bei Zugriff auf llama Kontext und Status
    std::mutex mtx;

    // Abbruch Flag, wird z. B. beim Cancel aus Kotlin gesetzt
    std::atomic<bool> abort{false};

    // llama.cpp Objekte
    llama_model * model = nullptr;
    llama_context * ctx = nullptr;
    const llama_vocab * vocab = nullptr;

    // Sampler Chain (top k, top p, temp, dist)
    llama_sampler * sampler = nullptr;

    // Sampling Seed
    uint32_t seed = 0;

    // KV Cache Positionierung und Token Zähler
    int32_t nPast = 0;       // Anzahl bisher in den Kontext gepushten Tokens
    int32_t generated = 0;   // Anzahl generierter Tokens

    // Sampling Parameter
    float temperature = 0.2f;
    float topP = 0.95f;

    // Generationslimit
    int32_t maxTokens = 900;

    // Status Flags
    bool started = false;
    bool finished = false;

    // Snapshot des llama Kontext Zustands für schnelles Reset pro Anfrage
    std::vector<uint8_t> base_state;
};

/**
 * Helfer: jstring nach std::string konvertieren.
 */
static std::string jstringToStdString(JNIEnv * env, jstring s) {
    const char * chars = env->GetStringUTFChars(s, nullptr);
    std::string out = chars ? chars : "";
    if (chars) env->ReleaseStringUTFChars(s, chars);
    return out;
}

/**
 * Baut eine Sampler Chain.
 * Reihenfolge: top k, top p, temperature, dist(seed).
 */
static llama_sampler * buildSampler(uint32_t seed, float temperature, float topP) {
    llama_sampler_chain_params params = llama_sampler_chain_default_params();
    llama_sampler * chain = llama_sampler_chain_init(params);

    // Begrenzung auf die Top 50 Tokens
    llama_sampler_chain_add(chain, llama_sampler_init_top_k(50));

    // Top P Sampling
    llama_sampler_chain_add(chain, llama_sampler_init_top_p(topP, 1));

    // Temperatur
    llama_sampler_chain_add(chain, llama_sampler_init_temp(temperature));

    // Zufallsverteilung mit Seed
    llama_sampler_chain_add(chain, llama_sampler_init_dist(seed));

    return chain;
}

/**
 * Snapshot des aktuellen llama Kontext Zustands (KV Cache, etc.).
 * Dient als Basis Zustand, auf den man pro Anfrage zurücksetzen kann.
 */
static bool snapshotBaseState(EngineState & st) {
    const size_t sz = llama_state_get_size(st.ctx);
    if (sz == 0) return false;

    st.base_state.resize(sz);
    const size_t got = llama_state_get_data(st.ctx, st.base_state.data(), st.base_state.size());
    return got == st.base_state.size();
}

/**
 * Wiederherstellung des zuvor gesnapshotteten Basiszustands.
 * Damit wird der Kontext für eine neue Anfrage effektiv zurückgesetzt.
 */
static bool restoreBaseState(EngineState & st) {
    if (st.base_state.empty()) return false;
    const size_t set = llama_state_set_data(st.ctx, st.base_state.data(), st.base_state.size());
    return set == st.base_state.size();
}

/**
 * Wendet ein Chat Template aus dem Modell an, falls vorhanden.
 * Falls kein Template existiert, wird der Prompt unverändert zurückgegeben.
 */
static std::string applyChatTemplate(EngineState & st, const std::string & userPrompt) {
    llama_chat_message msg;
    msg.role = "user";
    msg.content = userPrompt.c_str();

    // Template aus dem Modell lesen
    const char * tmpl = llama_model_chat_template(st.model, nullptr);
    if (!tmpl || tmpl[0] == '\0') return userPrompt;

    // Initialer Puffer, wird bei Bedarf vergrößert
    int32_t cap = 4096;
    std::vector<char> buf((size_t)cap);

    while (true) {
        // Template anwenden
        const int32_t n = llama_chat_apply_template(
                tmpl,
                &msg,
                1,
                true,
                buf.data(),
                cap
        );

        if (n < 0) return userPrompt;                // Fehler
        if (n < cap) return std::string(buf.data(), (size_t)n); // Erfolg

        // Puffer zu klein, vergrößern
        cap = n + 1;
        buf.resize((size_t)cap);
    }
}

/**
 * Tokenisierung des Eingabetexts.
 * Nutzt zuerst einen Puffer, vergrößert ihn bei Bedarf.
 */
static std::vector<llama_token> tokenize(EngineState & st, const std::string & text) {
    std::vector<llama_token> tokens(1024);

    // true,true: add special + parse special (abhängig von llama.cpp Version)
    int32_t n = llama_tokenize(
            st.vocab,
            text.c_str(),
            (int32_t)text.size(),
            tokens.data(),
            (int32_t)tokens.size(),
            true,
            true
    );

    // Wenn n < 0, ist der Puffer zu klein, benötigt wird -n
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

    // Wenn weiterhin <= 0, Tokenisierung fehlgeschlagen
    if (n <= 0) {
        tokens.clear();
        return tokens;
    }

    tokens.resize((size_t)n);
    return tokens;
}

/**
 * Konvertiert ein einzelnes Token in Text.
 * Verwendet llama_token_to_piece.
 */
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

/**
 * JNI: Initialisierung der Engine.
 * Lädt Modell, erstellt Kontext, erstellt Sampler und snapshotet den Basiszustand.
 * Gibt ein Handle (Pointer als jlong) zurück.
 */
extern "C" JNIEXPORT jlong JNICALL
Java_de_raum7_local_1llm_1learning_llm_LlamaNative_nativeInit(
        JNIEnv * env,
        jobject,
        jstring modelPath,
        jint contextLength,
        jint threads,
        jint seed
) {
    const std::string mp = jstringToStdString(env, modelPath);
    if (mp.empty()) return 0;

    // llama Backend initialisieren (CPU, ggf. weitere Backends)
    llama_backend_init();

    // Default Parameter laden
    llama_model_params mparams = llama_model_default_params();
    llama_context_params cparams = llama_context_default_params();

    // Kontext Parameter setzen
    cparams.n_ctx = (uint32_t)contextLength;
    cparams.n_threads = (int32_t)threads;
    cparams.n_threads_batch = (int32_t)threads;

    // EngineState anlegen
    auto * st = new EngineState();
    st->seed = (uint32_t)seed;

    // Modell laden
    st->model = llama_model_load_from_file(mp.c_str(), mparams);
    if (!st->model) {
        LOGE("Failed to load model: %s", mp.c_str());
        delete st;
        return 0;
    }

    // Kontext erstellen
    st->ctx = llama_init_from_model(st->model, cparams);
    if (!st->ctx) {
        LOGE("Failed to create context");
        llama_model_free(st->model);
        delete st;
        return 0;
    }

    // Vocab holen
    st->vocab = llama_model_get_vocab(st->model);

    // Sampler initial bauen
    st->sampler = buildSampler(st->seed, st->temperature, st->topP);

    // Basiszustand snapshoten, damit pro Anfrage schnell zurückgesetzt werden kann
    if (!snapshotBaseState(*st)) {
        LOGE("Failed to snapshot base state");
    }

    LOGI("nativeInit ok. model=%s n_ctx=%d threads=%d", mp.c_str(), (int)contextLength, (int)threads);
    return reinterpret_cast<jlong>(st);
}

/**
 * JNI: Ressourcen freigeben.
 * Muss aufgerufen werden, wenn die Engine nicht mehr benötigt wird.
 */
extern "C" JNIEXPORT void JNICALL
Java_de_raum7_local_1llm_1learning_llm_LlamaNative_nativeFree(
        JNIEnv *,
        jobject,
        jlong handle
) {
    auto * st = reinterpret_cast<EngineState *>(handle);
    if (!st) return;

    std::lock_guard<std::mutex> lock(st->mtx);

    // Sampler freigeben
    if (st->sampler) llama_sampler_free(st->sampler);
    st->sampler = nullptr;

    // Kontext und Modell freigeben
    if (st->ctx) llama_free(st->ctx);
    if (st->model) llama_model_free(st->model);

    st->ctx = nullptr;
    st->model = nullptr;
    st->vocab = nullptr;

    delete st;
    LOGI("nativeFree done");
}

/**
 * JNI: Startet eine neue Generierung.
 * Setzt Statusvariablen zurück, stellt Basiszustand wieder her, tokenisiert Prompt,
 * decoded den Prompt in den Kontext, sodass logits für das nächste Token bereitstehen.
 *
 * Rückgabecodes:
 *  0  ok
 * -1  invalid handle
 * -2  empty prompt
 * -3  tokenization failed
 * -4  decode prompt failed
 */
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

    // Status zurücksetzen
    st->abort.store(false);
    st->finished = false;
    st->started = true;
    st->generated = 0;
    st->nPast = 0;

    // Sampling Parameter setzen
    st->temperature = (float)temperature;
    st->topP = (float)topP;
    st->maxTokens = (int32_t)maxTokens;

    // Sampler neu aufbauen, um Parameter zu übernehmen
    if (st->sampler) {
        llama_sampler_free(st->sampler);
        st->sampler = nullptr;
    }
    st->sampler = buildSampler(st->seed, st->temperature, st->topP);

    // Kontext auf Basiszustand zurücksetzen
    restoreBaseState(*st);

    // Prompt formatieren (Chat Template) und tokenisieren
    const std::string formatted = applyChatTemplate(*st, p);
    std::vector<llama_token> tokens = tokenize(*st, formatted);

    if (tokens.empty()) {
        LOGE("Tokenize failed");
        st->finished = true;
        return -3;
    }

    // Batch für Prompt Decode vorbereiten
    llama_batch batch = llama_batch_init((int32_t)tokens.size(), 0, 1);
    batch.n_tokens = (int32_t)tokens.size();

    // Tokens mit Positionen in den Kontext schreiben
    for (int32_t i = 0; i < batch.n_tokens; i++) {
        batch.token[i] = tokens[(size_t)i];
        batch.pos[i] = i;
        batch.n_seq_id[i] = 1;
        batch.seq_id[i][0] = 0;
        // logits nur für das letzte Token berechnen, damit Sampling starten kann
        batch.logits[i] = (i == batch.n_tokens - 1);
    }

    // Prompt in den Kontext decodieren
    const int rc = llama_decode(st->ctx, batch);
    llama_batch_free(batch);

    if (rc != 0) {
        LOGE("Decode prompt failed rc=%d", rc);
        st->finished = true;
        return -4;
    }

    // Anzahl vergangener Tokens merken
    st->nPast = (int32_t)tokens.size();
    return 0;
}

/**
 * JNI: Liefert das nächste Token als UTF String.
 * Gibt null zurück, wenn fertig, abgebrochen oder Fehler.
 *
 * Ablauf:
 *  1. Token über Sampler aus logits wählen
 *  2. Token accepten
 *  3. Wenn EOG, beenden
 *  4. Token in Kontext decodieren
 *  5. Token in Text umwandeln und zurückgeben
 */
extern "C" JNIEXPORT jstring JNICALL
Java_de_raum7_local_1llm_1learning_llm_LlamaNative_nativeNextToken(
        JNIEnv * env,
        jobject,
        jlong handle
) {
    auto * st = reinterpret_cast<EngineState *>(handle);
    if (!st) return nullptr;

    // Schneller Abbruch ohne Lock
    if (st->abort.load()) return nullptr;

    std::lock_guard<std::mutex> lock(st->mtx);

    // Guard gegen falsche Zustände
    if (!st->started || st->finished) return nullptr;

    // Max Tokens Limit
    if (st->generated >= st->maxTokens) {
        st->finished = true;
        return nullptr;
    }

    // Nächstes Token aus logits sampeln
    const llama_token next = llama_sampler_sample(st->sampler, st->ctx, -1);

    // Sampler mit Token updaten
    llama_sampler_accept(st->sampler, next);

    // End Of Generation Token
    if (llama_vocab_is_eog(st->vocab, next)) {
        st->finished = true;
        return nullptr;
    }

    // Batch für ein Token Decode
    llama_batch batch = llama_batch_init(1, 0, 1);
    batch.n_tokens = 1;
    batch.token[0] = next;
    batch.pos[0] = st->nPast;
    batch.n_seq_id[0] = 1;
    batch.seq_id[0][0] = 0;
    batch.logits[0] = true;

    // Token in Kontext decodieren, damit logits für das nächste Token entstehen
    const int rc = llama_decode(st->ctx, batch);
    llama_batch_free(batch);

    if (rc != 0) {
        LOGE("Decode token failed rc=%d", rc);
        st->finished = true;
        return nullptr;
    }

    // Zähler aktualisieren
    st->nPast += 1;
    st->generated += 1;

    // Control Tokens nicht als Text ausgeben
    if (llama_vocab_is_control(st->vocab, next)) {
        return env->NewStringUTF("");
    }

    // Token zu Textpiece konvertieren und zurückgeben
    const std::string piece = tokenToText(*st, next);
    return env->NewStringUTF(piece.c_str());
}

/**
 * JNI: Setzt Abbruch Flag.
 * Die nächste nativeNextToken Abfrage beendet dann die Generierung.
 */
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
