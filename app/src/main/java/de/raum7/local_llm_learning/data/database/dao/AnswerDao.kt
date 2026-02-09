package de.raum7.local_llm_learning.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.raum7.local_llm_learning.data.models.Answer
import kotlinx.coroutines.flow.Flow

@Dao
interface AnswerDao {

    @Query("SELECT * FROM answer WHERE questionId = :questionId")
    suspend fun getAnswersForQuestion(questionId: String): Flow<List<Answer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAnswers(answers: List<Answer>)
}