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
    var answers: List<Answer>,
    val selectedAnswer: Answer?,
    val result: QuizResult?,
    val totalQuestions: Int,
    val startedAt: Long,
    val elapsedTime: Long,
) : BaseUiState() {
    companion object {
        fun from(
            learningMaterial: LearningMaterial,
            questionCount: Int,
            question: Question,
            answers: List<Answer>,
            startedAt: Long = System.nanoTime()
        ): QuizUiState {

            val shuffledAnswers = answers.shuffled()

            return QuizUiState(
                phase = QuizPhase.ANSWERING,
                question = question,
                answers = shuffledAnswers,
                selectedAnswer = null,
                result = null,
                totalQuestions = questionCount,
                startedAt,
                elapsedTime = System.nanoTime() - startedAt,
            )
        }

        // TODO: remove later
//        fun from(
//            learningMaterial: LearningMaterial,
//            questionIndex: Int = 0,
//            startedAt: Long = System.nanoTime()
//        ): QuizUiState {
//
//            val question = learningMaterial.questions[questionIndex]
//
//            val questionWithShuffledAnswers = question.copy(
//                answers = question.answers.shuffled()
//            )
//
//            return QuizUiState(
//                phase = QuizPhase.ANSWERING,
//                question = questionWithShuffledAnswers,
//                selectedAnswer = null,
//                result = null,
//                totalQuestions = learningMaterial.questions.size,
//                startedAt,
//                elapsedTime = System.nanoTime() - startedAt,
//            )
//        }
    }
}