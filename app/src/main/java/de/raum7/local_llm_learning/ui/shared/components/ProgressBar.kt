package de.raum7.local_llm_learning.ui.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun ProgressBar(
    perc: Float,
    size: Dp = 20.dp,
    shape: Shape = RectangleShape,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        Box(
            modifier = Modifier
                .height(size)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.secondary.copy(0f),
                    shape = shape,
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.15f),
                    shape = shape,
                )
        )

        Box(
            modifier = when(perc) {
                0f -> Modifier
                    .height(size)
                    .width(size)
                    .background(
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                        shape = shape,
                    )
                else -> Modifier
                    .height(size)
                    .fillMaxWidth(perc)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = shape,
                    )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressBarPreview_0() {
    AppTheme {
        ProgressBar(0f)
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressBarPreview_60() {
    AppTheme {
        ProgressBar(0.6f)
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressBarPreview_100() {
    AppTheme {
        ProgressBar(1f)
    }
}