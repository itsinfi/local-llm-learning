package de.raum7.local_llm_learning.ui.screens.edit_question

import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.Question

class EditQuestionViewModel(
    learningMaterialId: String,
    questionId: String,
    private val repository: EditQuestionRepository
) : BaseViewModel(repository) {

    private val question: Question =
        this.repository.getQuestion(questionId, learningMaterialId)

    init {
        val initialState = EditQuestionUiState.from(this.question)
        this._uiState.value = initialState
    }

    fun onEditableAnswerSelected(answer: Answer) {
        this._uiState.value = (this.uiState as EditQuestionUiState).copy(
            selectedEditableAnswer = answer
        )
    }
}