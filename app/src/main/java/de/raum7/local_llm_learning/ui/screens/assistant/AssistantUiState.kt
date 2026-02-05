package de.raum7.local_llm_learning.ui.screens.assistant

import de.raum7.local_llm_learning.data.base.BaseUiState
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantCardUiState
import de.raum7.local_llm_learning.ui.screens.assistant.types.DepthOfTopic
import de.raum7.local_llm_learning.ui.screens.assistant.types.FurtherSpecificationUiState
import de.raum7.local_llm_learning.ui.screens.assistant.types.InitialDescriptionUiState
import de.raum7.local_llm_learning.ui.screens.assistant.types.ParameterSelectionUiState
import de.raum7.local_llm_learning.ui.screens.assistant.types.QuestionCount
import de.raum7.local_llm_learning.ui.shared.components.SelectionUiState

data class AssistantUiState(
    val assistantCard: AssistantCardUiState,
    val initialDescription: InitialDescriptionUiState,
    val parameterSelection: ParameterSelectionUiState,
    val furtherSpecification: FurtherSpecificationUiState,

    val isGenerating: Boolean = false,
    val generationResult: String = "",
    val generationError: String? = null,
) : BaseUiState() {

    companion object {
        fun from(): AssistantUiState {
            return AssistantUiState(
                assistantCard = AssistantCardUiState(
                    phase = DEFAULT_ASSISTANT_PHASE,
                    isContinueEnabled = false,
                ),
                initialDescription = InitialDescriptionUiState(
                    filePath = null,
                    prompt = "",
                ),
                parameterSelection = ParameterSelectionUiState(
                    questionCount = SelectionUiState.from(
                        options = QuestionCount.entries.map { it.value.toString() },
                        selected = DEFAULT_QUESTION_COUNT.value.toString(),
                    ),
                    depthOfTopic = SelectionUiState.from(
                        options = DepthOfTopic.entries.map { it.toString() },
                        selected = DEFAULT_DEPTH_OF_TOPIC.toString(),
                    )
                ),
                furtherSpecification = FurtherSpecificationUiState(
                    topicSpecification = "",
                    goal = "",
                ),
            )
        }
    }
}