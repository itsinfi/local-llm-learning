package de.raum7.local_llm_learning.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.raum7.local_llm_learning.data.parsing.LearningMaterialJsonParser
import de.raum7.local_llm_learning.llm.LlmGenerationService
import de.raum7.local_llm_learning.ui.screens.assistant.AssistantResultScreen
import de.raum7.local_llm_learning.data.database.dao.AnswerDao
import de.raum7.local_llm_learning.data.database.dao.LearningMaterialDao
import de.raum7.local_llm_learning.data.database.dao.QuestionDao
import de.raum7.local_llm_learning.ui.screens.assistant.AssistantRoute
import de.raum7.local_llm_learning.ui.screens.edit_question.EditQuestionRoute
import de.raum7.local_llm_learning.ui.screens.library.LibraryRoute
import de.raum7.local_llm_learning.ui.screens.quiz.QuizRoute
import kotlinx.coroutines.runBlocking

object Routes {
    const val LIBRARY = "library"

    const val ASSISTANT = "assistant"
    const val ASSISTANT_RESULT = "assistant_result"

    const val QUIZ = "quiz/{learningMaterialId}"
    fun quiz(learningMaterialId: Int): String = "quiz/$learningMaterialId"

    const val EDIT_QUESTION = "editQuestion/{learningMaterialId}/{questionId}"
    fun editQuestion(learningMaterialId: Int, questionId: Int): String = "editQuestion/$learningMaterialId/$questionId"
}

@Composable
fun AppNavHost(startDestination: String = Routes.LIBRARY, questionDao: QuestionDao, answerDao: AnswerDao, learningMaterialDao: LearningMaterialDao) {
    val navController = rememberNavController()
    val activity = LocalActivity.current

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        // Library Screen
        composable(Routes.LIBRARY) {
            LibraryRoute(
                navigateToAssistantCallback = { navController.navigate(Routes.ASSISTANT) },
                navigateToQuizCallback = { learningMaterialId: Int -> navController.navigate("quiz/$learningMaterialId") },
                learningMaterialDao = learningMaterialDao,
                questionDao = questionDao,
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
                        // TODO: integrate parser into db
                        runBlocking {
                            val material = LearningMaterialJsonParser.parse(resultText)
                            // write learningMaterial to db and retrieve auto generated id
                            val learningMaterialId = learningMaterialDao.upsertMaterial(material.first)

                            val questionsWithAnswers = material.second

                            questionsWithAnswers.forEach {
                                // set question foreign key
                                it.key.learningMaterialId = learningMaterialId.toInt()
                                // write question to db and retrieve auto generated id
                                val questionId = questionDao.upsertQuestion(it.key).toInt()
                                it.value.forEach { answer ->
                                    // set foreign key
                                    answer.questionId = questionId
                                    // write answer to db
                                    answerDao.upsertAnswer(answer)
                                }
                            }

                        }



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
            listOf(navArgument("learningMaterialId") { type = NavType.IntType })
        ) { backStackEntry ->
            val learningMaterialId = backStackEntry.arguments?.getInt("learningMaterialId")
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
                learningMaterialId,
                navigateToEditQuestionCallback = {
                        learningMaterialId: Int, questionId: Int ->
                    navController.navigate(Routes.editQuestion(learningMaterialId, questionId))
                },
                questionDao,
                answerDao,
                learningMaterialDao,
            )
        }



        // Edit Question Screen
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

            EditQuestionRoute(
                learningMaterialId = learningMaterialId,
                questionId = questionId,
                questionDao = questionDao,
                answerDao = answerDao,
            )
        }
    }
}