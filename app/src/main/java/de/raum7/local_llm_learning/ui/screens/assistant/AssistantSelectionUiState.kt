package de.raum7.local_llm_learning.ui.screens.assistant

data class AssistantSelectionUiState(
    val options: List<String>,
    val selected: String,
    val expanded: Boolean,
    val type: AssistantSelectionUiStateType,
) {
    companion object {
        // on init
        fun from(options: List<String>, type: AssistantSelectionUiStateType): AssistantSelectionUiState {
            val selected = options.firstOrNull() ?: error("Options are empty: $options")

            return AssistantSelectionUiState(
                options = options,
                selected = options.first(),
                expanded = false,
                type = type,
            )
        }

        // on change of selection
        fun from(state: AssistantSelectionUiState, selected: String): AssistantSelectionUiState {
            if (selected !in state.options) {
                error("Selected option $selected is not defined in options: ${state.options}")
            }

            return AssistantSelectionUiState(
                options = state.options,
                selected = selected,
                expanded = false,
                type = state.type,
            )
        }

        // on change of expanded
        fun from(state: AssistantSelectionUiState, expanded: Boolean): AssistantSelectionUiState {
            return AssistantSelectionUiState(
                options = state.options,
                selected = state.selected,
                expanded = expanded,
                type = state.type,
            )
        }
    }
}