package de.raum7.local_llm_learning.ui.screens.quiz.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.Question
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun AnsweringPhaseCard(
    question: Question,
    selectedAnswer: Answer?,
    elapsedTime: Long,
    onAnswerSelected: (Answer) -> Unit,
    onContinue: () -> Unit,
    padding: PaddingValues
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 16.dp)
            .padding(top = 64.dp),
    ) {
        LazyColumn (
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(16.dp)
        ) {
            item {
                TimerText(elapsedTime)
            }

            item {
                QuestionTitle(question)
            }

            items(question.answers) { answer: Answer ->
                AnswerSelection(answer, isSelected = answer == selectedAnswer, onClick = { onAnswerSelected(answer) })
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ContinueButton(
                        isEnabled = selectedAnswer != null,
                        onclick = onContinue,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionTitle(question: Question) {
    Text(
        text = question.question,
        style = MaterialTheme.typography.headlineMedium,
        maxLines = 4,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(bottom = 32.dp)
    )
}

@Composable
fun AnswerSelection(
    answer: Answer,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton
            )
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = answer.answer,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = false)
@Composable
fun AnsweringPhaseCardPreview_Unselected() {
    AppTheme {
        AnsweringPhaseCard(
            question = MOCK_LEARNING_MATERIALS[0].questions[0],
            selectedAnswer = null,
            elapsedTime = 1000L,
            onAnswerSelected = {},
            onContinue = {},
            padding = PaddingValues.Zero,
        )
    }
}

@Preview(showBackground = false)
@Composable
fun AnsweringPhaseCardPreview_Selected() {
    AppTheme {
        AnsweringPhaseCard(
            question = MOCK_LEARNING_MATERIALS[0].questions[0],
            selectedAnswer = MOCK_LEARNING_MATERIALS[0].questions[0].answers[0],
            elapsedTime = 1000L,
            onAnswerSelected = {},
            onContinue = {},
            padding = PaddingValues.Zero,
        )
    }
}