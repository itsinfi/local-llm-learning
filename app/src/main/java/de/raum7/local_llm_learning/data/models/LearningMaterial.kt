package de.raum7.local_llm_learning.data.models

data class LearningMaterial (
    val id: String,
    val title: String,
    val questions: Array<Question>,
    val progress: Double,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LearningMaterial

        if (progress != other.progress) return false
        if (id != other.id) return false
        if (title != other.title) return false
        if (!questions.contentEquals(other.questions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = progress.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + questions.contentHashCode()
        return result
    }
}
