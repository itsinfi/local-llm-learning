package de.raum7.local_llm_learning.ui.screens.assistant

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.shared.components.AppBar
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun AssistantScreen(uiState: AssistantUiState) {
    Scaffold(
        topBar = { AppBar(title = stringResource(R.string.lmca)) },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Text(
            text = "Assistant:\n\n${uiState.toString()}",
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AssistantScreenPreview_initial() {
    AppTheme {
        AssistantScreen(
            AssistantUiState(
                filePath = null,
                prompt = "",
                questionCount = DEFAULT_QUESTION_COUNT,
                topicSpecification = "",
                goal = "",
            )
        )
    }
}