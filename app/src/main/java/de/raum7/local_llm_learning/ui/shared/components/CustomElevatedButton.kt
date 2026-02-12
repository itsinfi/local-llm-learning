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

enum class ButtonClass {
    PRIMARY,
    SECONDARY,
    TERTIARY,
    ERROR,
}

enum class ButtonColorFillType {
    FILLED,
    OUTLINE,
}

data class ButtonStyle (
    val buttonClass: ButtonClass,
    val fillType: ButtonColorFillType,
) {
    companion object {
        val DEFAULT = ButtonStyle(
            buttonClass = ButtonClass.PRIMARY,
            fillType = ButtonColorFillType.FILLED,
        )
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
    val containerColor: Color = when(style.fillType) {
        ButtonColorFillType.OUTLINE -> MaterialTheme.colorScheme.surfaceContainer
        ButtonColorFillType.FILLED -> when(style.buttonClass) {
            ButtonClass.PRIMARY -> MaterialTheme.colorScheme.primary
            ButtonClass.SECONDARY -> MaterialTheme.colorScheme.secondary
            ButtonClass.TERTIARY -> MaterialTheme.colorScheme.tertiary
            ButtonClass.ERROR -> MaterialTheme.colorScheme.error
        }
    }
    val contentColor: Color = when(style.fillType) {
        ButtonColorFillType.OUTLINE -> MaterialTheme.colorScheme.onSurface
        ButtonColorFillType.FILLED -> when(style.buttonClass) {
            ButtonClass.PRIMARY -> MaterialTheme.colorScheme.onPrimary
            ButtonClass.SECONDARY -> MaterialTheme.colorScheme.onSecondary
            ButtonClass.TERTIARY -> MaterialTheme.colorScheme.onTertiary
            ButtonClass.ERROR -> MaterialTheme.colorScheme.onError
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
        border = when(style.fillType) {
            ButtonColorFillType.OUTLINE -> BorderStroke(
                width = 1.dp,
                color = when(style.buttonClass) {
                    ButtonClass.PRIMARY -> MaterialTheme.colorScheme.primary
                    ButtonClass.SECONDARY -> MaterialTheme.colorScheme.secondary
                    ButtonClass.TERTIARY -> MaterialTheme.colorScheme.tertiary
                    ButtonClass.ERROR -> MaterialTheme.colorScheme.error
                },
            )
            ButtonColorFillType.FILLED -> null
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
                buttonClass = ButtonClass.PRIMARY,
                fillType = ButtonColorFillType.FILLED,
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
                buttonClass = ButtonClass.PRIMARY,
                fillType = ButtonColorFillType.FILLED,
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
                buttonClass = ButtonClass.PRIMARY,
                fillType = ButtonColorFillType.OUTLINE,
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
                buttonClass = ButtonClass.PRIMARY,
                fillType = ButtonColorFillType.OUTLINE,
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
                buttonClass = ButtonClass.SECONDARY,
                fillType = ButtonColorFillType.FILLED,
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
                buttonClass = ButtonClass.SECONDARY,
                fillType = ButtonColorFillType.FILLED,
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
                buttonClass = ButtonClass.SECONDARY,
                fillType = ButtonColorFillType.OUTLINE,
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
                buttonClass = ButtonClass.SECONDARY,
                fillType = ButtonColorFillType.OUTLINE,
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
                buttonClass = ButtonClass.TERTIARY,
                fillType = ButtonColorFillType.FILLED,
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
                buttonClass = ButtonClass.TERTIARY,
                fillType = ButtonColorFillType.FILLED,
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
                buttonClass = ButtonClass.TERTIARY,
                fillType = ButtonColorFillType.OUTLINE,
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
                buttonClass = ButtonClass.TERTIARY,
                fillType = ButtonColorFillType.OUTLINE,
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
                buttonClass = ButtonClass.ERROR,
                fillType = ButtonColorFillType.FILLED,
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
                buttonClass = ButtonClass.ERROR,
                fillType = ButtonColorFillType.FILLED,
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
                buttonClass = ButtonClass.ERROR,
                fillType = ButtonColorFillType.OUTLINE,
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
                buttonClass = ButtonClass.ERROR,
                fillType = ButtonColorFillType.OUTLINE,
            ),
        )
    }
}