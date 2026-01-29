package de.raum7.local_llm_learning.ui.screens.assistant.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantUiStateChange
import de.raum7.local_llm_learning.ui.screens.assistant.types.InitialDescription
import de.raum7.local_llm_learning.ui.shared.components.TextInput
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun InitialDescriptionForm(
    uiState: InitialDescription,
    onChanged: (AssistantUiStateChange) -> Unit,
) {
    // file input
    // TODO: implement file input component
    Text("FILE INPUT PLACEHOLDER", modifier = Modifier.padding(bottom = 16.dp))

    // prompt input
    TextInput(
        title = stringResource(R.string.assistant_prompt),
        placeholder = stringResource(R.string.assistant_prompt_placeholder),
        value = uiState.prompt,
        onValueChange = { value -> onChanged(AssistantUiStateChange(prompt = value)) },
    )
}

// TODO: previews

@Preview(showBackground = true)
@Composable
fun InitialDescriptionFormPreview_Initial() {
    AppTheme {
        Column {
            InitialDescriptionForm(
                uiState = InitialDescription(
                    filePath = null,
                    prompt = "",
                ),
                onChanged = {}
            )
        }
    }
}