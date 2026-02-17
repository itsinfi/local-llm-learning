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
            // TODO: get question by priority
            val learningMaterial = this@QuizViewModel.repository.getLearningMaterialById(learningMaterialId)
            val question = repository.getNextQuestionById(-1, learningMaterial.id)
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

        // calculate priority for current question TODO: calculate priority for all questions
        /*
        steps:
        1. update question specific parameters
        2. increment lastPresented for each question except current one
        3. calculate priority for each question except those not answered yet
         */
        // TODO: remove val trial = state.learningMaterial.currentTrial
        // update question specific parameters
        state.question.accuracy = if(selectedAnswer == correctAnswer) {
            0
        } else {
            1
        }
        state.question.trialsSinceLastPresented = 0
        state.question.rt = elapsed.toDouble() * 0.000000001

        // calculate every priority
        Log.d("DEBUG LOG", "before priority calculation")
        runBlocking {
            Log.d("DEBUG LOG", "during priority calculation")
            repository.updateQuestion(state.question)
            val questions = repository.getAnsweredQuestions(state.learningMaterial.id)
            questions.forEach {
                if (it.id != state.question.id) {
                    it.trialsSinceLastPresented++
                }
                it.priority = calculatePriority(it)

            }
            repository.updateQuestions(questions)
        }
        Log.d("DEBUG LOG", "after priority calculation")
        // TODO remove deprecated code
//        state.question.priority = calculatePriority(question, trial, elapsed.toDouble() * 0.000000001)

//        state.question.trialsSinceLastPresented = trial // not incremented because question was presented in trial that has been passed, not in next trial (trial + 1)
//        state.learningMaterial.currentTrial = trial + 1 // incremented because trial has been passed

        // TODO remove debug log
//        Log.d("DEBUG_LOG", "Question: " + state.question.question + "priority: " + state.question.priority)
        // update learningMaterial (incremented trial) and question (updated priority and lastPresented
//        viewModelScope.launch {
//            repository.updateLearningMaterial(state.learningMaterial)
//            repository.updateQuestion(state.question)
//        }

        _uiState.value = state.copy(
            phase = QuizPhase.RESULTS,
            result = result,
            elapsedTime = elapsed,
//            learningMaterial = state.learningMaterial,
        )
    }

    private fun showNextQuestion() {
        viewModelScope.launch {
            // TODO: find solution with loading state and loading icon
            val state = this@QuizViewModel.uiState as QuizUiState

            // TODO: only temporary code, please add question selection via spaced repetition
//            val question = repository.getNextQuestionById(state.question.id, state.learningMaterial.id)
            // retrieve question with highest priority (or null priority if default priority is set to null)
            val question = when(DEFAULT_PRIORITY) {
                null -> repository.getNextQuestion(state.question.id, state.learningMaterial.id)
                else -> repository.getNextHighestPriorityQuestion(state.question.id, state.learningMaterial.id)
            }

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
//        val n = trial - (question.trialsSinceLastPresented ?: trial) // set n to 0 if question hasn't been presented once
        val priority: Double = A * (question.trialsSinceLastPresented - ENFORCED_DELAY) * (B * (1-question.accuracy) * ln(question.rt!! / R) + question.accuracy * W)
        // TODO remove debug log
        Log.d("DEBUG LOG", "priority for question " + question.id + " " + question.question)
        Log.d("DEBUG LOG", priority.toString() + " = " + A.toString() + " * " + " ( " + question.trialsSinceLastPresented + " - " + ENFORCED_DELAY + " ) * ( " + B + " * " + " ( 1 - " + question.accuracy + " ) * ln( " + question.rt + " / " + R + " )  + " + question.accuracy + " * " + W + " )")

        return priority
    }
}