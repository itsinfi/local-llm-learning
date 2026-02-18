package de.raum7.local_llm_learning.ui.screens.quiz

import android.util.Log
import androidx.lifecycle.viewModelScope
import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.Question
import de.raum7.local_llm_learning.data.models.QuizResult
import de.raum7.local_llm_learning.ui.screens.quiz.types.QuizPhase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.UUID
import de.raum7.local_llm_learning.data.spaced_repitition.ENFORCED_DELAY
import de.raum7.local_llm_learning.data.spaced_repitition.A
import de.raum7.local_llm_learning.data.spaced_repitition.B
import de.raum7.local_llm_learning.data.spaced_repitition.DEFAULT_PRIORITY
import de.raum7.local_llm_learning.data.spaced_repitition.R
import de.raum7.local_llm_learning.data.spaced_repitition.W
import kotlin.math.ln

class QuizViewModel(
    learningMaterialId: Int,
    private val repository: QuizRepository
) : BaseViewModel(repository) {

    init {
        runBlocking {
            val learningMaterial = this@QuizViewModel.repository.getLearningMaterialById(learningMaterialId)
            val question = repository.getNextQuestion(learningMaterial.id, DEFAULT_PRIORITY==null) // look for question with priority == null if default priority is null
            val answers = repository.getAnswersForQuestion(question.id)
            val questionCount = repository.getQuestionCountForLearningMaterial(learningMaterial.id)
            val initialState = QuizUiState.from(learningMaterial, questionCount, question, answers)
            this@QuizViewModel._uiState.value = initialState
            startTimer(initialState.startedAt)
        }

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
            elapsedNanoSeconds = elapsed,
            previousNanoSeconds = elapsed
        )

        // calculate priority for current question
        /*
        steps:
        1. update question specific parameters
        2. increment lastPresented for each question except current one
        3. calculate priority for each question except for those not answered yet
         */
        question.accuracy = if(selectedAnswer == correctAnswer) {
            0
        } else {
            1
        }
        question.trialsSinceLastPresented = 0
        question.rt = elapsed.toDouble() * 0.000000001

        Log.d("PriorityCalculation", "Priority Calculation---------------------------------")
        var learningMaterial = state.learningMaterial
        runBlocking {
            repository.updateQuestion(question)
            val questions = repository.getAnsweredQuestions(learningMaterial.id)
            questions.forEach {
                if (it.id != question.id) {
                    it.trialsSinceLastPresented++
                }
                it.priority = calculatePriority(it)

            }
            repository.updateQuestions(questions)

            // calculate and update progress of learning material
            val totalQuestionCount = repository.getQuestionCount(learningMaterial.id)
            val masteredQuestionCount = repository.getMasteredQuestionCount(learningMaterial.id)
            val learningMaterialProgress = masteredQuestionCount.toDouble() / totalQuestionCount.toDouble()
            learningMaterial.progress = learningMaterialProgress
            repository.updateLearningMaterial(learningMaterial)

        }

        _uiState.value = state.copy(
            phase = QuizPhase.RESULTS,
            result = result,
            elapsedTime = elapsed,
            learningMaterial = learningMaterial
        )
    }

    private fun showNextQuestion() {
        viewModelScope.launch {
            // TODO: find solution with loading state and loading icon
            val state = this@QuizViewModel.uiState as QuizUiState

            val question = repository.getNextQuestion(state.learningMaterial.id, DEFAULT_PRIORITY==null) // look for question with priority == null if default priority is null

            val answers = repository.getAnswersForQuestion(question.id)
            val questionCount = repository.getQuestionCountForLearningMaterial(state.learningMaterial.id)

            // TODO: use from function signature with questionId as a parameter
            val newState = QuizUiState.from(
                state.learningMaterial,
                questionCount,
                question,
                answers
            )

            _uiState.value = newState
            startTimer(newState.startedAt)
        }

    }

    private fun calculatePriority(question: Question): Double {
        val priority: Double = A * (question.trialsSinceLastPresented - ENFORCED_DELAY) * (B * (1-question.accuracy) * ln(question.rt!! / R) + question.accuracy * W)
        Log.d("PriorityCalculation", "priority for question " + question.id + ": " + question.question)
        Log.d("PriorityCalculation", priority.toString() + " = " + A.toString() + " * ( " + question.trialsSinceLastPresented + " - " + ENFORCED_DELAY + " ) * ( " + B + " * " + " ( 1 - " + question.accuracy + " ) * ln( " + question.rt + " / " + R + " ) + " + question.accuracy + " * " + W + " )")

        // alternative priority function with differing log function:
//        val priority: Double = A * (question.trialsSinceLastPresented - ENFORCED_DELAY) * (B * (1-question.accuracy) * log(question.rt!!, R) + question.accuracy * W)
//        Log.d("PriorityCalculation", "priority for question " + question.id + ": " + question.question)
//        Log.d("PriorityCalculation", priority.toString() + " = " + A.toString() + " * ( " + question.trialsSinceLastPresented + " - " + ENFORCED_DELAY + " ) * ( " + B + " * " + " ( 1 - " + question.accuracy + " ) * log( " + question.rt + " , " + R + " ) + " + question.accuracy + " * " + W + " )")

        return priority
    }
}