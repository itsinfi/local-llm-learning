package de.raum7.local_llm_learning.ui.screens.quiz

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.data.models.QuizResult
import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.ui.screens.quiz.types.QuizPhase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.UUID

class QuizViewModel(
    learningMaterialId: Int,
    private val repository: QuizRepository
) : BaseViewModel(repository) {

    private var learningMaterial: LearningMaterial = TODO()

    init {
        runBlocking {
            this@QuizViewModel.learningMaterial = this@QuizViewModel.repository.getLearningMaterialById(learningMaterialId)
            val question = repository.getNextQuestionById(-1, this@QuizViewModel.learningMaterial.id)
            val answers = repository.getAnswersForQuestion(question.id)
            val questionCount = repository.getQuestionCountForLearningMaterial(learningMaterial.id)
            val initialState = QuizUiState.from(this@QuizViewModel.learningMaterial, questionCount, question, answers)
            this@QuizViewModel._uiState.value = initialState
            startTimer(initialState.startedAt)
        }

    }

    private var timerJob: Job? = null

    private fun startTimer(startedAt: Long) {
        this.timerJob?.cancel()

        this.timerJob = viewModelScope.launch {
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
        this.timerJob?.cancel()
        this.timerJob = null
    }

    fun onAnswerSelected(answer: Answer) {
        val state = this.uiState as QuizUiState

        this._uiState.value = state.copy(
            selectedAnswer = answer
        )
    }

    fun onContinue() {
        val state = this.uiState as QuizUiState

        when (state.phase) {
            QuizPhase.ANSWERING -> showResults()
            QuizPhase.RESULTS -> showNextQuestion()
        }
    }

    private fun showResults() {
        this.stopTimer()

        val state = this.uiState as QuizUiState

        val endedAt: Long = System.nanoTime()
        val elapsedNanoSeconds: Long = endedAt - state.startedAt

        val question = state.question

        val selectedAnswer = state.selectedAnswer
            ?: error("No answer selected")

        val correctAnswer = state.answers.firstOrNull { it.isCorrect }
            ?: error("No correct answer found")

        val result = QuizResult(
            id = UUID.randomUUID().toString(),
            question = question,
            isCorrect = selectedAnswer == correctAnswer,
            selectedAnswer = selectedAnswer,
            correctAnswer = correctAnswer,
            elapsedNanoSeconds = elapsedNanoSeconds,
            previousNanoSeconds = elapsedNanoSeconds, // TODO: add previous average
        )

        this._uiState.value = state.copy(
            phase = QuizPhase.RESULTS,
            result = result,
            elapsedTime = elapsedNanoSeconds,
        )
    }

    private fun showNextQuestion() {
        viewModelScope.launch() {
            val state = this@QuizViewModel.uiState as QuizUiState

            // TODO: only temporary code, please add question selection via spaced repetition
            val question = repository.getNextQuestionById(state.question.id, this@QuizViewModel.learningMaterial.id)
            val answers = repository.getAnswersForQuestion(state.question.id)
            val questionCount = repository.getQuestionCountForLearningMaterial(learningMaterial.id)

            // TODO: use from function signature with questionId as a parameter
            val newState = QuizUiState.from(
                learningMaterial,
                questionCount,
                question,
                answers
            )

            this@QuizViewModel._uiState.value = newState
            this@QuizViewModel.startTimer(newState.startedAt)
        }

    }
}