package de.raum7.local_llm_learning.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.ui.pages.LearningMaterialCreationAssistant
import de.raum7.local_llm_learning.ui.pages.LearningMaterialLibrary
import de.raum7.local_llm_learning.ui.pages.SpacedRepetitionAlgorithmSingleChoiceQuestionBasedQuiz
import de.raum7.local_llm_learning.ui.screens.assistant.AssistantScreen

object Routes {
    const val LML = "LML"
    const val LMCA = "LMCA"
    const val QUIZ = "QUIZ"
    const val ASSISTANT = "ASSISTANT"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LML,
    ) {
        composable(Routes.LML) {
            LearningMaterialLibrary(
                learningMaterials = MOCK_LEARNING_MATERIALS,
                onCreateButtonClick = { navController.navigate(Routes.LMCA) },
                onCardClick = { navController.navigate(Routes.ASSISTANT) }
            )
        }

        composable(Routes.LMCA) { LearningMaterialCreationAssistant() }

        composable(Routes.ASSISTANT) { AssistantScreen() }

        composable(Routes.QUIZ) { SpacedRepetitionAlgorithmSingleChoiceQuestionBasedQuiz() }
    }
}
