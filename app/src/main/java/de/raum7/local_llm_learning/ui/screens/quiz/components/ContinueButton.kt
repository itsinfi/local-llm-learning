package de.raum7.local_llm_learning.ui.screens.quiz.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun ContinueButton(
    isEnabled: Boolean,
    onclick: () -> Unit,
    isError: Boolean = false,
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ElevatedButton(
            onclick,
            enabled = isEnabled,
            colors = colors,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Text(text = stringResource(R.string.srascqbq_continue))
        }
    }
}

@Preview(showBackground = false)
@Composable
fun ContinueButtonPreview_EnabledNoError() {
    AppTheme {
        ContinueButton(
            isEnabled = true,
            onclick = {},
            isError = false,
        )
    }
}

@Preview(showBackground = false)
@Composable
fun ContinueButtonPreview_DisabledNoError() {
    AppTheme {
        ContinueButton(
            isEnabled = false,
            onclick = {},
            isError = false,
        )
    }
}

@Preview(showBackground = false)
@Composable
fun ContinueButtonPreview_EnabledError() {
    AppTheme {
        ContinueButton(
            isEnabled = true,
            onclick = {},
            isError = true,
        )
    }
}

@Preview(showBackground = false)
@Composable
fun ContinueButtonPreview_DisabledError() {
    AppTheme {
        ContinueButton(
            isEnabled = false,
            onclick = {},
            isError = true,
        )
    }
}