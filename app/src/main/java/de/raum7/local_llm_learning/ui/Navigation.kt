package de.raum7.local_llm_learning.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.raum7.local_llm_learning.data.database.dao.AnswerDao
import de.raum7.local_llm_learning.data.database.dao.LearningMaterialDao
import de.raum7.local_llm_learning.data.database.dao.QuestionDao
import de.raum7.local_llm_learning.ui.screens.assistant.AssistantRoute
import de.raum7.local_llm_learning.ui.screens.edit_question.EditQuestionRoute
import de.raum7.local_llm_learning.ui.screens.library.LibraryRoute
import de.raum7.local_llm_learning.ui.screens.quiz.QuizRoute

object Routes {
    const val LIBRARY = "library"

    const val ASSISTANT = "assistant"

    const val QUIZ = "quiz/{learningMaterialId}"
    fun quiz(learningMaterialId: Int): String = "quiz/$learningMaterialId"

    const val EDIT_QUESTION = "editQuestion/{learningMaterialId}/{questionId}"
    fun editQuestion(learningMaterialId: Int, questionId: Int): String = "editQuestion/$learningMaterialId/$questionId"
}

@Composable
fun AppNavHost(questionDao: QuestionDao, answerDao: AnswerDao, learningMaterialDao: LearningMaterialDao) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LIBRARY,
    ) {

        // Library Screen Route
        composable(Routes.LIBRARY) {
            LibraryRoute(
                navigateToAssistantCallback = { navController.navigate(Routes.ASSISTANT) },
                navigateToQuizCallback = { learningMaterialId: Int -> navController.navigate("quiz/$learningMaterialId") },
                learningMaterialDao = learningMaterialDao,
                questionDao = questionDao,
            )
        }

        // Assistant Screen Route
        composable(Routes.ASSISTANT) {
            AssistantRoute()
        }

        // Quiz Screen Route
        composable(
            Routes.QUIZ,
            listOf(navArgument("learningMaterialId") { type = NavType.IntType })
        ) { backStackEntry ->
            val learningMaterialId = backStackEntry.arguments?.getInt("learningMaterialId")
                ?: error("Missing learningMaterialId")

            QuizRoute(
                learningMaterialId, // TODO: fix to integer
                navigateToEditQuestionCallback = {
                        learningMaterialId: Int, questionId: Int ->
                    navController.navigate(Routes.editQuestion(learningMaterialId, questionId))
                },
                questionDao,
                answerDao,
                learningMaterialDao,
            )
        }

        // Edit Question Screen Route
        composable(
            Routes.EDIT_QUESTION,
            listOf(
                navArgument("learningMaterialId") { type = NavType.IntType },
                navArgument("questionId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val learningMaterialId = backStackEntry.arguments?.getInt("learningMaterialId")
                ?: error("Missing learningMaterialId")
            val questionId = backStackEntry.arguments?.getInt("questionId")
                ?: error("Missing questionId")

            EditQuestionRoute(learningMaterialId, questionId, questionDao, answerDao)
        }
    }
}