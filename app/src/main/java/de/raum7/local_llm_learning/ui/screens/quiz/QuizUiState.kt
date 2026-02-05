package de.raum7.local_llm_learning.ui.screens.quiz

import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.data.models.Question
import de.raum7.local_llm_learning.data.models.QuizResult
import de.raum7.local_llm_learning.data.base.BaseUiState
import de.raum7.local_llm_learning.ui.screens.quiz.types.QuizPhase

data class QuizUiState(
    val phase: QuizPhase,
    val question: Question,
    val selectedAnswer: Answer?,
    val result: QuizResult?,
    val totalQuestions: Int,
    val startedAt: Long,
    val elapsedTime: Long,
) : BaseUiState() {
    companion object {
        fun from(
            learningMaterial: LearningMaterial,
            questionId: String?,
            startedAt: Long = System.nanoTime()
        ): QuizUiState {

            val question = when(questionId) {
                null -> learningMaterial.questions[0]
                else -> learningMaterial.questions.firstOrNull { it.id == questionId }
                    ?: error("No question found with id $questionId")
            }

            val questionWithShuffledAnswers = question.copy(
                answers = question.answers.shuffled()
            )

            return QuizUiState(
                phase = QuizPhase.ANSWERING,
                question = questionWithShuffledAnswers,
                selectedAnswer = null,
                result = null,
                totalQuestions = learningMaterial.questions.size,
                startedAt,
                elapsedTime = System.nanoTime() - startedAt,
            )
        }

        // TODO: remove later
        fun from(
            learningMaterial: LearningMaterial,
            questionIndex: Int = 0,
            startedAt: Long = System.nanoTime()
        ): QuizUiState {

            val question = learningMaterial.questions[questionIndex]

            val questionWithShuffledAnswers = question.copy(
                answers = question.answers.shuffled()
            )

            return QuizUiState(
                phase = QuizPhase.ANSWERING,
                question = questionWithShuffledAnswers,
                selectedAnswer = null,
                result = null,
                totalQuestions = learningMaterial.questions.size,
                startedAt,
                elapsedTime = System.nanoTime() - startedAt,
            )
        }
    }
}