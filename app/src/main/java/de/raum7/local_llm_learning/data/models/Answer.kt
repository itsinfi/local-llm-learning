package de.raum7.local_llm_learning.data.models

import de.raum7.local_llm_learning.data.base.BaseModel

data class Answer (
    val id: String,
    val answer: String,
    val isCorrect: Boolean,
) : BaseModel() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Answer
        return id == other.id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + answer.hashCode()
        result = 31 * result + isCorrect.hashCode()
        return result
    }
}
