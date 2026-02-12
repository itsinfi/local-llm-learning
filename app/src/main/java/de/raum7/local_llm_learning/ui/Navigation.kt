package de.raum7.local_llm_learning.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.raum7.local_llm_learning.data.parsing.LearningMaterialJsonParser
import de.raum7.local_llm_learning.data.store.LearningMaterialStore
import de.raum7.local_llm_learning.llm.LlmGenerationService
import de.raum7.local_llm_learning.ui.screens.assistant.AssistantResultScreen
import de.raum7.local_llm_learning.ui.screens.assistant.AssistantRoute
import de.raum7.local_llm_learning.ui.screens.edit_question.EditQuestionRoute
import de.raum7.local_llm_learning.ui.screens.library.LibraryRoute
import de.raum7.local_llm_learning.ui.screens.quiz.QuizRoute


object Routes {
    const val LIBRARY = "library"

    const val ASSISTANT = "assistant"
    const val ASSISTANT_RESULT = "assistant_result"

    const val QUIZ = "quiz/{learningMaterialId}"
    fun quiz(learningMaterialId: String): String = "quiz/$learningMaterialId"

    const val EDIT_QUESTION = "editQuestion/{learningMaterialId}/{questionId}"
    fun editQuestion(learningMaterialId: String, questionId: String): String =
        "editQuestion/$learningMaterialId/$questionId"
}

@Composable
fun AppNavHost(
    startDestination: String = Routes.LIBRARY
) {
    val navController = rememberNavController()
    val activity = LocalActivity.current

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        // Library Screen
        composable(Routes.LIBRARY) {
            LibraryRoute(
                navigateToAssistantCallback = {
                    navController.navigate(Routes.ASSISTANT)
                },
                navigateToQuizCallback = { learningMaterialId ->
                    navController.navigate(Routes.quiz(learningMaterialId))
                },
            )
        }

        // Assistant Screen
        composable(Routes.ASSISTANT) {
            AssistantRoute(
                navigateToResult = {
                    navController.navigate(Routes.ASSISTANT_RESULT) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Assistant Result Screen
        composable(Routes.ASSISTANT_RESULT) {
            val resultText =
                activity?.intent?.getStringExtra(LlmGenerationService.EXTRA_RESULT_TEXT).orEmpty()

            AssistantResultScreen(
                resultText = resultText,
                onSaveToLibrary = {
                    try {
                        val material = LearningMaterialJsonParser.parse(resultText)
                        LearningMaterialStore.add(material)

                        navController.navigate(Routes.LIBRARY) {
                            popUpTo(Routes.LIBRARY) { inclusive = true }
                        }
                    } catch (_: Throwable) {
                        navController.navigate(Routes.LIBRARY) {
                            popUpTo(Routes.LIBRARY) { inclusive = true }
                        }
                    }
                },
                onBackToLibrary = {
                    navController.navigate(Routes.LIBRARY) {
                        popUpTo(Routes.LIBRARY) { inclusive = true }
                    }
                }
            )
        }

        // Quiz Screen
        composable(
            Routes.QUIZ,
            listOf(navArgument("learningMaterialId") { type = NavType.StringType })
        ) { backStackEntry ->
            val learningMaterialId =
                backStackEntry.arguments?.getString("learningMaterialId")
                    ?: error("Missing learningMaterialId")

            // is this required?
            /*val material = LearningMaterialStore.getById(learningMaterialId)
            if (material == null) {
                navController.navigate(Routes.LIBRARY) {
                    popUpTo(Routes.LIBRARY) { inclusive = true }
                }
                return@composable
            }*/

            QuizRoute(
                learningMaterialId = learningMaterialId,
                navigateToEditQuestionCallback = { lmId, qId ->
                    navController.navigate(Routes.editQuestion(lmId, qId))
                }
            )
        }



        // Edit Question Screen
        composable(
            Routes.EDIT_QUESTION,
            listOf(
                navArgument("learningMaterialId") { type = NavType.StringType },
                navArgument("questionId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val learningMaterialId =
                backStackEntry.arguments?.getString("learningMaterialId")
                    ?: error("Missing learningMaterialId")

            val questionId =
                backStackEntry.arguments?.getString("questionId")
                    ?: error("Missing questionId")

            EditQuestionRoute(
                learningMaterialId = learningMaterialId,
                questionId = questionId
            )
        }
    }
}
