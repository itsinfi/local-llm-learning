package de.raum7.local_llm_learning.llm

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

/**
 * Implementierung von LlmEngine auf Basis von llama.cpp via JNI.
 * Läuft auf einem einzelnen nativen Thread, um Thread Safety zu vereinfachen.
 */
class LlamaCppEngine : LlmEngine {

    // Native Handle (Pointer als Long) wird atomar gehalten
    private val handle = AtomicLong(0L)

    // Merker, ob init erfolgreich ausgeführt wurde
    private val isInitialized = AtomicBoolean(false)

    // Single Thread Executor für alle nativen Aufrufe
    private val nativeExecutor = Executors.newSingleThreadExecutor { r ->
        Thread(r, "llama-native")
    }

    // Coroutine Dispatcher, der auf dem nativen Thread läuft
    private val nativeDispatcher = nativeExecutor.asCoroutineDispatcher()

    /**
     * Initialisiert die native Engine.
     * Muss vor generate aufgerufen werden.
     */
    override suspend fun init(modelPath: String, config: LlmConfig) {
        withContext(nativeDispatcher) {

            // Doppelte Initialisierung vermeiden
            if (isInitialized.get()) return@withContext

            // Native Initialisierung
            val h = LlamaNative.nativeInit(
                modelPath = modelPath,
                contextLength = config.contextLength,
                threads = config.threads,
                seed = config.seed
            )

            // 0 bedeutet: Laden fehlgeschlagen
            if (h == 0L) {
                throw IllegalStateException("nativeInit returned 0, model could not be loaded")
            }

            // Handle speichern und Flag setzen
            handle.set(h)
            isInitialized.set(true)
        }
    }

    /**
     * Startet die Generierung und liefert Token Events über einen Flow.
     * Verwendet channelFlow, um aus dem nativen Thread in den Flow zu senden.
     */
    override fun generate(prompt: String, config: LlmConfig): Flow<LlmEvent> = channelFlow {

        // Aktuellen Handle auslesen
        val h = handle.get()

        // Guard: init muss erfolgt sein
        if (!isInitialized.get() || h == 0L) {
            trySend(LlmEvent.Error("Engine not initialized"))
            close()
            return@channelFlow
        }

        // Job auf dem nativen Dispatcher starten
        val job = launch(nativeDispatcher) {
            try {
                // Native Generierung starten
                val started = LlamaNative.nativeStartGenerate(
                    handle = h,
                    prompt = prompt,
                    temperature = config.temperature,
                    topP = config.topP,
                    maxTokens = config.maxTokens
                )

                // 0 bedeutet hier: OK, sonst Fehlercode
                if (started != 0) {
                    trySend(LlmEvent.Error("nativeStartGenerate failed with code $started"))
                    return@launch
                }

                // Token zählen
                var tokens = 0

                // Tokens iterativ abholen, bis null zurückkommt
                while (true) {
                    val piece = LlamaNative.nativeNextToken(h) ?: break

                    // Leere Stücke nicht senden
                    if (piece.isNotEmpty()) {
                        tokens += 1
                        trySend(LlmEvent.Token(piece))
                    }
                }

                // Abschluss Event senden
                trySend(LlmEvent.Completed(tokens))

            } catch (ce: CancellationException) {
                // Bei Cancel versuchen, native Generierung abzubrechen
                runCatching { LlamaNative.nativeAbort(h) }
                throw ce

            } catch (t: Throwable) {
                // Sonstige Fehler in Error Event übersetzen
                trySend(LlmEvent.Error(t.message ?: "Unknown error"))

            } finally {
                // Flow schließen, beendet awaitClose
                close()
            }
        }

        /**
         * Wird aufgerufen, wenn der Collector den Flow abbricht oder der Flow beendet wird.
         * Hier wird der native Job abgebrochen und nativeAbort ausgelöst.
         */
        awaitClose {
            if (job.isActive) {
                job.cancel()

                // Abort auf dem nativen Thread ausführen
                launch(nativeDispatcher) {
                    runCatching { LlamaNative.nativeAbort(h) }
                }
            }
        }
    }

    /**
     * Gibt native Ressourcen frei und beendet den nativen Thread.
     */
    override suspend fun close() {
        withContext(nativeDispatcher) {
            // Handle atomar auf 0 setzen und vorherigen Wert freigeben
            val h = handle.getAndSet(0L)
            if (h != 0L) {
                runCatching { LlamaNative.nativeFree(h) }
            }

            // Init Flag zurücksetzen
            isInitialized.set(false)
        }

        // Dispatcher und Executor schließen
        runCatching { nativeDispatcher.close() }
        runCatching { nativeExecutor.shutdown() }
    }
}
