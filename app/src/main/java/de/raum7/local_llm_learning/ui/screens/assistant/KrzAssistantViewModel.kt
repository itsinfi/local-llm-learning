package de.raum7.local_llm_learning.ui.screens.assistant

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import de.raum7.local_llm_learning.llm.LlmGenerationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel für den Assistant Screen.
 * Verwaltet den UI State und die Kommunikation mit dem LlmGenerationService.
 */
class KrzAssistantViewModel(app: Application) : AndroidViewModel(app) {

    // Interner, veränderbarer State
    private val _state = MutableStateFlow(KrzAssistantUiState())

    // Exponierter, nur lesbarer State für die UI
    val state: StateFlow<KrzAssistantUiState> = _state

    // Application Context, damit kein Activity-Leak entsteht
    private val appCtx: Context = app.applicationContext

    /**
     * BroadcastReceiver, der Ergebnisse vom LlmGenerationService empfängt.
     * Wird aufgerufen, sobald der Service ein Resultat sendet.
     */
    private val resultReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Nur relevante Intents verarbeiten
            if (intent?.action != LlmGenerationService.ACTION_RESULT) return

            // Ergebnistext auslesen
            val text = intent.getStringExtra(LlmGenerationService.EXTRA_RESULT_TEXT).orEmpty()

            // Optionaler Fehlertext
            val error = intent.getStringExtra(LlmGenerationService.EXTRA_RESULT_ERROR)

            // UI State aktualisieren
            _state.update {
                it.copy(
                    isGenerating = false,
                    answer = if (error == null) text else it.answer,
                    error = error
                )
            }
        }
    }

    init {
        // Receiver direkt beim Erstellen des ViewModels registrieren
        registerReceiver()
    }

    /**
     * Wird aufgerufen, wenn sich der Prompt im UI ändert.
     */
    fun onPromptChanged(value: String) {
        _state.update { it.copy(prompt = value) }
    }

    /**
     * Startet eine neue LLM Anfrage über den Service.
     */
    fun send() {
        val prompt = state.value.prompt.trim()

        // Leere Prompts ignorieren
        if (prompt.isEmpty()) return

        // Keine neue Anfrage starten, wenn bereits eine läuft
        if (state.value.isGenerating) return

        // UI State vorbereiten
        _state.update { it.copy(answer = "", error = null, isGenerating = true) }

        // Intent für den Service erstellen
        val i = Intent(appCtx, LlmGenerationService::class.java).apply {
            action = LlmGenerationService.ACTION_START
            putExtra(LlmGenerationService.EXTRA_PROMPT, prompt)
        }

        // Ab Android 8 muss ein Foreground Service verwendet werden
        if (Build.VERSION.SDK_INT >= 26) {
            appCtx.startForegroundService(i)
        } else {
            appCtx.startService(i)
        }
    }

    /**
     * Stoppt die aktuelle Generierung im Service.
     */
    fun stop() {
        val i = Intent(appCtx, LlmGenerationService::class.java).apply {
            action = LlmGenerationService.ACTION_STOP
        }
        appCtx.startService(i)

        // UI sofort zurücksetzen
        _state.update { it.copy(isGenerating = false) }
    }

    /**
     * Registriert den BroadcastReceiver für Service Ergebnisse.
     */
    private fun registerReceiver() {
        val filter = IntentFilter(LlmGenerationService.ACTION_RESULT)

        // Ab Android 13 muss explizit angegeben werden, ob der Receiver exportiert ist
        if (Build.VERSION.SDK_INT >= 33) {
            appCtx.registerReceiver(resultReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            appCtx.registerReceiver(resultReceiver, filter)
        }
    }

    /**
     * Wird aufgerufen, wenn das ViewModel zerstört wird.
     * Receiver wird sauber deregistriert.
     */
    override fun onCleared() {
        runCatching { appCtx.unregisterReceiver(resultReceiver) }
        super.onCleared()
    }
}
