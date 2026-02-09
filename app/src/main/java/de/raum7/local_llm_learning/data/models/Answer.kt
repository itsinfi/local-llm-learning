package de.raum7.local_llm_learning.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import de.raum7.local_llm_learning.data.base.BaseModel

@Entity(
    tableName = "answer",
    foreignKeys = [
        ForeignKey(
            entity = Question::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("questionId")]
)
data class Answer (
    @PrimaryKey
    val id: String,
    val questionId: String,
    val answer: String,
    val isCorrect: Boolean,
) : BaseModel() {
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as Answer
//        return id == other.id
//    }
//
//    override fun hashCode(): Int {
//        var result = id.hashCode()
//        result = 31 * result + answer.hashCode()
//        result = 31 * result + isCorrect.hashCode()
//        return result
//    }
}
