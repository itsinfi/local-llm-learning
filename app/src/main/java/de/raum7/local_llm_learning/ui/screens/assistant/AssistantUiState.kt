package de.raum7.local_llm_learning.ui.screens.assistant

import de.raum7.local_llm_learning.data.base.BaseUiState
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantPhase
import de.raum7.local_llm_learning.ui.screens.assistant.types.DepthOfTopic
import de.raum7.local_llm_learning.ui.screens.assistant.types.FurtherSpecification
import de.raum7.local_llm_learning.ui.screens.assistant.types.InitialDescription
import de.raum7.local_llm_learning.ui.screens.assistant.types.ParameterSelection
import de.raum7.local_llm_learning.ui.screens.assistant.types.QuestionCount
import de.raum7.local_llm_learning.ui.shared.components.SelectionUiState

data class AssistantUiState(
    val phase: AssistantPhase,
    val isContinueEnabled: Boolean,
    val initialDescription: InitialDescription,
    val parameterSelection: ParameterSelection,
    val furtherSpecification: FurtherSpecification,
) : BaseUiState() {

    companion object {
        fun from(): AssistantUiState {
            return AssistantUiState(
                phase = DEFAULT_ASSISTANT_PHASE,
                isContinueEnabled = false,
                initialDescription = InitialDescription(
                    filePath = null,
                    prompt = "",
                ),
                parameterSelection = ParameterSelection(
                    questionCount = SelectionUiState.from(
                        options = QuestionCount.entries.map { it.value.toString() },
                        selected = DEFAULT_QUESTION_COUNT.value.toString(),
                    ),
                    depthOfTopic = SelectionUiState.from(
                        options = DepthOfTopic.entries.map { it.toString() },
                        selected = DEFAULT_DEPTH_OF_TOPIC.toString(),
                    )
                ),
                furtherSpecification = FurtherSpecification(
                    topicSpecification = "",
                    goal = "",
                ),
            )
        }
    }
}