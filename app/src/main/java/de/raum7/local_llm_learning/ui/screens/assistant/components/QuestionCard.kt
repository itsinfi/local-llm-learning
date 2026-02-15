package de.raum7.local_llm_learning.ui.screens.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.data.mock.MOCK_ANSWERS
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.mock.MOCK_QUESTIONS
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.Question
import de.raum7.local_llm_learning.ui.shared.components.CustomCard
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun QuestionCard(i: Int, q: Question, answers: List<Answer>) {
    CustomCard(
        modifier = Modifier.padding(bottom = 16.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "${i + 1}. ${q.question}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(6.dp))

            answers.forEach { a ->
                val shape = CardDefaults.shape
                
                Spacer(Modifier.padding(top = 4.dp))

                Box(Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(0.1f),
                        shape = shape,
                    )
                    .border(
                        width = 1.dp,
                        color = when(a.isCorrect) {
                            true -> MaterialTheme.colorScheme.secondary
                            false -> MaterialTheme.colorScheme.error
                        },
                        shape = shape,
                    )
                    .padding(8.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        when(a.isCorrect) {
                            true -> Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = stringResource(R.string.assisant_result_correct),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            false -> Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = stringResource(R.string.assisant_result_incorrect),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }

                        Spacer(Modifier.padding(4.dp))

                        Text(text = a.answer)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuestionCardPreview() {
    AppTheme {
        QuestionCard(2, MOCK_QUESTIONS[0], listOf(MOCK_ANSWERS[0], MOCK_ANSWERS[1], MOCK_ANSWERS[2], MOCK_ANSWERS[3]))
    }
}