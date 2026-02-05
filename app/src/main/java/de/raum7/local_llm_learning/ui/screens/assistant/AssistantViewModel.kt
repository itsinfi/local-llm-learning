package de.raum7.local_llm_learning.ui.screens.assistant

import android.net.Uri
import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantCardUiState
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantPhase
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantUiStateChange

class AssistantViewModel(
    private val repository: AssistantRepository
) : BaseViewModel(repository) {

    init {
        val initialState = AssistantUiState.from()
        this._uiState.value = initialState
    }

    private fun isContinueAllowed(
        phase: AssistantPhase? = null,
        prompt: String? = null,
        filePath: Uri? = null,
    ): Boolean {
        val state = this.uiState as AssistantUiState

        return when(phase ?: state.assistantCard.phase) {
            AssistantPhase.INITIAL_DESCRIPTION ->
                (prompt ?: state.initialDescription.prompt).isNotEmpty() ||
                    (filePath ?: state.initialDescription.filePath) != null

            else -> true
        }
    }

    fun onContinue() {
        val state = this.uiState as AssistantUiState

        val phase = when(state.assistantCard.phase) {
            AssistantPhase.INITIAL_DESCRIPTION -> AssistantPhase.PARAMETER_SELECTION
            else -> AssistantPhase.FURTHER_SPECIFICATION // TODO: handle output generation
        }

        val isContinueEnabled = isContinueAllowed(phase)

        this._uiState.value = state.copy(
            assistantCard = AssistantCardUiState(
                phase = phase,
                isContinueEnabled = isContinueEnabled,
            )
        )
    }

    fun onBack() {
        val state = this.uiState as AssistantUiState

        val phase = when(state.assistantCard.phase) {
            AssistantPhase.FURTHER_SPECIFICATION -> AssistantPhase.PARAMETER_SELECTION
            else -> AssistantPhase.INITIAL_DESCRIPTION
        }

        val isContinueEnabled = isContinueAllowed(phase)

        this._uiState.value = state.copy(
            assistantCard = AssistantCardUiState(
                phase = phase,
                isContinueEnabled = isContinueEnabled
            )
        )
    }

    fun onChanged(change: AssistantUiStateChange) {
        val state = this.uiState as AssistantUiState

        val isContinueEnabled = isContinueAllowed(
            prompt = change.prompt,
            filePath = change.filePath,
        )

        this._uiState.value = state.copy(
            assistantCard = state.assistantCard.copy(
                isContinueEnabled = isContinueEnabled,
            ),
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