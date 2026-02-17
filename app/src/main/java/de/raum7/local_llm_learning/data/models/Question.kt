package de.raum7.local_llm_learning.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import de.raum7.local_llm_learning.data.base.BaseModel
import de.raum7.local_llm_learning.data.spaced_repitition.DEFAULT_PRIORITY

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
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var learningMaterialId: Int,
    val question: String,
    var priority: Double? = DEFAULT_PRIORITY,
    var trialsSinceLastPresented: Int = 0, // parameter N, saves the last trial this question was presented
    var accuracy: Int = 1, // used in determining priority, accuracy of user answer from last presentation, must be set to 0 when answered correctly, otherwise 1
    var rt: Double? = null, // response time of user for answering this question in seconds

): BaseModel() {

}
