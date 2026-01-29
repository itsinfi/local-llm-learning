package de.raum7.local_llm_learning.ui.screens.library.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.ui.theme.AppTheme
import kotlin.math.roundToInt

@Composable
fun LearningMaterialCard(learningMaterial: LearningMaterial, onClick: () -> Unit) {
    Card (
        onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
    ) {
        LearningMaterialCardContent(learningMaterial)
    }
}

@Composable
private fun LearningMaterialCardContent(learningMaterial: LearningMaterial) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(16.dp),
    ) {
        Text(
            text = learningMaterial.title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        LearningMaterialDetailInfo(learningMaterial)
    }
}

@Composable
private fun LearningMaterialDetailInfo(learningMaterial: LearningMaterial) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "${learningMaterial.questions.size} ${stringResource(R.string.library_questions)}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75.toFloat()),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text ="${(learningMaterial.progress * 100).roundToInt()}% ${stringResource(
                R.string.library_progress)}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75.toFloat()),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LearningMaterialCardPreview() {
    AppTheme {
        LearningMaterialCard(
            learningMaterial = MOCK_LEARNING_MATERIALS[1],
            onClick = {},
        )
    }
}