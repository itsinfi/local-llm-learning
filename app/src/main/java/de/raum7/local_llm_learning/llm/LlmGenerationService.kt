package de.raum7.local_llm_learning.llm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import de.raum7.local_llm_learning.MainActivity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Foreground Service für die LLM Textgenerierung.
 * Nimmt Start und Stop Befehle per Intent entgegen, erzeugt Tokens im Hintergrund
 * und meldet Ergebnis oder Fehler per Broadcast an die UI zurück.
 */
class LlmGenerationService : LifecycleService() {

    companion object {
        // Aktionen zum Steuern des Services
        const val ACTION_START = "LLM_START"
        const val ACTION_STOP = "LLM_STOP"

        // Extra für den Prompt Eingang
        const val EXTRA_PROMPT = "PROMPT"

        // Broadcast Action und Extras für Ergebnis Rückgabe
        const val ACTION_RESULT = "LLM_RESULT"
        const val EXTRA_RESULT_TEXT = "RESULT_TEXT"
        const val EXTRA_RESULT_ERROR = "RESULT_ERROR"

        // Notification ID für Foreground und Finished Notification
        private const val NOTIFICATION_ID = 1
    }

    // Repository, kapselt Modell Setup und Inferenz
    private val repo by lazy { AssistantRepository(applicationContext) }

    // Aktiver Coroutine Job für die Generierung
    private var job: Job? = null

    // Merker, ob das Modell bereits initialisiert wurde
    private var isReady = false

    // Konfiguration für die Generierung
    private val config = LlmConfig(
        contextLength = 2028,
        threads = 4,
        temperature = 0.2f,
        topP = 0.95f,
        seed = 0,
        maxTokens = 700
    )

    override fun onCreate() {
        super.onCreate()
        // Notification Channel anlegen, damit Foreground Service Notifications funktionieren
        NotificationHelper.createChannel(this)
    }

    /**
     * Einstiegspunkt für Start und Stop Befehle.
     * Bei ACTION_START wird die Generierung gestartet, bei ACTION_STOP wird sie abgebrochen.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        when (intent?.action) {

            ACTION_START -> {
                // Prompt aus Intent lesen
                val prompt = intent.getStringExtra(EXTRA_PROMPT).orEmpty().trim()

                // Leeren Prompt ignorieren
                if (prompt.isEmpty()) return START_NOT_STICKY

                // Wenn bereits eine Generierung läuft, keinen neuen Job starten
                if (job != null) return START_NOT_STICKY

                // Foreground Notification starten
                startForeground(
                    NOTIFICATION_ID,
                    NotificationHelper.running(this, "Generierung läuft")
                )

                // Generierung starten
                startGeneration(prompt)
            }

            ACTION_STOP -> stopGeneration()
        }

        // Nicht automatisch neu starten, falls Prozess beendet wird
        return START_NOT_STICKY
    }

    /**
     * Startet die Token Generierung in einer Coroutine auf Dispatchers.Default.
     * Initialisiert das Modell einmalig über ensureReady.
     */
    private fun startGeneration(prompt: String) {
        job = lifecycleScope.launch(Dispatchers.Default) {
            try {
                // Modell initialisieren, falls noch nicht geschehen
                if (!isReady) {
                    repo.ensureReady(
                        assetModelPath = "models/model.gguf",
                        targetFileName = "model.gguf",
                        config = config
                    )
                    isReady = true
                }



                // Ergebnis akkumulieren
                val result = StringBuilder()

                // Token Counter für Status Updates
                var tokenCount = 0

                // Token Stream sammeln
                val endMarker = "<END_JSON>"

                repo.generate(prompt, config).collect { ev ->
                    when (ev) {

                        is LlmEvent.Token -> {
                            result.append(ev.text)
                            tokenCount += 1

                            val current = result.toString()
                            val markerIndex = current.indexOf(endMarker)

                            // Sobald das Modell den Marker erzeugt -> sofort stoppen
                            if (markerIndex >= 0) {
                                val jsonOnly = current.substring(0, markerIndex).trim()

                                finishSuccess(jsonOnly)

                                // Coroutine sauber abbrechen
                                throw CancellationException("Stopped after END_JSON")
                            }

                            if (tokenCount % 50 == 0) {
                                updateRunningNotification("Generierung läuft, Tokens: $tokenCount")
                            }
                        }

                        is LlmEvent.Completed -> {
                            val out = result.toString()
                            val idx = out.indexOf(endMarker)
                            val jsonOnly = if (idx >= 0) out.substring(0, idx).trim() else out.trim()
                            finishSuccess(jsonOnly)
                        }

                        is LlmEvent.Error -> finishError(ev.message)
                    }
                }

            } catch (ce: CancellationException) {
                // Abbruch durch stopGeneration
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            } catch (t: Throwable) {
                // Sonstiger Fehler
                finishError(t.message ?: "Unknown error")
            } finally {
                // Job Referenz freigeben
                job = null
            }
        }
    }

    /**
     * Stoppt die Generierung, falls ein Job läuft.
     */
    private fun stopGeneration() {
        job?.cancel()
        job = null

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    /**
     * Erfolgsfall: Broadcast an UI senden, Finished Notification anzeigen, Service stoppen.
     */
    private fun finishSuccess(text: String) {
        sendResultBroadcast(text = text, error = null)

        // PendingIntent, um MainActivity mit Ergebnis zu öffnen
        val openPending = buildOpenPendingIntent(text = text, error = null)

        // Finished Notification setzen
        notifyFinished(openPending)

        // Foreground entfernen, Notification optional bestehen lassen
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    /**
     * Fehlerfall: Broadcast an UI senden, Finished Notification anzeigen, Service stoppen.
     */
    private fun finishError(message: String) {
        sendResultBroadcast(text = "", error = message)

        val openPending = buildOpenPendingIntent(text = "", error = message)
        notifyFinished(openPending)

        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    /**
     * Sendet das Ergebnis per Broadcast innerhalb des eigenen Pakets.
     * Die UI hört auf ACTION_RESULT.
     */
    private fun sendResultBroadcast(text: String, error: String?) {
        val i = Intent(ACTION_RESULT).apply {
            setPackage(packageName)
            putExtra(EXTRA_RESULT_TEXT, text)
            putExtra(EXTRA_RESULT_ERROR, error)
        }
        sendBroadcast(i)
    }

    /**
     * Erstellt ein PendingIntent, das beim Antippen der Finished Notification die MainActivity öffnet.
     * Ergebnis und Fehler werden als Extras mitgegeben.
     */
    private fun buildOpenPendingIntent(text: String, error: String?): PendingIntent {
        val openIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(EXTRA_RESULT_TEXT, text)
            putExtra(EXTRA_RESULT_ERROR, error)
        }

        return PendingIntent.getActivity(
            this,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    /**
     * Zeigt eine Finished Notification an.
     */
    private fun notifyFinished(openPending: PendingIntent) {
        val nm = getSystemService(NotificationManager::class.java)
        nm.notify(NOTIFICATION_ID, NotificationHelper.finished(this, openPending))
    }

    /**
     * Aktualisiert die Foreground Notification während der laufenden Generierung.
     */
    private fun updateRunningNotification(text: String) {
        val nm = getSystemService(NotificationManager::class.java)
        nm.notify(NOTIFICATION_ID, NotificationHelper.running(this, text))
    }

    /**
     * Service Cleanup.
     * Repository wird synchron geschlossen, um Native Ressourcen sauber freizugeben.
     */
    override fun onDestroy() {
        try {
            runBlocking { repo.close() }
        } catch (_: Throwable) {
            // Fehler beim Cleanup ignorieren
        }
        super.onDestroy()
    }
}
