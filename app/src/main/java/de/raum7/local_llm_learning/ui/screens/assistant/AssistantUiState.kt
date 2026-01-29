package de.raum7.local_llm_learning.ui.screens.assistant

import android.net.Uri
import de.raum7.local_llm_learning.data.base.BaseUiState

val DEFAULT_ASSISTANT_PHASE = AssistantPhase.INITIAL_DESCRIPTION
const val DEFAULT_QUESTION_COUNT = 20
val DEFAULT_DEPTH_OF_TOPIC = DepthOfTopic.BASIC_OVERVIEW

data class AssistantUiState(
    val phase: AssistantPhase,
    val filePath: Uri?,
    val prompt: String,
    val questionCount: Int,
    val depthOfTopic: DepthOfTopic,
    val topicSpecification: String,
    val goal: String,
    val isContinueEnabled: Boolean,
    val questionCountSelectionUiState: AssistantSelectionUiState,
    val depthOfTopicSelectionUiState: AssistantSelectionUiState,
) : BaseUiState() {

    companion object {
        // initial ui state
        fun init(): AssistantUiState {
            return AssistantUiState(
                phase = DEFAULT_ASSISTANT_PHASE,
                filePath = null,
                prompt = "",
                questionCount = DEFAULT_QUESTION_COUNT,
                depthOfTopic = DEFAULT_DEPTH_OF_TOPIC,
                topicSpecification = "",
                goal = "",
                isContinueEnabled = false,
                questionCountSelectionUiState = AssistantSelectionUiState.from(
                    options = QuestionCount.entries.map { it.value.toString() },
                    type = AssistantSelectionUiStateType.QUESTION_COUNT,
                ),
                depthOfTopicSelectionUiState = AssistantSelectionUiState.from(
                    options = DepthOfTopic.entries.map { it.toString() }, // TODO: translations?
                    type = AssistantSelectionUiStateType.DEPTH_OF_TOPIC,
                )
            )
        }

        // on form input update
        fun from(
            state: AssistantUiState,
            change: AssistantUiStateChange,
        ): AssistantUiState {
            return AssistantUiState(
                phase = state.phase,
                filePath = change.filePath ?: state.filePath,
                prompt = change.prompt ?: state.prompt,
                questionCount = change.questionCount?.value ?: state.questionCount,
                depthOfTopic = change.depthOfTopic ?: state.depthOfTopic,
                topicSpecification = change.topicSpecification ?: state.topicSpecification,
                goal = change.goal ?: state.goal,
                isContinueEnabled = true, // TODO:
                questionCountSelectionUiState = state.questionCountSelectionUiState, // TODO:
                depthOfTopicSelectionUiState = state.depthOfTopicSelectionUiState, // TODO:
            )
        }

        // on continue button press
        fun from(
            state: AssistantUiState,
            phase: AssistantPhase,
            isContinueEnabled: Boolean
        ): AssistantUiState {
            return AssistantUiState(
                phase = phase,
                filePath = state.filePath,
                prompt = state.prompt,
                questionCount = state.questionCount,
                depthOfTopic = state.depthOfTopic,
                topicSpecification = state.topicSpecification,
                goal = state.goal,
                isContinueEnabled = isContinueEnabled,
                questionCountSelectionUiState = state.questionCountSelectionUiState,
                depthOfTopicSelectionUiState = state.depthOfTopicSelectionUiState,
            )
        }

        // on change of assistant selection ui state selected
        fun from(
            state: AssistantUiState,
            type: AssistantSelectionUiStateType,
            selected: String,
        ): AssistantUiState {
            when(type) {
                AssistantSelectionUiStateType.QUESTION_COUNT -> return AssistantUiState(
                    phase = state.phase,
                    filePath = state.filePath,
                    prompt = state.prompt,
                    questionCount = state.questionCount,
                    depthOfTopic = state.depthOfTopic,
                    topicSpecification = state.topicSpecification,
                    goal = state.goal,
                    isContinueEnabled = state.isContinueEnabled,
                    questionCountSelectionUiState = AssistantSelectionUiState.from(
                        state = state.questionCountSelectionUiState,
                        selected = selected,
                    ),
                    depthOfTopicSelectionUiState = state.depthOfTopicSelectionUiState,
                )

                AssistantSelectionUiStateType.DEPTH_OF_TOPIC -> return AssistantUiState(
                    phase = state.phase,
                    filePath = state.filePath,
                    prompt = state.prompt,
                    questionCount = state.questionCount,
                    depthOfTopic = state.depthOfTopic,
                    topicSpecification = state.topicSpecification,
                    goal = state.goal,
                    isContinueEnabled = state.isContinueEnabled,
                    questionCountSelectionUiState = state.questionCountSelectionUiState,
                    depthOfTopicSelectionUiState = AssistantSelectionUiState.from(
                        state = state.depthOfTopicSelectionUiState,
                        selected = selected,
                    ),
                )
            }
        }

        // on change of assistant selection ui state expanded
        fun from(
            state: AssistantUiState,
            type: AssistantSelectionUiStateType,
            expanded: Boolean,
        ): AssistantUiState {
            when(type) {
                AssistantSelectionUiStateType.QUESTION_COUNT -> return AssistantUiState(
                    phase = state.phase,
                    filePath = state.filePath,
                    prompt = state.prompt,
                    questionCount = state.questionCount,
                    depthOfTopic = state.depthOfTopic,
                    topicSpecification = state.topicSpecification,
                    goal = state.goal,
                    isContinueEnabled = state.isContinueEnabled,
                    questionCountSelectionUiState = AssistantSelectionUiState.from(
                        state = state.questionCountSelectionUiState,
                        expanded = expanded,
                    ),
                    depthOfTopicSelectionUiState = state.depthOfTopicSelectionUiState,
                )

                AssistantSelectionUiStateType.DEPTH_OF_TOPIC -> return AssistantUiState(
                    phase = state.phase,
                    filePath = state.filePath,
                    prompt = state.prompt,
                    questionCount = state.questionCount,
                    depthOfTopic = state.depthOfTopic,
                    topicSpecification = state.topicSpecification,
                    goal = state.goal,
                    isContinueEnabled = state.isContinueEnabled,
                    questionCountSelectionUiState = state.questionCountSelectionUiState,
                    depthOfTopicSelectionUiState = AssistantSelectionUiState.from(
                        state = state.depthOfTopicSelectionUiState,
                        expanded = expanded,
                    ),
                )
            }
        }
    }
}