package de.raum7.local_llm_learning.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.raum7.local_llm_learning.data.base.BaseModel

@Entity(tableName = "LearningMaterial")
data class LearningMaterial (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val progress: Double,
) : BaseModel() {
}
