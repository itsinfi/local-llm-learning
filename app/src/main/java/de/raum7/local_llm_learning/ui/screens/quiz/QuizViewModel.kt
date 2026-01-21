package de.raum7.local_llm_learning.ui.screens.quiz

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.data.models.QuizResult
import de.raum7.local_llm_learning.data.base.BaseUiState
import de.raum7.local_llm_learning.data.base.BaseViewModel
import java.util.UUID

class QuizViewModel(
    learningMaterialId: String,
    private val repository: QuizRepository
) : BaseViewModel(repository) {

    private val learningMaterial: LearningMaterial =
        repository.getLearningMaterialById(learningMaterialId)

    override val _uiState: MutableState<BaseUiState> = mutableStateOf(
        QuizUiState.from(learningMaterial)
    )

    override val uiState: BaseUiState get() = _uiState.value

    fun onAnswerSelected(answer: Answer) {
        _uiState.value = (uiState as QuizUiState).copy(
            selectedAnswer = answer
        )
    }

    fun onContinue() {
        when ((uiState as QuizUiState).phase) {
            QuizPhase.ANSWERING -> showResults()
            QuizPhase.RESULTS -> showNextQuestion()
        }
    }

    private fun showResults() {
        val quizUiState = uiState as QuizUiState

        val question = quizUiState.question

        val selectedAnswer = quizUiState.selectedAnswer
            ?: error("No answer selected")

        val correctAnswer = quizUiState.question.answers.firstOrNull { it.isCorrect }
            ?: error("No correct answer found")

        _uiState.value = quizUiState.copy(
            phase = QuizPhase.RESULTS,
            result = QuizResult(
                id = UUID.randomUUID().toString(),
                question = question,
                isCorrect = selectedAnswer == correctAnswer,
                selectedAnswer = selectedAnswer,
                correctAnswer = correctAnswer,
            )
        )
    }

    private fun showNextQuestion() {
        val quizUiState = uiState as QuizUiState

        val questionIndex = (quizUiState.questionIndex + 1) % learningMaterial.questions.size

        _uiState.value = QuizUiState.from(
            learningMaterial,
            questionIndex,
        )
    }
}