package de.raum7.local_llm_learning.data.models

data class Question (
    val id: String,
    val question: String,
    val answers: Array<Answer>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Question
        return id == other.id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + question.hashCode()
        result = 31 * result + answers.contentHashCode()
        return result
    }
}
