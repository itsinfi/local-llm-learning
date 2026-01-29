package de.raum7.local_llm_learning.ui.shared.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.ui.theme.AppTheme

data class SelectionUiState(
    val options: List<String>,
    val selected: String,
    val expanded: Boolean,
    val translations: Map<String, String>?,
) {
    companion object {
        fun from(
            options: List<String>,
            selected: String,
            translations: Map<String, String>? = null,
        ): SelectionUiState {
            if (selected !in options) {
                error("Selected option $selected is not defined in options: $options")
            }

            return SelectionUiState(
                options = options,
                selected = selected,
                expanded = false,
                translations = translations,
            )
        }
    }
}

@Composable
fun getTranslation(value: String, translations: Map<String, String>?): String = translations?.get(value) ?: value

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionInput(
    title: String,
    placeholder: String,
    uiState: SelectionUiState,
    onSelected: (String) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
    ) {
        Text(title)

        ExposedDropdownMenuBox(
            uiState.expanded,
            onExpandedChange = { expanded -> onExpandedChange(expanded) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            TextField(
                value = getTranslation(uiState.selected, uiState.translations),
                onValueChange = {},
                readOnly = true,
                label = { Text(placeholder) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(uiState.expanded)
                },
                modifier = Modifier
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryEditable,
                        enabled = true,
                    )
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                uiState.expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                uiState.options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(getTranslation(option, uiState.translations)) },
                        onClick = { onSelected(option) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectionInputPreview_Init() {
    AppTheme {
        SelectionInput(
            title = "Title",
            placeholder = "Choose an option...",
            uiState = SelectionUiState(
                options = listOf("Option 1", "Option 2", "Option 3"),
                selected = "Option 1",
                expanded = false,
                translations = null,
            ),
            onSelected = { _: String -> },
            onExpandedChange = { _: Boolean -> },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectionInputPreview_Expanded() {
    AppTheme {
        SelectionInput(
            title = "Title",
            placeholder = "Choose an option...",
            uiState = SelectionUiState(
                options = listOf("Option 1", "Option 2", "Option 3"),
                selected = "Option 1",
                expanded = true,
                translations = null,
            ),
            onSelected = { _: String -> },
            onExpandedChange = { _: Boolean -> },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectionInputPreview_Translated() {
    AppTheme {
        SelectionInput(
            title = "Title",
            placeholder = "Choose an option...",
            uiState = SelectionUiState(
                options = listOf("Option 1", "Option 2", "Option 3"),
                selected = "Option 1",
                expanded = true,
                translations = mapOf(
                    "Option 1" to "Translation 1",
                    "Option 2" to "Translation 2",
                    "Option 3" to "Translation 3",
                ),
            ),
            onSelected = { _: String -> },
            onExpandedChange = { _: Boolean -> },
        )
    }
}