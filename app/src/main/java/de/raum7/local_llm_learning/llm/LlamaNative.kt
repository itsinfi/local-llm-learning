package de.raum7.local_llm_learning.llm

/**
 * JNI Bridge zur nativen llama.cpp Implementierung.
 * Stellt die Verbindung zwischen Kotlin und C/C++ Code her.
 */
internal object LlamaNative {

    init {
        // Lädt die native Bibliothek beim ersten Zugriff
        System.loadLibrary("llama_jni")
    }

    /**
     * Initialisiert das native Modell und gibt ein Handle zurück.
     *
     * @param modelPath Pfad zur Modell Datei
     * @param contextLength Kontextgröße für das Modell
     * @param threads Anzahl CPU Threads
     * @param seed Seed für deterministische Generierung
     * @return Long Handle auf native Struktur
     */
    external fun nativeInit(
        modelPath: String,
        contextLength: Int,
        threads: Int,
        seed: Int
    ): Long

    /**
     * Gibt alle nativen Ressourcen für das Handle frei.
     */
    external fun nativeFree(handle: Long)

    /**
     * Startet die Generierung im nativen Code.
     *
     * @return Anzahl geplanter Tokens oder Statuscode
     */
    external fun nativeStartGenerate(
        handle: Long,
        prompt: String,
        temperature: Float,
        topP: Float,
        maxTokens: Int
    ): Int

    /**
     * Liefert das nächste generierte Token.
     * Gibt null zurück, wenn keine Tokens mehr verfügbar sind.
     */
    external fun nativeNextToken(
        handle: Long
    ): String?

    /**
     * Bricht die laufende Generierung ab.
     */
    external fun nativeAbort(
        handle: Long
    )
}
