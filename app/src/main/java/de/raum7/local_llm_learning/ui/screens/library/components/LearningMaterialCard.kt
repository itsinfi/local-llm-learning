package de.raum7.local_llm_learning.ui.screens.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import de.raum7.local_llm_learning.ui.shared.components.BlurredBox
import de.raum7.local_llm_learning.ui.theme.AppTheme
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun LearningMaterialCard(learningMaterial: LearningMaterial, onClick: () -> Unit) {
    Card (
        onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CardDefaults.shape,
            )
    ) {
        BlurredBox(
            backgroundContent = {
                LearningMaterialCardContent(
                    learningMaterial,
                    modifier = Modifier.padding(8.dp)
                )
            },
            foregroundContent = {
                LearningMaterialCardContent(
                    learningMaterial,
                    modifier = Modifier.padding(16.dp)
                )
            },
        )
    }
}

@Composable
private fun LearningMaterialCardContent(
    learningMaterial: LearningMaterial,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier,
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
    val size = 12.dp
    val shape = RoundedCornerShape(size)

    Text(
        text = "${learningMaterial.questions.size} " +
            "${stringResource(R.string.library_questions)} • " +
            "${(learningMaterial.progress * 100).roundToInt()}% " +
            stringResource(R.string.library_progress),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.5f),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(bottom = 8.dp),
    )

    ProgressBar(
        perc = learningMaterial.progress.toFloat(),
        size = size,
        shape = shape,
    )
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