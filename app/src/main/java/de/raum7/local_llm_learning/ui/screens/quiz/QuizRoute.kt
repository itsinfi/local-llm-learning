package de.raum7.local_llm_learning.ui.screens.quiz

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import de.raum7.local_llm_learning.data.database.dao.AnswerDao
import de.raum7.local_llm_learning.data.database.dao.LearningMaterialDao
import de.raum7.local_llm_learning.data.database.dao.QuestionDao
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS

@Composable
fun QuizRoute(
    learningMaterialId: Int,
    navigateToEditQuestionCallback: (Int, Int) -> Unit,
    questionDao: QuestionDao,
    answerDao: AnswerDao,
    learningMaterialDao: LearningMaterialDao,
) {
    val data = MOCK_LEARNING_MATERIALS

    val viewModel: QuizViewModel = viewModel(
        factory = QuizViewModelFactory(
            learningMaterialId = learningMaterialId,
            repository = QuizRepository(questionDao, answerDao, learningMaterialDao),
        )
    )

    val quizUiState = viewModel.uiState as QuizUiState

    val onEdit = {
        navigateToEditQuestionCallback(learningMaterialId, quizUiState.question.id)
    }

    QuizScreen(
        uiState = viewModel.uiState as QuizUiState,
        onAnswerSelected = viewModel::onAnswerSelected,
        onContinue = viewModel::onContinue,
        onEdit = onEdit
    )
}