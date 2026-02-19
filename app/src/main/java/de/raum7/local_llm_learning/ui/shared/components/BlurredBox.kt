package de.raum7.local_llm_learning.ui.shared.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.ui.theme.AppTheme

val DEFAULT_BLUR_RADIUS = 8.dp
const val DEFAULT_ALPHA = 0.2f
const val DEFAULT_SATURATION = 0.5f
const val DEFAULT_SCALE = 1.25f


data class BlurredBoxOptions(
    val blurRadius: Dp,
    val alpha: Float,
    val saturation: Float,
    val scale: Float,
) {
    companion object {
        val DEFAULT = BlurredBoxOptions(
            blurRadius = DEFAULT_BLUR_RADIUS,
            alpha = DEFAULT_ALPHA,
            saturation = DEFAULT_SATURATION,
            scale = DEFAULT_SCALE,
        )
    }
}

@Composable
fun BlurredBox(
    backgroundContent: @Composable (() -> Unit),
    foregroundContent: @Composable (() -> Unit),
    options: BlurredBoxOptions = BlurredBoxOptions.DEFAULT,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        Box(
            Modifier
                .blur(options.blurRadius)
                .alpha(options.alpha)
                .graphicsLayer {
                    colorFilter = ColorFilter.colorMatrix(
                        ColorMatrix().apply { setToSaturation(options.saturation) }
                    )
                }
                .scale(options.scale)
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
        val content = @Composable { Text(text = "Hello, World!", color = color) }

        BlurredBox(
            backgroundContent = content,
            foregroundContent = content,
            options = BlurredBoxOptions(
                blurRadius = 1.dp,
                alpha = 0.5f,
                saturation = 0.5f,
                scale = 1.1f,
            )
        )
    }
}