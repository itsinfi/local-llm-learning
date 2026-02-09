package de.raum7.local_llm_learning.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import de.raum7.local_llm_learning.data.base.BaseModel

@Entity(
    tableName = "question",
    foreignKeys = [
        ForeignKey(
            entity = LearningMaterial::class,
            parentColumns = ["id"],
            childColumns = ["learningMaterialId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index("learningMaterialId")]
)
data class Question (
    @PrimaryKey
    val id: String,
    val learningMaterialId: String,
    val question: String,
//    val answers: List<Answer>,
): BaseModel() {
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as Question
//        return id == other.id
//    }
//
//    override fun hashCode(): Int {
//        var result = id.hashCode()
//        result = 31 * result + question.hashCode()
//        result = 31 * result + answers.toTypedArray().contentHashCode()
//        return result
//    }
}
