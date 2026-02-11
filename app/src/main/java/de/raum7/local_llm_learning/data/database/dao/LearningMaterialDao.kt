package de.raum7.local_llm_learning.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.raum7.local_llm_learning.data.models.LearningMaterial
import kotlinx.coroutines.flow.Flow

@Dao
interface LearningMaterialDao {

    @Query("SELECT * FROM learningmaterial")
    suspend fun getAllMaterials(): Flow<List<LearningMaterial>>

    @Query("SELECT * FROM learningmaterial WHERE id = :id")
    suspend fun getMaterialById(id: Int): LearningMaterial?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMaterial(learningMaterial: LearningMaterial)

}