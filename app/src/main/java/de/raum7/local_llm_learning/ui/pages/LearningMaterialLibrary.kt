package de.raum7.local_llm_learning.ui.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.ui.components.AppBar
import de.raum7.local_llm_learning.ui.components.CreateLearningMaterialEFAB
import de.raum7.local_llm_learning.ui.components.EmptyPlaceholder
import de.raum7.local_llm_learning.ui.components.LearningMaterialCardList
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun LearningMaterialLibrary(
    learningMaterials: Array<LearningMaterial>,
    onCreateButtonClick: () -> Unit,
    onCardClick: () -> Unit,
) {
    Scaffold(
        topBar = { AppBar(title = stringResource(R.string.lml)) },
        floatingActionButton = { CreateLearningMaterialEFAB(onCreateButtonClick) },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        if (learningMaterials.isNotEmpty()) {
            LearningMaterialCardList(learningMaterials, onCardClick, padding)
        } else {
            EmptyPlaceholder(stringResource(R.string.lml_no_material), padding)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LearningMaterialLibraryPreview() {
    AppTheme {
        LearningMaterialLibrary(
            MOCK_LEARNING_MATERIALS,
            onCreateButtonClick = {},
            onCardClick = {}
        )
    }
}