package de.raum7.local_llm_learning.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.raum7.local_llm_learning.data.base.BaseModel

@Entity(tableName = "LearningMaterial")
data class LearningMaterial (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    //val questions: List<Question>,
    val progress: Double,
) : BaseModel() {
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as LearningMaterial
//        return id == other.id
//    }
//
//    override fun hashCode(): Int {
//        var result = progress.hashCode()
//        result = 31 * result + id.hashCode()
//        result = 31 * result + title.hashCode()
//        result = 31 * result + questions.toTypedArray().contentHashCode()
//        return result
//    }
}
