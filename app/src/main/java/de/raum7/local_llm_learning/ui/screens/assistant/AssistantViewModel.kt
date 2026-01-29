package de.raum7.local_llm_learning.ui.screens.assistant

import de.raum7.local_llm_learning.data.base.BaseViewModel

class AssistantViewModel(
    private val repository: AssistantRepository
) : BaseViewModel(repository) {

    init {
        val initialState = AssistantUiState.init()
        this._uiState.value = initialState
    }

    fun onContinue() {
        // TODO: finish
        val state = this.uiState as AssistantUiState
        val phase = when(state.phase) {
            AssistantPhase.INITIAL_DESCRIPTION -> AssistantPhase.PARAMETER_SELECTION
            AssistantPhase.PARAMETER_SELECTION -> AssistantPhase.FURTHER_SPECIFICATION
            AssistantPhase.FURTHER_SPECIFICATION -> AssistantPhase.INITIAL_DESCRIPTION
        }
        val isContinueEnabled = true
        this._uiState.value = AssistantUiState.from(state, phase, isContinueEnabled)
    }

    fun onChanged(change: AssistantUiStateChange) {
        val state = this.uiState as AssistantUiState
        this._uiState.value = AssistantUiState.from(state, change)
    }

    fun onSelected(selected: String, type: AssistantSelectionUiStateType) {
        val state = this.uiState as AssistantUiState
        this._uiState.value = AssistantUiState.from(state, type, selected)
    }

    fun onExpandedChange(expanded: Boolean, type: AssistantSelectionUiStateType) {
        val state = this.uiState as AssistantUiState
        this._uiState.value = AssistantUiState.from(state, type, expanded)
    }
}