package de.raum7.local_llm_learning.ui.screens.library

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.ui.shared.components.AppBar
import de.raum7.local_llm_learning.ui.screens.library.components.CreateLearningMaterialEFAB
import de.raum7.local_llm_learning.ui.shared.components.EmptyPlaceholder
import de.raum7.local_llm_learning.ui.screens.library.components.LearningMaterialCardList
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun LibraryScreen(
    uiState: LibraryUiState,
    onCreateButtonClick: () -> Unit,
    onCardClick: (LearningMaterial) -> Unit,
) {
    Scaffold(
        topBar = { AppBar(title = stringResource(R.string.lml)) },
        floatingActionButton = { CreateLearningMaterialEFAB(onCreateButtonClick) },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        if (uiState.learningMaterials.isNotEmpty()) {
            LearningMaterialCardList(uiState.learningMaterials, onCardClick, padding)
        } else {
            EmptyPlaceholder(stringResource(R.string.lml_no_material), padding)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LibraryScreenPreview_NotEmpty() {
    AppTheme {
        LibraryScreen(
            LibraryUiState(MOCK_LEARNING_MATERIALS),
            onCreateButtonClick = {},
            onCardClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LibraryScreenPreview_Empty() {
    AppTheme {
        LibraryScreen(
            LibraryUiState(emptyList()),
            onCreateButtonClick = {},
            onCardClick = {}
        )
    }
}