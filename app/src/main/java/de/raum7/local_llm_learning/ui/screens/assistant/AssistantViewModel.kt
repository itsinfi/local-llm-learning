package de.raum7.local_llm_learning.ui.screens.assistant

import de.raum7.local_llm_learning.data.base.BaseViewModel

class AssistantViewModel(
    private val repository: AssistantRepository
) : BaseViewModel(repository) {
    init {
        val initialState = AssistantUiState.init()
        this._uiState.value = initialState
    }
}