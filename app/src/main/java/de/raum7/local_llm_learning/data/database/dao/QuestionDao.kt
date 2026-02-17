package de.raum7.local_llm_learning.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.raum7.local_llm_learning.data.models.Question

@Dao
interface QuestionDao {

    @Query("SELECT * FROM question WHERE learningMaterialId = :learningMaterialId")
    suspend fun getQuestionsForLearningMaterial(learningMaterialId: Int): List<Question>

    @Query("SELECT * FROM question WHERE id = :id")
    suspend fun getQuestionById(id: Int): Question

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertQuestion(question: Question): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertQuestions(questions: List<Question>): List<Long>

    @Query("SELECT COUNT(id) from question WHERE learningMaterialId = :learningMaterialId")
    suspend fun getQuestionCountForLearningMaterial(learningMaterialId: Int): Int

    @Query("SELECT * FROM question WHERE id > :id AND learningMaterialId = :learningMaterialId ORDER BY id asc LIMIT 1")
    suspend fun getNextQuestionById(id: Int, learningMaterialId: Int): Question

    @Query("SELECT * FROM question")
    suspend fun getAllQuestions(): List<Question>

    @Update
    suspend fun updateQuestion(question: Question)

    @Update
    suspend fun updateQuestions(questions: List<Question>)

    @Query("SELECT * FROM question WHERE learningMaterialId = :learningMaterialId ORDER BY priority desc LIMIT 1")
    suspend fun getHighestPriorityQuestion(learningMaterialId: Int): Question

    @Query("SELECT * FROM question WHERE id != :id AND learningMaterialId = :learningMaterialId ORDER BY priority desc LIMIT 1")
    suspend fun getNextHighestPriorityQuestion(id: Int, learningMaterialId: Int): Question

    @Query("SELECT * FROM question WHERE learningMaterialId = :learningMaterialId and priority IS null LIMIT 1")
    suspend fun getMissingPriorityQuestion(learningMaterialId: Int): Question?

    @Query("SELECT * FROM question WHERE id != :id AND learningMaterialId = :learningMaterialId and priority IS null LIMIT 1")
    suspend fun getNextMissingPriorityQuestion(id: Int, learningMaterialId: Int): Question?

    @Query("SELECT * FROM question WHERE learningMaterialId = :learningMaterialId AND rt NOT null")
    suspend fun getAnsweredQuestions(learningMaterialId: Int): List<Question>
}