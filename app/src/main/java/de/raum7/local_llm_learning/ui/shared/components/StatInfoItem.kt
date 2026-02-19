package de.raum7.local_llm_learning.ui.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.data.mock.MOCK_QUIZ_RESULTS
import de.raum7.local_llm_learning.ui.shared.utils.formatDoubleString
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun StatInfoItem(
    icon: ImageVector,
    contentDescription: String,
    value: Double,
    unit: String,
    modifier: Modifier = Modifier,
) {
    val color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
    val backgroundColor = Color.White.copy(0.1f)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(8.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = color,
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text = formatDoubleString(value = value, unit = unit),
            style = MaterialTheme.typography.bodySmall,
            color = color,
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