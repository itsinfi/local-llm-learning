package de.raum7.local_llm_learning.data.models

import de.raum7.local_llm_learning.data.base.BaseModel

data class QuizResult(
    val id: String,
    val question: Question,
    val isCorrect: Boolean,
    val selectedAnswer: Answer,
    val correctAnswer: Answer,
    val elapsedNanoSeconds: Long,
    val previousNanoSeconds: Long,
): BaseModel() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizResult
        return id == other.id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + question.hashCode()
        result = 31 * result + isCorrect.hashCode()
        result = 31 * result + selectedAnswer.hashCode()
        result = 31 * result + correctAnswer.hashCode()
        result = 31 * result + elapsedNanoSeconds.hashCode()
        result = 31 * result + previousNanoSeconds.hashCode()
        return result
    }
}
