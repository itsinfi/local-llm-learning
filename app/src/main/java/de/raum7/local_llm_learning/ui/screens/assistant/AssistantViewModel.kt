package de.raum7.local_llm_learning.ui.screens.assistant

import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantPhase
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantUiStateChange

class AssistantViewModel(
    private val repository: AssistantRepository
) : BaseViewModel(repository) {

    init {
        val initialState = AssistantUiState.from()
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

        this._uiState.value = state.copy(
            phase = phase,
            isContinueEnabled = isContinueEnabled,
        )
    }

    fun onChanged(change: AssistantUiStateChange) {
        val state = this.uiState as AssistantUiState

        val isContinueEnabled = true // TODO:

        this._uiState.value = state.copy(
            isContinueEnabled = isContinueEnabled,
            initialDescription = state.initialDescription.copy(
                filePath = change.filePath ?: state.initialDescription.filePath,
                prompt = change.prompt ?: state.initialDescription.prompt,
            ),
            parameterSelection = state.parameterSelection.copy(
                questionCount = state.parameterSelection.questionCount.copy(
                    selected = change.questionCount?.value?.toString()
                        ?: state.parameterSelection.questionCount.selected,
                    expanded =  change.questionCountExpanded
                        ?: state.parameterSelection.questionCount.expanded,
                ),
                depthOfTopic = state.parameterSelection.depthOfTopic.copy(
                    selected = change.depthOfTopic?.toString()
                        ?: state.parameterSelection.depthOfTopic.selected,
                    expanded =  change.depthOfTopicExpanded
                        ?: state.parameterSelection.depthOfTopic.expanded,
                )
            ),
            furtherSpecification = state.furtherSpecification.copy(
                topicSpecification = change.topicSpecification ?: state.furtherSpecification.topicSpecification,
                goal = change.goal ?: state.furtherSpecification.goal,
            )
        )
    }
}