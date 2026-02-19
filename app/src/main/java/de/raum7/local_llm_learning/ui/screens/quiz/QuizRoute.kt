package de.raum7.local_llm_learning.ui.screens.quiz

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import de.raum7.local_llm_learning.data.database.dao.AnswerDao
import de.raum7.local_llm_learning.data.database.dao.LearningMaterialDao
import de.raum7.local_llm_learning.data.database.dao.QuestionDao

//import de.raum7.local_llm_learning.data.store.LearningMaterialStore
//import de.raum7.local_llm_learning.ui.shared.components.EmptyPlaceholder

@Composable
fun QuizRoute(
    learningMaterialId: Int,
    navigateToEditQuestionCallback: (Int, Int) -> Unit,
    questionDao: QuestionDao,
    answerDao: AnswerDao,
    learningMaterialDao: LearningMaterialDao,
    navigateToLibraryCallback: () -> Unit,
) {

    val viewModel: QuizViewModel = viewModel(
        factory = QuizViewModelFactory(
            learningMaterialId = learningMaterialId,
            repository = QuizRepository(questionDao, answerDao, learningMaterialDao),
            navigateToLibraryCallback = navigateToLibraryCallback,
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
