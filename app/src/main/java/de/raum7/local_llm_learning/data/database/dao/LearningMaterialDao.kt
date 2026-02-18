package de.raum7.local_llm_learning.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.raum7.local_llm_learning.data.models.LearningMaterial

@Dao
interface LearningMaterialDao {

    @Query("SELECT * FROM learningmaterial")
    suspend fun getAllMaterials(): List<LearningMaterial>

    @Query("SELECT progress from learningmaterial where id = :id")
    suspend fun getProgress(id: Int): Double

    @Query("SELECT * FROM learningmaterial WHERE id = :id")
    suspend fun getMaterialById(id: Int): LearningMaterial

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMaterial(learningMaterial: LearningMaterial): Long

    @Update
    suspend fun updateLearningMaterial(learningMaterial: LearningMaterial)

}