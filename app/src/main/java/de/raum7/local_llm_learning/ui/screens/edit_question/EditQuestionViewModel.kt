package de.raum7.local_llm_learning.ui.screens.edit_question

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EditQuestionViewModel(
    learningMaterialId: Int,
    questionId: Int,
    private val repository: EditQuestionRepository
) : BaseViewModel(repository) {

    init {
        runBlocking {
            val question: Question = this@EditQuestionViewModel.repository.getQuestion(questionId)
            val answers: List<Answer> = this@EditQuestionViewModel.repository.getAnswersForQuestion(questionId)
            val initialState = EditQuestionUiState.from(question, answers)
            this@EditQuestionViewModel._uiState.value = initialState
        }


    }

    fun onEditableAnswerSelected(answer: Answer) {
        this._uiState.value = (this.uiState as EditQuestionUiState).copy(
            selectedEditableAnswer = answer
        )
    }

    fun onQuestionSave(answers: List<Answer>) {
        viewModelScope.launch {
            this@EditQuestionViewModel.repository.upsertAnswers(answers)
        }

    }
}