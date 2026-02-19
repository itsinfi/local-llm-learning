package de.raum7.local_llm_learning.ui.shared.components

import androidx.compose.foundation.border
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.ui.theme.AppTheme

data class CustomCardColors(
    val outlineColor: Color,
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
) {
    fun getCardColors(): CardColors {
        return CardColors(
            containerColor = this.containerColor,
            contentColor = this.contentColor,
            disabledContainerColor = this.disabledContainerColor,
            disabledContentColor = this.disabledContentColor,
        )
    }

    companion object {

        @Composable
        fun from(
            outlineColor: Color? = null,
            containerColor: Color? = null,
            contentColor: Color? = null,
            disabledContainerColor: Color? = null,
            disabledContentColor: Color? = null,
        ): CustomCardColors {
            val cardDefaultColors = CardDefaults.cardColors()

            return CustomCardColors(
                outlineColor = outlineColor ?: MaterialTheme.colorScheme.primary,
                containerColor = containerColor ?: MaterialTheme.colorScheme.surfaceContainer,
                contentColor = contentColor ?: cardDefaultColors.contentColor,
                disabledContainerColor = disabledContainerColor ?: cardDefaultColors.disabledContainerColor,
                disabledContentColor = disabledContentColor ?: cardDefaultColors.disabledContentColor,
            )
        }
    }
}

@Composable
fun CustomCard(
    onClick: () -> Unit = {},
    colors: CustomCardColors = CustomCardColors.from(),
    blurredBoxOptions: BlurredBoxOptions = BlurredBoxOptions.DEFAULT,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit),
) {
    Card(
        onClick = onClick,
        colors = colors.getCardColors(),
        modifier = modifier
            .border(
                width = 1.dp,
                color = colors.outlineColor,
                shape = CardDefaults.shape,
            ),
    ) {
        BlurredBox(
            backgroundContent = content,
            foregroundContent = content,
            options = blurredBoxOptions,
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun CustomCardPreview() {
    AppTheme {
        CustomCard {
            Text("hello")
        }
    }
}