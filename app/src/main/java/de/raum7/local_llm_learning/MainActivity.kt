package de.raum7.local_llm_learning

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import de.raum7.local_llm_learning.llm.LlmGenerationService
import androidx.room.Room
import de.raum7.local_llm_learning.ui.AppNavHost
import de.raum7.local_llm_learning.ui.Routes
import de.raum7.local_llm_learning.ui.theme.AppTheme
import de.raum7.local_llm_learning.data.database.Database
import de.raum7.local_llm_learning.data.mock.MOCK_ANSWERS
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.mock.MOCK_QUESTIONS
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // instantiate database
        val db = Room.databaseBuilder(applicationContext,
            Database::class.java, "Database"
        ).build()
        val questionDao = db.questionDao()
        val answerDao = db.answerDao()
        val learningMaterialDao = db.learningMaterialDao()

        //TODO remove temp mock data
        val TAG ="log"
        runBlocking {
            MOCK_LEARNING_MATERIALS.forEach { learningMaterialDao.insertMaterial(it) }
            MOCK_QUESTIONS.forEach { questionDao.insertQuestion(it) }
            answerDao.insertAnswers(MOCK_ANSWERS)
            Log.d(TAG, "Questions:" + questionDao.getAllQuestions())
            Log.d(TAG, "Answers:" + answerDao.getAllAnswers())
        }

        enableEdgeToEdge()

        val startDestination =
            if (
                intent?.hasExtra(LlmGenerationService.EXTRA_RESULT_TEXT) == true ||
                intent?.hasExtra(LlmGenerationService.EXTRA_RESULT_ERROR) == true
            ) {
                Routes.ASSISTANT_RESULT
            } else {
                Routes.LIBRARY
            }

        setContent {
            AppTheme {
                AppNavHost(startDestination = startDestination, questionDao, answerDao, learningMaterialDao)
            }
        }
    }
}