package de.raum7.local_llm_learning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import de.raum7.local_llm_learning.ui.AppNavHost
import de.raum7.local_llm_learning.ui.theme.AppTheme
import de.raum7.local_llm_learning.data.database.Database

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // instantiate database
        val db = Room.databaseBuilder(applicationContext,
            Database::class.java, "Database"
        ).build()
        val questionDao = db.questionDao()
        val answerDao = db.answerDao()
        val learningMaterialDao = db.learningMaterialDao()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                AppNavHost(questionDao, answerDao, learningMaterialDao)
            }
        }
    }
}