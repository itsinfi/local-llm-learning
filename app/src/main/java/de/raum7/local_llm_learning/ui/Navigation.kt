package de.raum7.local_llm_learning.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.raum7.local_llm_learning.ui.screens.assistant.AssistantRoute
import de.raum7.local_llm_learning.ui.screens.assistant.AssistantScreen
import de.raum7.local_llm_learning.ui.screens.library.LibraryRoute
import de.raum7.local_llm_learning.ui.screens.quiz.QuizRoute

object Routes {
    const val LIBRARY = "library"

    const val ASSISTANT = "assistant"

    const val QUIZ = "quiz/{learningMaterialId}"
    fun quiz(learningMaterialId: String): String = "quiz/$learningMaterialId"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LIBRARY,
    ) {

        // Library Screen Route
        composable(Routes.LIBRARY) {
            LibraryRoute(
                navigateToAssistantCallback = { navController.navigate(Routes.ASSISTANT) },
                navigateToQuizCallback = { learningMaterialId: String -> navController.navigate("quiz/$learningMaterialId") },
            )
        }

        // Assistant Screen Route
        composable(Routes.ASSISTANT) {
            AssistantRoute()
        }

        // Quiz Screen Route
        composable(
            Routes.QUIZ,
            listOf(navArgument("learningMaterialId") { type = NavType.StringType })
        ) { backStackEntry ->
            val learningMaterialId = backStackEntry.arguments?.getString("learningMaterialId")
                ?: error("Missing learningMaterialId")

            QuizRoute(learningMaterialId)
        }

        // TODO: Edit Question Screen Route
    }
}