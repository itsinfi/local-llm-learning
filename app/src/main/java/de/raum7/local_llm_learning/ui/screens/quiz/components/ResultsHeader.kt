package de.raum7.local_llm_learning.ui.screens.quiz.components

import androidx.compose.foundation.layout.Arrangement
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
import de.raum7.local_llm_learning.ui.shared.components.StatInfoItem
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun ResultsHeader(result: QuizResult) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(bottom = 32.dp)
            .fillMaxWidth(),
    ) {
        ResultsSummary(result)
        TimeStats(result)
    }
}

@Composable
fun ResultsSummary(result: QuizResult) {
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
}

@Composable
fun TimeStats(result: QuizResult) {
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

@Preview(showBackground = true)
@Composable
fun ResultsHeaderPreview_Correct() {
    AppTheme {
        ResultsHeader(MOCK_QUIZ_RESULTS[0])
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsHeaderPreview_Incorrect() {
    AppTheme {
        ResultsHeader(MOCK_QUIZ_RESULTS[1])
    }
}