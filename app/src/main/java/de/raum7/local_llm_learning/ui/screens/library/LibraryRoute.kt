package de.raum7.local_llm_learning.ui.screens.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import de.raum7.local_llm_learning.data.store.LearningMaterialStore

@Composable
fun LibraryRoute(
    navigateToAssistantCallback: () -> Unit,
    navigateToQuizCallback: (String) -> Unit,
) {
    val materials by LearningMaterialStore.items.collectAsState()

    LibraryScreen(
        uiState = LibraryUiState.from(materials),
        onCreateButtonClick = navigateToAssistantCallback,
        onCardClick = { learningMaterial ->
            navigateToQuizCallback(learningMaterial.id)
        }
    )
}
