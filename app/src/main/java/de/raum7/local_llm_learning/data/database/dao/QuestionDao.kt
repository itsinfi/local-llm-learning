package de.raum7.local_llm_learning.data.database.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.raum7.local_llm_learning.data.models.Question
import de.raum7.local_llm_learning.data.spaced_repitition.MAX_RESPONSE_TIME
import de.raum7.local_llm_learning.data.spaced_repitition.TARGET_STREAK

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

    @Query("SELECT COUNT(id) from question WHERE learningMaterialId = :learningMaterialId AND accuracy = 0 AND rt <= $MAX_RESPONSE_TIME")
    suspend fun getMasteredQuestionCount(learningMaterialId: Int): Int

    @Query("SELECT SUM(streak) from question WHERE learningMaterialId = :learningMaterialId AND streak > 0")
    suspend fun getCurrentProgression(learningMaterialId: Int): Int

    @Query("SELECT * FROM question")
    suspend fun getAllQuestions(): List<Question>

    @Update
    suspend fun updateQuestion(question: Question)

    @Update
    suspend fun updateQuestions(questions: List<Question>)

    @Query("SELECT * FROM question WHERE learningMaterialId = :learningMaterialId AND streak < $TARGET_STREAK ORDER BY priority desc LIMIT 1")
    suspend fun getHighestPriorityQuestion(learningMaterialId: Int): Question?

    @Query("SELECT * FROM question WHERE learningMaterialId = :learningMaterialId and priority IS null LIMIT 1")
    suspend fun getMissingPriorityQuestion(learningMaterialId: Int): Question?

    @Query("SELECT * FROM question WHERE learningMaterialId = :learningMaterialId AND rt NOT null")
    suspend fun getAnsweredQuestions(learningMaterialId: Int): List<Question>

    suspend fun calculateMaterialProgress(learningMaterialId: Int): Double {
        val targetProgression = this.getQuestionCountForLearningMaterial(learningMaterialId) * TARGET_STREAK
        val currentProgression = this.getCurrentProgression(learningMaterialId)
        val learningMaterialProgress = currentProgression.toDouble() / targetProgression.toDouble()
        Log.d("ProgressionCalculation", "Progress: " + learningMaterialProgress + " = " + targetProgression + " / " + currentProgression)
        return learningMaterialProgress
    }
}