package de.raum7.local_llm_learning.ui.screens.quiz.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.data.mock.MOCK_QUIZ_RESULTS
import de.raum7.local_llm_learning.data.models.QuizResult
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun ResultsPhaseCard(currentResult: QuizResult, onContinue: () -> Unit, padding: PaddingValues) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = when (currentResult.isCorrect) {
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
            ResultsHeader(currentResult.isCorrect)

            InfoText(
                header = stringResource(R.string.srascqbq_actual_answer),
                body = currentResult.correctAnswer.answer,
            )

            InfoText(
                header = stringResource(R.string.srascqbq_your_answer),
                body = currentResult.selectedAnswer.answer,
            )

            ContinueButton(
                isEnabled = true,
                onclick = onContinue,
                isError = !currentResult.isCorrect,
            )
        }
    }
}

@Composable
fun ResultsHeader(isCorrect: Boolean) {
    val text = when (isCorrect) {
        true -> stringResource(R.string.srascqbq_correct_result)
        false -> stringResource(R.string.srascqbq_incorrect_result)
    }

    val imageVector = when(isCorrect) {
        true -> Icons.Default.CheckCircle
        false -> Icons.Default.Error
    }

    val tint = when(isCorrect) {
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
        Icon(
            imageVector,
            contentDescription = text,
            tint = tint,
        )
        Text(
            text,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun InfoText(header: String, body: String) {
    Text(
        text = header,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
    Text(
        text = body,
        style = MaterialTheme.typography.bodyLarge,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Preview(showBackground = false)
@Composable
fun ResultsPhaseCardPreview_CorrectAnswer() {
    AppTheme {
        ResultsPhaseCard(
            currentResult = MOCK_QUIZ_RESULTS[0],
            onContinue = {},
            padding = PaddingValues.Zero,
        )
    }
}

@Preview(showBackground = false)
@Composable
fun ResultsPhaseCardPreview_IncorrectAnswer() {
    AppTheme {
        ResultsPhaseCard(
            currentResult = MOCK_QUIZ_RESULTS[1],
            onContinue = {},
            padding = PaddingValues.Zero,
        )
    }
}