package de.raum7.local_llm_learning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import de.raum7.local_llm_learning.data.store.LearningMaterialStore
import de.raum7.local_llm_learning.llm.LlmGenerationService
import de.raum7.local_llm_learning.ui.AppNavHost
import de.raum7.local_llm_learning.ui.Routes
import de.raum7.local_llm_learning.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Wichtig: Store laden, bevor Compose startet
        LearningMaterialStore.init(applicationContext)

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
                AppNavHost(startDestination = startDestination)
            }
        }
    }
}
