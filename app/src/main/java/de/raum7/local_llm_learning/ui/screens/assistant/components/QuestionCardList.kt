package de.raum7.local_llm_learning.ui.screens.assistant.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.data.mock.MOCK_ANSWERS
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.mock.MOCK_QUESTIONS
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.data.models.Question
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun QuestionCardList(parsed: Pair<LearningMaterial, MutableMap<Question, List<Answer>>>) { // TODO: change parsed type to actual return type of json parsing function
    LearningMaterialTitle(parsed.first.title)

    Spacer(modifier = Modifier.height(16.dp))

    parsed.second.keys.forEachIndexed { i, q ->
        QuestionCard(i, q, parsed.second.getValue(q))
    }
}

@Preview(showBackground = true)
@Composable
private fun QuestionCardListPreview() {
    AppTheme { QuestionCardList(Pair<LearningMaterial, MutableMap<Question, List<Answer>>>(
        MOCK_LEARNING_MATERIALS[0],
        mutableMapOf(MOCK_QUESTIONS[0] to listOf<Answer>(MOCK_ANSWERS[0], MOCK_ANSWERS[1], MOCK_ANSWERS[2], MOCK_ANSWERS[3]))
        )
    ) }
}