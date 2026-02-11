package de.raum7.local_llm_learning.ui.screens.edit_question.components

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
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.data.mock.MOCK_ANSWERS
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.mock.MOCK_QUESTIONS
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.Question
import de.raum7.local_llm_learning.ui.screens.quiz.components.QuestionTitle
import de.raum7.local_llm_learning.ui.shared.components.CustomElevatedButton
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun EditQuestionCard(
    question: Question,
    answers: List<Answer>,
    selectedEditableAnswer: Answer?,
    onEditableAnswerSelected: (Answer) -> Unit,
    padding: PaddingValues
) {
    Card(
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
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
                QuestionTitle(question)
            }

            items(answers) { answer: Answer ->
                EditableAnswerSelection(answer, isSelected = answer == selectedEditableAnswer, onClick = { onEditableAnswerSelected(answer) })
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    CustomElevatedButton(
                        label = stringResource(R.string.confirm_edit),
                        onclick = { }, // TODO: add function to save edited questions
                        modifier = Modifier.align(Alignment.CenterEnd),
                    )
                }
            }
        }
    }
}

@Composable
fun EditableAnswerSelection(
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
        Checkbox(
            checked = isSelected,
            onCheckedChange = null,
        )

        Spacer(modifier = Modifier.width(16.dp))

        TextField(
            state = rememberTextFieldState(initialText = answer.answer),
            textStyle = MaterialTheme.typography.bodyMedium,
            lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 4), // TODO: global line limit for answers
        )
    }
}

@Preview(showBackground = false)
@Composable
fun EditQuestionCardTest() {
    AppTheme {
        EditQuestionCard(
            question = MOCK_QUESTIONS[0],
            answers = listOf(MOCK_ANSWERS[0], MOCK_ANSWERS[1], MOCK_ANSWERS[2], MOCK_ANSWERS[3]),
            selectedEditableAnswer = null,
            onEditableAnswerSelected = {},
            padding = PaddingValues.Zero,
        )
    }
}