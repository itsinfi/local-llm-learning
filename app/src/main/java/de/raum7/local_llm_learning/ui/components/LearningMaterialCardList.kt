package de.raum7.local_llm_learning.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun LearningMaterialCardList(
    learningMaterials: Array<LearningMaterial>,
    onCardClick: () -> Unit,
    padding: PaddingValues
) {
    LazyColumn (
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
    ) {
        items(learningMaterials) { learningMaterial ->
            LearningMaterialCard(learningMaterial, onClick = onCardClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LearningMaterialCardListPreview() {
    AppTheme {
        LearningMaterialCardList(
            learningMaterials = MOCK_LEARNING_MATERIALS,
            onCardClick = {},
            padding = PaddingValues.Zero,
        )
    }
}