package de.raum7.local_llm_learning.ui.screens.quiz

import androidx.lifecycle.viewModelScope
import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.data.models.QuizResult
import de.raum7.local_llm_learning.ui.screens.quiz.types.QuizPhase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class QuizViewModel(
    learningMaterialId: String,
    private val repository: QuizRepository
) : BaseViewModel(repository) {

    private val learningMaterial: LearningMaterial =
        repository.getLearningMaterialById(learningMaterialId)

    init {
        val initialState = QuizUiState.from(learningMaterial)
        _uiState.value = initialState
        startTimer(initialState.startedAt)
    }

    private var timerJob: Job? = null

    private fun startTimer(startedAt: Long) {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (true) {
                val elapsed = System.nanoTime() - startedAt

                _uiState.value = (uiState as QuizUiState).copy(
                    elapsedTime = elapsed
                )

                delay(TIMER_UPDATE_CYCLE)
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun onAnswerSelected(answer: Answer) {
        val state = uiState as QuizUiState

        _uiState.value = state.copy(
            selectedAnswer = answer
        )
    }

    fun onContinue() {
        val state = uiState as QuizUiState

        when (state.phase) {
            QuizPhase.ANSWERING -> showResults()
            QuizPhase.RESULTS -> showNextQuestion()
        }
    }

    private fun showResults() {
        stopTimer()

        val state = uiState as QuizUiState
        val endedAt = System.nanoTime()
        val elapsed = endedAt - state.startedAt

        val question = state.question
        val selectedAnswer = state.selectedAnswer ?: return
        val correctAnswer = question.answers.first { it.isCorrect }

        val result = QuizResult(
            id = UUID.randomUUID().toString(),
            question = question,
            isCorrect = selectedAnswer == correctAnswer,
            selectedAnswer = selectedAnswer,
            correctAnswer = correctAnswer,
            elapsedNanoSeconds = elapsed,
            previousNanoSeconds = elapsed
        )

        _uiState.value = state.copy(
            phase = QuizPhase.RESULTS,
            result = result,
            elapsedTime = elapsed
        )
    }

    private fun showNextQuestion() {
        val state = uiState as QuizUiState

        val nextIndex =
            (learningMaterial.questions.indexOf(state.question) + 1) %
                    learningMaterial.questions.size

        val newState = QuizUiState.from(
            learningMaterial = learningMaterial,
            questionIndex = nextIndex
        )

        _uiState.value = newState
        startTimer(newState.startedAt)
    }
}
