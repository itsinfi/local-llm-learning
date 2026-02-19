package de.raum7.local_llm_learning.ui.screens.quiz

import android.util.Log
import androidx.lifecycle.viewModelScope
import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.LearningMaterial
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
import de.raum7.local_llm_learning.data.spaced_repitition.MAX_RESPONSE_TIME
import de.raum7.local_llm_learning.data.spaced_repitition.R
import de.raum7.local_llm_learning.data.spaced_repitition.TARGET_STREAK
import de.raum7.local_llm_learning.data.spaced_repitition.W
import kotlin.math.ln

class QuizViewModel(
    learningMaterialId: Int,
    private val repository: QuizRepository,
    private val navigateToLibraryCallback: () -> Unit,
) : BaseViewModel(repository) {

    init {
        runBlocking {
            val progress = repository.getProgress(learningMaterialId)
            // reset everything if progress has reached 100%
            if (progress == 1.0) {
                resetLearningMaterial(learningMaterialId)
            }
            val learningMaterial = this@QuizViewModel.repository.getLearningMaterialById(learningMaterialId)
            val question = repository.getNextQuestion(learningMaterial.id, DEFAULT_PRIORITY==null) // look for question with priority == null if default priority is null
            val answers = repository.getAnswersForQuestion(question!!.id)
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

        val previousElapsed = if (question.rt == null) {
            elapsed
        } else {
            question.rt!! * 1_000_000_000.0
        }

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
            previousNanoSeconds = previousElapsed.toLong(),
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
        if (question.rt!! <= MAX_RESPONSE_TIME && question.accuracy == 0) {
            if (question.streak < 3) {
                question.streak++
            }

        } else {
            question.streak = 0
        }
        // force max streak if response time is smaller than weighting constant r which would result in the question not showing up anymore anyway
        if (question.rt!! < R && question.accuracy == 0) {
            question.streak = TARGET_STREAK
        }

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


            learningMaterial.progress = repository.calculateMaterialProgress(learningMaterial.id)
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
            if (question == null) {
                // head to library
                navigateToLibraryCallback()
            } else {
                // start next question as usual
                val answers = repository.getAnswersForQuestion(question!!.id)
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

    }

    private fun calculatePriority(question: Question): Double {
        val priority: Double = A * (question.trialsSinceLastPresented - ENFORCED_DELAY) * (B * (1-question.accuracy) * ln(question.rt!! / R) + question.accuracy * W)
        Log.d("PriorityCalculation", "priority p and streak s for question " + question.id + ": " + question.question)
        Log.d("PriorityCalculation", "p: " + priority.toString() + " = " + A.toString() + " * ( " + question.trialsSinceLastPresented + " - " + ENFORCED_DELAY + " ) * ( " + B + " * " + " ( 1 - " + question.accuracy + " ) * ln( " + question.rt + " / " + R + " ) + " + question.accuracy + " * " + W + " )")
        Log.d("PriorityCalculation", "s: " + question.streak)
        // alternative priority function with differing log function:
//        val priority: Double = A * (question.trialsSinceLastPresented - ENFORCED_DELAY) * (B * (1-question.accuracy) * log(question.rt!!, R) + question.accuracy * W)
//        Log.d("PriorityCalculation", "priority for question " + question.id + ": " + question.question)
//        Log.d("PriorityCalculation", priority.toString() + " = " + A.toString() + " * ( " + question.trialsSinceLastPresented + " - " + ENFORCED_DELAY + " ) * ( " + B + " * " + " ( 1 - " + question.accuracy + " ) * log( " + question.rt + " , " + R + " ) + " + question.accuracy + " * " + W + " )")

        return priority
    }

    private fun resetLearningMaterial(learningMaterialId: Int) {
        runBlocking {
            val oldLearningMaterial = repository.getLearningMaterialById(learningMaterialId)
            val newLearningMaterial = LearningMaterial(
                id = oldLearningMaterial.id,
                title = oldLearningMaterial.title
            )
            // reset learningMaterial
            repository.updateLearningMaterial(newLearningMaterial)
            // get all questions for learningMaterial
            val questions = repository.getQuestionsForLearningMaterial(newLearningMaterial.id)
            questions.forEach {
                val resetQuestion = Question(
                    id = it.id,
                    learningMaterialId = it.learningMaterialId,
                    question = it.question,
                )
                // reset question
                repository.updateQuestion(resetQuestion)
            }
        }
    }
}