package de.raum7.local_llm_learning.ui.screens.quiz.components

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun EditQuestionButton(
    isEnabled: Boolean,
    onclick: () -> Unit,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    val colors: ButtonColors = when (isError) {
        true -> ButtonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
            disabledContainerColor = MaterialTheme.colorScheme.onErrorContainer,
            disabledContentColor = MaterialTheme.colorScheme.errorContainer,
        )
        false -> ButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        )
    }

    ElevatedButton(
        onclick,
        enabled = isEnabled,
        colors = colors,
        modifier = modifier
    ) {
        Text(text = stringResource(R.string.quiz_edit_button))
    }
}

@Preview(showBackground = false)
@Composable
fun EditButtonPreview_EnabledNoError() {
    AppTheme {
        EditQuestionButton(
            isEnabled = true,
            onclick = {},
            isError = false,
        )
    }
}

@Preview(showBackground = false)
@Composable
fun EditButtonPreview_DisabledNoError() {
    AppTheme {
        EditQuestionButton(
            isEnabled = false,
            onclick = {},
            isError = false,
        )
    }
}

@Preview(showBackground = false)
@Composable
fun EditButtonPreview_EnabledError() {
    AppTheme {
        EditQuestionButton(
            isEnabled = true,
            onclick = {},
            isError = true,
        )
    }
}

@Preview(showBackground = false)
@Composable
fun EditButtonPreview_DisabledError() {
    AppTheme {
        EditQuestionButton(
            isEnabled = false,
            onclick = {},
            isError = true,
        )
    }
}