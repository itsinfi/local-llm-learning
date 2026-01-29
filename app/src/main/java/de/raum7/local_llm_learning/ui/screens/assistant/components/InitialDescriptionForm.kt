package de.raum7.local_llm_learning.ui.screens.assistant.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.screens.assistant.MIME_TYPES
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantUiStateChange
import de.raum7.local_llm_learning.ui.screens.assistant.types.InitialDescription
import de.raum7.local_llm_learning.ui.shared.components.FileInput
import de.raum7.local_llm_learning.ui.shared.components.TextInput
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun InitialDescriptionForm(
    uiState: InitialDescription,
    onChanged: (AssistantUiStateChange) -> Unit,
) {
    // file input
    FileInput(
        title = stringResource(R.string.assistant_file),
        placeholder = stringResource(R.string.assistant_file_placeholder),
        mimeTypes = MIME_TYPES,
        pathToSelectedFile = uiState.filePath,
        onFileSelected = { uri -> onChanged(AssistantUiStateChange(filePath = uri)) },
    )

    // prompt input
    TextInput(
        title = stringResource(R.string.assistant_prompt),
        placeholder = stringResource(R.string.assistant_prompt_placeholder),
        value = uiState.prompt,
        onValueChange = { value -> onChanged(AssistantUiStateChange(prompt = value)) },
    )
}

@Preview(showBackground = true)
@Composable
fun InitialDescriptionFormPreview_Filled() {
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

@Preview(showBackground = true)
@Composable
fun InitialDescriptionFormPreview_Selected() {
    AppTheme {
        Column {
            InitialDescriptionForm(
                uiState = InitialDescription(
                    filePath = "lorem-ipsum/dolor-sit-amet-consectetur-adipiscing-elit-sed.pdf".toUri(),
                    prompt = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                ),
                onChanged = {}
            )
        }
    }
}