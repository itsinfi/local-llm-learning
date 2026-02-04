package de.raum7.local_llm_learning.ui.screens.library

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS

@Composable
fun LibraryRoute(
    navigateToAssistantCallback: () -> Unit,
    navigateToQuizCallback: (String) -> Unit,
) {
    val data = MOCK_LEARNING_MATERIALS

    val viewModel: LibraryViewModel = viewModel(
        factory = LibraryViewModelFactory(
            navigateToAssistantCallback,
            navigateToQuizCallback,
            repository = LibraryRepository(data),
        )
    )

    LibraryScreen(
        uiState = viewModel.uiState as LibraryUiState,
        onCreateButtonClick = viewModel::onCreateButtonClick,
        onCardClick = viewModel::onCardClick,
    )
}