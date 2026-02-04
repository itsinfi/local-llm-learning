package de.raum7.local_llm_learning.data.models

import de.raum7.local_llm_learning.data.base.BaseModel

data class Question (
    val id: String,
    val question: String,
    val answers: List<Answer>,
): BaseModel() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Question
        return id == other.id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + question.hashCode()
        result = 31 * result + answers.toTypedArray().contentHashCode()
        return result
    }
}
