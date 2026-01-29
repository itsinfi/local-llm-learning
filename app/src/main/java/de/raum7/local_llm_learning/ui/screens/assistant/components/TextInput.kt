package de.raum7.local_llm_learning.ui.screens.assistant.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun TextInput(
    title: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        onValueChange = onValueChange,
        value = value,
        label = { Text(title) },
        placeholder = { Text(placeholder) },
        maxLines = Int.MAX_VALUE,
        singleLine = false,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp, 300.dp)
            .padding(bottom = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun TextInputPreview_Empty() {
    AppTheme {
        TextInput(
            title = "Title",
            placeholder = "Insert a text here...",
            value = "",
            onValueChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextInputPreview_Filled() {
    AppTheme {
        TextInput(
            title = "Title",
            placeholder = "Insert a text here...",
            value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            onValueChange = {},
        )
    }
}