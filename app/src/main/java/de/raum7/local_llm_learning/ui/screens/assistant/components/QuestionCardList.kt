package de.raum7.local_llm_learning.ui.screens.assistant.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun QuestionCardList(parsed: LearningMaterial) {
    LearningMaterialTitle(parsed.title)

    Spacer(modifier = Modifier.height(16.dp))

    parsed.questions.forEachIndexed { i, q ->
        QuestionCard(i, q)
    }
}

@Preview(showBackground = true)
@Composable
private fun QuestionCardListPreview() {
    AppTheme { QuestionCardList(MOCK_LEARNING_MATERIALS[0]) }
}