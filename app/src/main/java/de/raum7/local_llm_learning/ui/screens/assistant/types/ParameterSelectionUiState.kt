package de.raum7.local_llm_learning.ui.screens.assistant.types

import de.raum7.local_llm_learning.ui.shared.components.SelectionUiState

data class ParameterSelectionUiState(
    val questionCount: SelectionUiState,
    val depthOfTopic: SelectionUiState,
)
