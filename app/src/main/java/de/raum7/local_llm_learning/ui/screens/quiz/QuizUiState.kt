package de.raum7.local_llm_learning.ui.screens.quiz

import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.data.models.Question
import de.raum7.local_llm_learning.data.models.QuizResult
import de.raum7.local_llm_learning.data.base.BaseUiState

data class QuizUiState(
    val phase: QuizPhase,
    val question: Question,
    val selectedAnswer: Answer?,
    val result: QuizResult?,
    val questionIndex: Int,
    val totalQuestions: Int,
    val startedAt: Long,
) : BaseUiState() {
    companion object {
        fun from(
            learningMaterial: LearningMaterial,
            questionIndex: Int = 0,
            startedAt: Long = System.nanoTime()
        ): QuizUiState {
            return QuizUiState(
                phase = QuizPhase.ANSWERING,
                question = learningMaterial.questions[questionIndex],
                selectedAnswer = null,
                result = null,
                questionIndex,
                totalQuestions = learningMaterial.questions.size,
                startedAt,
            )
        }
    }
}