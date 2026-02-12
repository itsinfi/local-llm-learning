package de.raum7.local_llm_learning.ui.shared.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun BlurredBox(
    backgroundContent: @Composable (() -> Unit),
    foregroundContent: @Composable (() -> Unit),
    blurRadius: Dp = 20.dp,
    alpha: Float = 0.4f,
    saturation: Float = 0.1f,
) {
    Box {
        Box(
            Modifier
                .blur(blurRadius)
                .alpha(alpha)
                .graphicsLayer {
                    colorFilter = ColorFilter.colorMatrix(
                        ColorMatrix().apply { setToSaturation(saturation) }
                    )
                }
        ) {
            backgroundContent()
        }

        foregroundContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun BlurredBoxPreview() {
    AppTheme {
        val color = Color(0xFF00AA88)
        val text = "Hello, World!"

        BlurredBox(
            backgroundContent = { Text(text = "  $text", color = color) },
            foregroundContent = { Text(text = text, color = color) },
            blurRadius = 1.dp,
            alpha = 0.5f,
            saturation = 0.5f,
        )
    }
}