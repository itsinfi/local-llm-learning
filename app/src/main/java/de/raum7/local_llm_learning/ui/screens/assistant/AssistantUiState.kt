package de.raum7.local_llm_learning.ui.screens.assistant

import android.net.Uri
import de.raum7.local_llm_learning.data.base.BaseUiState

const val DEFAULT_QUESTION_COUNT = 20

data class AssistantUiState(
    val filePath: Uri?,
    val prompt: String = "",
    val questionCount: Int,
    val topicSpecification: String = "",
    val goal: String = "",
) : BaseUiState() {
    companion object {
        // initial ui state
        fun init(): AssistantUiState {
            return AssistantUiState(
                filePath = null,
                questionCount = DEFAULT_QUESTION_COUNT,
            )
        }

        // on form update
        fun from(
            filePath: Uri?,
            prompt: String,
            questionCount: Int,
            topicSpecification: String,
            goal: String,
        ): AssistantUiState {
            return AssistantUiState(
                filePath = filePath,
                prompt = prompt,
                questionCount = questionCount,
                topicSpecification = topicSpecification,
                goal = goal,
            )
        }
    }
}