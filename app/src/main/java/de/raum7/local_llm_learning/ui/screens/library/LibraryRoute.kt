package de.raum7.local_llm_learning.ui.screens.library

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import de.raum7.local_llm_learning.data.database.dao.LearningMaterialDao
import de.raum7.local_llm_learning.data.database.dao.QuestionDao

@Composable
fun LibraryRoute(
    navigateToAssistantCallback: () -> Unit,
    navigateToQuizCallback: (Int) -> Unit,
    learningMaterialDao: LearningMaterialDao,
    questionDao: QuestionDao,
) {
    val viewModel: LibraryViewModel = viewModel(
        factory = LibraryViewModelFactory(
            navigateToAssistantCallback,
            navigateToQuizCallback,
            repository = LibraryRepository(learningMaterialDao, questionDao),
        )
    )

    LibraryScreen(
        uiState = viewModel.uiState as LibraryUiState,
        onCreateButtonClick = viewModel::onCreateButtonClick,
        onCardClick = viewModel::onCardClick,
    )
}