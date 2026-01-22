package de.raum7.local_llm_learning.ui.screens.quiz.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.shared.utils.formatDoubleString
import de.raum7.local_llm_learning.ui.theme.AppTheme
import java.util.Locale

@Composable
fun TimerText(elapsedNanoSeconds: Long) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        val color = MaterialTheme.colorScheme.surfaceVariant

        Icon(
            imageVector = Icons.Outlined.Timer,
            contentDescription = stringResource(R.string.srascqbq_your_time),
            tint = color,
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text = format(elapsedNanoSeconds),
            style = MaterialTheme.typography.bodyLarge,
            color = color,
        )
    }
}

private fun format(nanoSeconds: Long): String {
    val seconds = nanoSeconds / 1_000_000_000.0
    return formatDoubleString(value = seconds, unit = "s")
}

@Preview(showBackground = true)
@Composable
fun TimerTextPreview() {
    AppTheme {
        TimerText(System.nanoTime())
    }
}