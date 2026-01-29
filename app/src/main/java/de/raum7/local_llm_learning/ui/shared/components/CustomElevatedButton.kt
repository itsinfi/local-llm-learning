package de.raum7.local_llm_learning.ui.shared.components

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.ui.theme.AppTheme

enum class ButtonClass {
    PRIMARY,
    SECONDARY,
}

enum class ButtonStatus {
    SUCCESS,
    ERROR,
}

@Composable
fun CustomElevatedButton(
    label: String,
    onclick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    buttonClass: ButtonClass = ButtonClass.PRIMARY,
    buttonStatus: ButtonStatus = ButtonStatus.SUCCESS,
) {
    // color definitions
    val containerColor: Color = MaterialTheme.colorScheme.primaryContainer
    val contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
    val disabledContainerColor: Color = containerColor.copy(alpha = 0.5f)
    val disabledContentColor: Color = contentColor.copy(alpha = 0.5f)

    val containerColorError: Color = MaterialTheme.colorScheme.error
    val contentColorError: Color = MaterialTheme.colorScheme.onError
    val disabledContainerColorError: Color = containerColorError.copy(alpha = 0.5f)
    val disabledContentColorError: Color = contentColorError.copy(alpha = 0.5f)

    val containerColorSecondary: Color = MaterialTheme.colorScheme.secondary
    val contentColorSecondary: Color = MaterialTheme.colorScheme.onSecondary
    val disabledContainerColorSecondary: Color = containerColor.copy(alpha = 0.5f)
    val disabledContentColorSecondary: Color = contentColor.copy(alpha = 0.5f)

    val containerColorErrorSecondary: Color = MaterialTheme.colorScheme.onError
    val contentColorErrorSecondary: Color = MaterialTheme.colorScheme.error
    val disabledContainerColorErrorSecondary: Color = containerColorError.copy(alpha = 0.5f)
    val disabledContentColorErrorSecondary: Color = contentColorError.copy(alpha = 0.5f)

    // color selection
    val colors: ButtonColors = when(buttonClass) {
        ButtonClass.PRIMARY -> when(buttonStatus) {
            ButtonStatus.SUCCESS -> ButtonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = disabledContainerColor,
                disabledContentColor = disabledContentColor,
            )

            ButtonStatus.ERROR -> ButtonColors(
                containerColor = containerColorError,
                contentColor = contentColorError,
                disabledContainerColor = disabledContainerColorError,
                disabledContentColor = disabledContentColorError,
            )
        }

        ButtonClass.SECONDARY -> when(buttonStatus) {
            ButtonStatus.SUCCESS -> ButtonColors(
                containerColor = containerColorSecondary,
                contentColor = contentColorSecondary,
                disabledContainerColor = disabledContainerColorSecondary,
                disabledContentColor = disabledContentColorSecondary,
            )

            ButtonStatus.ERROR -> ButtonColors(
                containerColor = containerColorErrorSecondary,
                contentColor = contentColorErrorSecondary,
                disabledContainerColor = disabledContainerColorErrorSecondary,
                disabledContentColor = disabledContentColorErrorSecondary,
            )
        }
    }

    ElevatedButton(
        onclick,
        enabled = isEnabled,
        colors = colors,
        modifier = modifier
    ) {
        Text(text = label)
    }
}

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_EnabledNoError() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = true,
            onclick = {},
            buttonStatus = ButtonStatus.SUCCESS,
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_DisabledNoError() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = false,
            onclick = {},
            buttonStatus = ButtonStatus.SUCCESS,
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_EnabledError() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = true,
            onclick = {},
            buttonStatus = ButtonStatus.ERROR,
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomElevatedButtonPreview_DisabledError() {
    AppTheme {
        CustomElevatedButton(
            label = "Something",
            isEnabled = false,
            onclick = {},
            buttonStatus = ButtonStatus.ERROR,
        )
    }
}