package de.raum7.local_llm_learning.ui.screens.quiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import de.raum7.local_llm_learning.data.database.dao.AnswerDao
import de.raum7.local_llm_learning.data.database.dao.LearningMaterialDao
import de.raum7.local_llm_learning.data.database.dao.QuestionDao
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
//import de.raum7.local_llm_learning.data.store.LearningMaterialStore
//import de.raum7.local_llm_learning.ui.shared.components.EmptyPlaceholder

@Composable
fun QuizRoute(
    learningMaterialId: Int,
    navigateToEditQuestionCallback: (Int, Int) -> Unit,
    questionDao: QuestionDao,
    answerDao: AnswerDao,
    learningMaterialDao: LearningMaterialDao,
) {

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
        uiState = quizUiState,
        onAnswerSelected = viewModel::onAnswerSelected,
        onContinue = viewModel::onContinue,
        onEdit = onEdit
    )
}
