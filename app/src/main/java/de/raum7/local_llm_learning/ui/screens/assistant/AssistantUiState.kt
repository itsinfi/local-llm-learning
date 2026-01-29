package de.raum7.local_llm_learning.ui.screens.assistant

/**
 * UI State für den Assistant Screen.
 * Enthält alle Daten, die zur Darstellung der Oberfläche benötigt werden.
 */
data class AssistantUiState(

    // Aktueller Nutzereingabe Prompt
    val prompt: String = "",

    // Antwort des LLM
    val answer: String = "",

    // Gibt an, ob gerade eine Generierung läuft
    val isGenerating: Boolean = false,

    // Optionaler Fehlertext, falls die Generierung fehlschlägt
    val error: String? = null
)
