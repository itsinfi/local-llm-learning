package de.raum7.local_llm_learning.llm

import kotlinx.coroutines.flow.Flow

/**
 * Konfiguration für die LLM Generierung.
 * Enthält Parameter für Kontextgröße, Sampling und Limits.
 */
data class LlmConfig(

    // Maximale Kontextlänge, die das Modell verwenden darf
    val contextLength: Int = 1024,

    // Anzahl CPU Threads für Inferenz
    val threads: Int = 6,

    // Sampling Temperatur, höher bedeutet variabler, niedriger deterministischer
    val temperature: Float = 0.2f,

    // Top P Sampling, begrenzt die Token Auswahl auf kumulative Wahrscheinlichkeit
    val topP: Float = 0.95f,

    // Seed für deterministische Ergebnisse, 0 als Standardwert
    val seed: Int = 0,

    // Maximale Anzahl zu generierender Tokens
    val maxTokens: Int = 700
)

/**
 * Events, die während der Generierung über einen Flow emittiert werden.
 */
sealed class LlmEvent {

    // Ein neues Token Stück Text wurde generiert
    data class Token(val text: String) : LlmEvent()

    // Generierung abgeschlossen, inkl. Gesamtzahl erzeugter Tokens
    data class Completed(val totalTokens: Int) : LlmEvent()

    // Fehler während Initialisierung oder Generierung
    data class Error(val message: String) : LlmEvent()
}

/**
 * Abstraktion für eine LLM Engine Implementierung.
 * Kapselt Initialisierung, Token Streaming und Cleanup.
 */
interface LlmEngine {

    /**
     * Initialisiert die Engine mit einem Modellpfad und optionaler Konfiguration.
     * Muss vor generate aufgerufen werden.
     */
    suspend fun init(modelPath: String, config: LlmConfig = LlmConfig())

    /**
     * Startet die Textgenerierung.
     * Gibt einen Flow zurück, der Token, Completed oder Error Events liefert.
     */
    fun generate(prompt: String, config: LlmConfig = LlmConfig()): Flow<LlmEvent>

    /**
     * Räumt Ressourcen auf, z. B. native Handles, Speicher und Threads.
     */
    suspend fun close()
}
