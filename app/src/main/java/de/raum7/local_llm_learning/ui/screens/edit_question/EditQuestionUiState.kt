package de.raum7.local_llm_learning.ui.screens.edit_question

import de.raum7.local_llm_learning.data.models.Question
import de.raum7.local_llm_learning.data.base.BaseUiState

data class EditQuestionUiState(
    val question: Question
) : BaseUiState() {
    companion object {
        fun from(question: Question): EditQuestionUiState {
            return EditQuestionUiState(
                question = question
            )
        }
    }
}