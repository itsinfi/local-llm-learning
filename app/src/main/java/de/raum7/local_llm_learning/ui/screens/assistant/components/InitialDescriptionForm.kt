package de.raum7.local_llm_learning.ui.screens.assistant.components

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.screens.assistant.MIME_TYPES
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantUiStateChange
import de.raum7.local_llm_learning.ui.screens.assistant.types.InitialDescriptionUiState
import de.raum7.local_llm_learning.ui.shared.components.ButtonColorFill
import de.raum7.local_llm_learning.ui.shared.components.ButtonStyle
import de.raum7.local_llm_learning.ui.shared.components.ButtonType
import de.raum7.local_llm_learning.ui.shared.components.FileInput
import de.raum7.local_llm_learning.ui.shared.components.TextInput
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun InitialDescriptionForm(
    uiState: InitialDescriptionUiState,
    onChanged: (AssistantUiStateChange) -> Unit,
) {
    TextInput(
        title = stringResource(R.string.assistant_prompt),
        placeholder = stringResource(R.string.assistant_prompt_placeholder),
        value = uiState.prompt,
        onValueChange = { value -> onChanged(AssistantUiStateChange(prompt = value)) },
    )

    FileInput(
        placeholder = stringResource(R.string.assistant_file_placeholder),
        mimeTypes = MIME_TYPES,
        pathToSelectedFile = uiState.filePath,
        onFileSelected = { uri -> onChanged(AssistantUiStateChange(filePath = uri)) },
        buttonStyle = when(uiState.filePath) {
            null -> ButtonStyle.from(
                type = ButtonType.SECONDARY,
                colorFill = ButtonColorFill.FILLED,
            )
            else -> ButtonStyle.from(
                type = ButtonType.SECONDARY,
                colorFill = ButtonColorFill.OUTLINE,
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun InitialDescriptionFormPreview_Empty() {
    AppTheme {
        Column {
            InitialDescriptionForm(
                uiState = InitialDescriptionUiState(
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
fun InitialDescriptionFormPreview_Filled() {
    AppTheme {
        Column {
            InitialDescriptionForm(
                uiState = InitialDescriptionUiState(
                    filePath = "lorem-ipsum/dolor-sit-amet-consectetur-adipiscing-elit-sed.pdf".toUri(),
                    prompt = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                ),
                onChanged = {}
            )
        }
    }
}