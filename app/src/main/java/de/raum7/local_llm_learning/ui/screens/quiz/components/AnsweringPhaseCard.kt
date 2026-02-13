package de.raum7.local_llm_learning.ui.screens.quiz.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.Question
import de.raum7.local_llm_learning.ui.shared.components.CustomCard
import de.raum7.local_llm_learning.ui.shared.components.CustomElevatedButton
import de.raum7.local_llm_learning.ui.shared.components.CustomRadioButton
import de.raum7.local_llm_learning.ui.shared.components.StatInfoItem
import de.raum7.local_llm_learning.ui.theme.AppTheme
import de.raum7.local_llm_learning.ui.theme.bodyFontFamily

@Composable
fun AnsweringPhaseCard(
    question: Question,
    selectedAnswer: Answer?,
    elapsedTime: Long,
    onAnswerSelected: (Answer) -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CustomCard(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 64.dp),
    ) {
        AnsweringPhaseCardContent(
            question = question,
            selectedAnswer = selectedAnswer,
            elapsedTime = elapsedTime,
            onAnswerSelected = onAnswerSelected,
            onContinue = onContinue,
        )
    }
}

@Composable
private fun AnsweringPhaseCardContent(
    question: Question,
    selectedAnswer: Answer?,
    elapsedTime: Long,
    onAnswerSelected: (Answer) -> Unit,
    onContinue: () -> Unit,
) {
    LazyColumn (
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            QuizTimer(elapsedTime)
        }

        item {
            QuestionTitle(question)
        }

        items(question.answers) { answer: Answer ->
            AnswerSelection(answer, isSelected = answer == selectedAnswer, onClick = { onAnswerSelected(answer) })
        }

        item {
            ButtonSection(isEnabled = selectedAnswer != null, onContinue = onContinue)
        }
    }
}

@Composable
private fun QuizTimer(
    elapsedNanoSeconds: Long,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        StatInfoItem(
            icon = Icons.Outlined.Timer,
            contentDescription = stringResource(R.string.quiz_your_time),
            value = elapsedNanoSeconds / 1_000_000_000.0,
            unit = "s",
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun QuestionTitle(question: Question) {
    Text(
        text = question.question,
        style = MaterialTheme.typography.bodyLarge.copy(fontFamily = bodyFontFamily),
        maxLines = 4,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(bottom = 32.dp)
    )
}

@Composable
private fun AnswerSelection(
    answer: Answer,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val shape = CardDefaults.shape

    Box(
        Modifier
            .padding(bottom = 16.dp)
            .clip(shape)
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton,
            )
            .background(
                color = Color.White.copy(alpha = 0.05f),
                shape = shape,
            )
            .border(
                width = 1.dp,
                color = when(isSelected) {
                    true -> MaterialTheme.colorScheme.primary
                    false -> MaterialTheme.colorScheme.surfaceVariant
                },
                shape = shape,
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            CustomRadioButton(isSelected)

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = answer.answer,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ButtonSection(isEnabled: Boolean, onContinue: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        CustomElevatedButton(
            label = stringResource(R.string.quiz_continue),
            isEnabled = isEnabled,
            onclick = onContinue,
            modifier = Modifier.align(Alignment.CenterEnd),
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
        )
    }
}