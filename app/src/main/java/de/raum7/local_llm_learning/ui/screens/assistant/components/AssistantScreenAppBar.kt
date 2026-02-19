package de.raum7.local_llm_learning.ui.screens.assistant.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.shared.components.AppBar
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun AssistantResultScreenAppBar(onBack: () -> Unit) {
    val navigationIcon = @Composable {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = stringResource(R.string.assisant_back_to_library),
            )
        }
    }

    AppBar(
        navigationIcon = navigationIcon,
        title = stringResource(R.string.assisant_result),
        textStyle = MaterialTheme.typography.headlineMedium,
    )
}

@Preview(showBackground = true)
@Composable
private fun AssistantResultScreenAppBarPreview() {
    AppTheme {
        Scaffold(topBar = { AssistantResultScreenAppBar(onBack = {}) }) {
            Text("Hello, World!")
        }
    }
}
