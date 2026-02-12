package de.raum7.local_llm_learning.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.raum7.local_llm_learning.data.models.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    @Query("SELECT * FROM question WHERE learningMaterialId = :learningMaterialId")
    suspend fun getQuestionsForLearningMaterial(learningMaterialId: Int): List<Question>

    @Query("SELECT * FROM question WHERE id = :id")
    suspend fun getQuestionById(id: Int): Question

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertQuestion(question: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertQuestions(questions: List<Question>)

    @Query("SELECT COUNT(id) from question WHERE learningMaterialId = :learningMaterialId")
    suspend fun getQuestionCountForLearningMaterial(learningMaterialId: Int): Int

    @Query("SELECT * FROM question WHERE id > :id AND learningMaterialId = :learningMaterialId ORDER BY id asc LIMIT 1")
    suspend fun getNextQuestionById(id: Int, learningMaterialId: Int): Question
}