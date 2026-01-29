package de.raum7.local_llm_learning.ui.screens.assistant.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import de.raum7.local_llm_learning.ui.screens.assistant.AssistantSelectionUiStateType
import de.raum7.local_llm_learning.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionInput(
    title: String,
    placeholder: String,
    type: AssistantSelectionUiStateType,
    options: List<String>,
    selected: String,
    expanded: Boolean,
    onSelected: (String, AssistantSelectionUiStateType) -> Unit,
    onExpandedChange: (Boolean, AssistantSelectionUiStateType) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(bottom = 16.dp),
    ) {
        Text(title)

        ExposedDropdownMenuBox(
            expanded,
            onExpandedChange = { expanded -> onExpandedChange(expanded, type) }
        ) {
            TextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                label = { Text(placeholder) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier.menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryEditable,
                    enabled = true,
                )
            )

            ExposedDropdownMenu(
                expanded,
                onDismissRequest = { onExpandedChange(false, type) },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = { onSelected(option, type) },
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
            options = listOf("Option 1", "Option 2", "Option 3"),
            type = AssistantSelectionUiStateType.QUESTION_COUNT,
            selected = "Option 1",
            expanded = false,
            onSelected = { _: String, _: AssistantSelectionUiStateType -> },
            onExpandedChange = { _: Boolean, _:AssistantSelectionUiStateType -> },
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
            options = listOf("Option 1", "Option 2", "Option 3"),
            type = AssistantSelectionUiStateType.QUESTION_COUNT,
            selected = "Option 1",
            expanded = true,
            onSelected = { _: String, _: AssistantSelectionUiStateType -> },
            onExpandedChange = { _: Boolean, _:AssistantSelectionUiStateType -> },
        )
    }
}