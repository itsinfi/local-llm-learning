package de.raum7.local_llm_learning.llm

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Repository Schicht für den Assistant.
 * Kapselt Modell Installation, Engine Initialisierung und Generierung.
 */
class AssistantRepository(
    private val context: Context,
    private val engine: LlmEngine = LlamaCppEngine()
) {
    // Mutex schützt ensureReady vor parallelen Initialisierungen
    private val lock = Mutex()

    // Flag, ob Modell und Engine bereits initialisiert wurden
    private var ready = false

    /**
     * Stellt sicher, dass Modell Datei vorhanden ist und die Engine initialisiert wurde.
     * Wird typischerweise einmal vor der ersten Generierung aufgerufen.
     *
     * @param assetModelPath Pfad in Assets, z. B. "models/model.gguf"
     * @param targetFileName Dateiname im internen Speicher, z. B. "model.gguf"
     * @param config Engine Konfiguration
     */
    suspend fun ensureReady(
        assetModelPath: String,
        targetFileName: String,
        config: LlmConfig = LlmConfig()
    ) {
        lock.withLock {
            // Bereits initialisiert
            if (ready) return

            // Modell aus Assets ins interne Dateisystem kopieren, falls nötig
            val modelFile = withContext(Dispatchers.IO) {
                ModelInstaller.ensureModelInstalled(
                    context = context,
                    assetPath = assetModelPath,
                    targetFileName = targetFileName
                )
            }

            // Engine mit lokalem Pfad initialisieren
            engine.init(modelFile.absolutePath, config)

            // Merken, dass alles bereit ist
            ready = true
        }
    }

    /**
     * Startet die Generierung über die Engine und liefert Events als Flow zurück.
     */
    fun generate(prompt: String, config: LlmConfig = LlmConfig()): Flow<LlmEvent> {
        return engine.generate(prompt, config)
    }

    /**
     * Ressourcen freigeben.
     * Sollte z. B. beim Beenden des Services oder der App aufgerufen werden.
     */
    suspend fun close() {
        engine.close()
    }
}
