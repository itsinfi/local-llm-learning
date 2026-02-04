package de.raum7.local_llm_learning.ui.screens.edit_question

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.shared.components.AppBar

@Composable
fun EditQuestionScreen(
    uiState: EditQuestionUiState,
) {
    Scaffold(
        topBar = { AppBar(title = stringResource(R.string.edit_question)) },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
    }
}