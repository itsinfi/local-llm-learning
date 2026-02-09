package de.raum7.local_llm_learning.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import de.raum7.local_llm_learning.data.database.dao.AnswerDao
import de.raum7.local_llm_learning.data.database.dao.LearningMaterialDao
import de.raum7.local_llm_learning.data.database.dao.QuestionDao
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.data.models.Question

@Database(
    entities = [
        LearningMaterial::class,
        Question::class,
        Answer::class,
    ],
    version = 1,
)
abstract class Database : RoomDatabase() {
    abstract fun learningMaterialDao(): LearningMaterialDao
    abstract fun questionDao(): QuestionDao
    abstract fun answerDao(): AnswerDao
}