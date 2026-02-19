package de.raum7.local_llm_learning.ui.shared.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.ui.theme.AppTheme

enum class ButtonType {
    PRIMARY,
    SECONDARY,
    TERTIARY,
    ERROR,
}

enum class ButtonColorFill {
    FILLED,
    OUTLINE,
}

data class ButtonStyle (
    val type: ButtonType,
    val colorFill: ButtonColorFill,
) {
    companion object {
        val DEFAULT = ButtonStyle(
            type = ButtonType.PRIMARY,
            colorFill = ButtonColorFill.FILLED,
        )

        fun from(
            type: ButtonType = DEFAULT.type,
            colorFill: ButtonColorFill = DEFAULT.colorFill,
        ): ButtonStyle {
            return ButtonStyle(
                type = type,
                colorFill = colorFill,
            )
        }
    }
}

@Composable
fun CustomElevatedButton(
    label: String,
    onclick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    style: ButtonStyle = ButtonStyle.DEFAULT,
) {
    // color definitions
    val containerColor: Color = when(style.colorFill) {
        ButtonColorFill.OUTLINE -> MaterialTheme.colorScheme.surfaceContainer
        ButtonColorFill.FILLED -> when(style.type) {
            ButtonType.PRIMARY -> MaterialTheme.colorScheme.primary
            ButtonType.SECONDARY -> MaterialTheme.colorScheme.secondary
            ButtonType.TERTIARY -> MaterialTheme.colorScheme.tertiary
            ButtonType.ERROR -> MaterialTheme.colorScheme.error
        }
    }
    val contentColor: Color = when(style.colorFill) {
        ButtonColorFill.OUTLINE -> MaterialTheme.colorScheme.onSurface
        ButtonColorFill.FILLED -> when(style.type) {
            ButtonType.PRIMARY -> MaterialTheme.colorScheme.onPrimary
            ButtonType.SECONDARY -> MaterialTheme.colorScheme.onSecondary
            ButtonType.TERTIARY -> MaterialTheme.colorScheme.onTertiary
            ButtonType.ERROR -> MaterialTheme.colorScheme.onError
        }
    }
    val disabledContainerColor: Color = containerColor.copy(alpha = 0.5f)
    val disabledContentColor: Color = contentColor.copy(alpha = 0.5f)

    // color selection
    val colors = ButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )

    ElevatedButton(
        onclick,
        enabled = isEnabled,
        colors = colors,
        border = when(style.colorFill) {
            ButtonColorFill.OUTLINE -> BorderStroke(
                width = 1.dp,
                color = when(style.type) {
                    ButtonType.PRIMARY -> MaterialTheme.colorScheme.primary
                    ButtonType.SECONDARY -> MaterialTheme.colorScheme.secondary
                    ButtonType.TERTIARY -> MaterialTheme.colorScheme.tertiary
                    ButtonType.ERROR -> MaterialTheme.colorScheme.error
                },
            )
            ButtonColorFill.FILLED -> null
        },
        modifier = modifier,
    ) {
        Text(text = label)
    }
}

// PRIMARY

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_PrimaryEnabledFilled() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = true,
            onclick = {},
            style = ButtonStyle(
                type = ButtonType.PRIMARY,
                colorFill = ButtonColorFill.FILLED,
            ),
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_PrimaryDisabledFilled() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = false,
            onclick = {},
            style = ButtonStyle(
                type = ButtonType.PRIMARY,
                colorFill = ButtonColorFill.FILLED,
            ),
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_PrimaryEnabledOutline() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = true,
            onclick = {},
            style = ButtonStyle(
                type = ButtonType.PRIMARY,
                colorFill = ButtonColorFill.OUTLINE,
            ),
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_PrimaryDisabledOutline() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = false,
            onclick = {},
            style = ButtonStyle(
                type = ButtonType.PRIMARY,
                colorFill = ButtonColorFill.OUTLINE,
            ),
        )
    }
}

// SECONDARY

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_SecondaryEnabledFilled() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = true,
            onclick = {},
            style = ButtonStyle(
                type = ButtonType.SECONDARY,
                colorFill = ButtonColorFill.FILLED,
            ),
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_SecondaryDisabledFilled() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = false,
            onclick = {},
            style = ButtonStyle(
                type = ButtonType.SECONDARY,
                colorFill = ButtonColorFill.FILLED,
            ),
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_SecondaryEnabledOutline() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = true,
            onclick = {},
            style = ButtonStyle(
                type = ButtonType.SECONDARY,
                colorFill = ButtonColorFill.OUTLINE,
            ),
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_SecondaryDisabledOutline() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = false,
            onclick = {},
            style = ButtonStyle(
                type = ButtonType.SECONDARY,
                colorFill = ButtonColorFill.OUTLINE,
            ),
        )
    }
}

// TERTIARY

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_TertiaryEnabledFilled() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = true,
            onclick = {},
            style = ButtonStyle(
                type = ButtonType.TERTIARY,
                colorFill = ButtonColorFill.FILLED,
            ),
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_TertiaryDisabledFilled() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = false,
            onclick = {},
            style = ButtonStyle(
                type = ButtonType.TERTIARY,
                colorFill = ButtonColorFill.FILLED,
            ),
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_TertiaryEnabledOutline() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = true,
            onclick = {},
            style = ButtonStyle(
                type = ButtonType.TERTIARY,
                colorFill = ButtonColorFill.OUTLINE,
            ),
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_TertiaryDisabledOutline() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = false,
            onclick = {},
            style = ButtonStyle(
                type = ButtonType.TERTIARY,
                colorFill = ButtonColorFill.OUTLINE,
            ),
        )
    }
}

// ERROR

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_ErrorEnabledFilled() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = true,
            onclick = {},
            style = ButtonStyle(
                type = ButtonType.ERROR,
                colorFill = ButtonColorFill.FILLED,
            ),
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_ErrorDisabledFilled() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = false,
            onclick = {},
            style = ButtonStyle(
                type = ButtonType.ERROR,
                colorFill = ButtonColorFill.FILLED,
            ),
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_ErrorEnabledOutline() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = true,
            onclick = {},
            style = ButtonStyle(
                type = ButtonType.ERROR,
                colorFill = ButtonColorFill.OUTLINE,
            ),
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_ErrorDisabledOutline() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = false,
            onclick = {},
            style = ButtonStyle(
                type = ButtonType.ERROR,
                colorFill = ButtonColorFill.OUTLINE,
            ),
        )
    }
}