package de.raum7.local_llm_learning.ui.shared.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.data.mock.MOCK_QUIZ_RESULTS
import de.raum7.local_llm_learning.ui.theme.AppTheme
import java.util.Locale

@Composable
fun StatInfoItem(icon: ImageVector, contentDescription: String, value: Double, unit: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            imageVector = icon,
            contentDescription,
        )
        Text(
            // TODO: make locale dynamic
            text = "${String.format(locale = Locale.GERMANY, format ="%.2f", value)}$unit",
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatInfoItemPreview() {
    AppTheme {
        StatInfoItem(
            icon = Icons.Default.Timer,
            contentDescription = "Benötigte Zeit",
            value = MOCK_QUIZ_RESULTS[0].elapsedNanoSeconds / 1_000_000_000.0,
            unit = "s"
        )
    }
}