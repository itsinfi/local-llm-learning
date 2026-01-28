package de.raum7.local_llm_learning.ui.screens.quiz.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import de.raum7.local_llm_learning.data.mock.MOCK_QUIZ_RESULTS
import de.raum7.local_llm_learning.data.models.QuizResult
import de.raum7.local_llm_learning.ui.shared.components.InfoTextBlock
import de.raum7.local_llm_learning.ui.shared.components.StatInfoItem
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun ResultsPhaseCard(
    result: QuizResult,
    onContinue: () -> Unit,
    onEdit: () -> Unit,
    padding: PaddingValues,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = when (result.isCorrect) {
                true -> MaterialTheme.colorScheme.primary
                false -> MaterialTheme.colorScheme.errorContainer
            }
        ),
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 16.dp)
            .padding(top = 64.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(16.dp)
        ) {
            ResultsHeader(result)

            InfoTextBlock(
                title = stringResource(R.string.quiz_actual_answer),
                content = result.correctAnswer.answer,
            )

            InfoTextBlock(
                title = stringResource(R.string.quiz_your_answer),
                content = result.selectedAnswer.answer,
            )
            ButtonSection(isError = !result.isCorrect, onEdit, onContinue)
        }
    }
}

@Composable
fun ResultsHeader(result: QuizResult) {
    val text = when (result.isCorrect) {
        true -> stringResource(R.string.quiz_correct_result)
        false -> stringResource(R.string.quiz_incorrect_result)
    }

    val imageVector = when(result.isCorrect) {
        true -> Icons.Default.CheckCircle
        false -> Icons.Default.Error
    }

    val tint = when(result.isCorrect) {
        true -> MaterialTheme.colorScheme.primaryContainer
        false -> MaterialTheme.colorScheme.error
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(bottom = 32.dp)
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Icon(
                imageVector,
                contentDescription = text,
                tint = tint,
            )

            Spacer(Modifier.width(4.dp))

            Text(
                text,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            StatInfoItem(
                icon = Icons.Outlined.Timer,
                contentDescription = stringResource(R.string.quiz_your_time),
                value = result.elapsedNanoSeconds / 1_000_000_000.0,
                unit = "s",
            )

            Spacer(Modifier.width(16.dp))

            StatInfoItem(
                icon = Icons.Default.History,
                contentDescription = stringResource(R.string.quiz_your_average_time),
                value = result.previousNanoSeconds / 1_000_000_000.0,
                unit = "s",
            )
        }
    }
}

@Composable
fun ButtonSection(isError: Boolean, onEdit: () -> Unit, onContinue: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        EditQuestionButton(
            isEnabled = true,
            onclick = onEdit,
            isError = isError,
        )
        Spacer(modifier = Modifier.width(8.dp))
        ContinueButton(
            isEnabled = true,
            onclick = onContinue,
            isError = isError
        )
    }
}

@Preview(showBackground = false)
@Composable
fun ResultsPhaseCardPreview_CorrectAnswer() {
    AppTheme {
        ResultsPhaseCard(
            result = MOCK_QUIZ_RESULTS[0],
            onContinue = {},
            padding = PaddingValues.Zero,
            onEdit = {}
        )
    }
}

@Preview(showBackground = false)
@Composable
fun ResultsPhaseCardPreview_IncorrectAnswer() {
    AppTheme {
        ResultsPhaseCard(
            result = MOCK_QUIZ_RESULTS[1],
            onContinue = {},
            padding = PaddingValues.Zero,
            onEdit = {}
        )
    }
}