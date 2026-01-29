package de.raum7.local_llm_learning.ui.screens.edit_question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.ui.screens.edit_question.components.EditQuestionCard
import de.raum7.local_llm_learning.ui.shared.components.AppBar

@Composable
fun EditQuestionScreen(
    uiState: EditQuestionUiState,
    onEditableAnswerSelected: (Answer) -> Unit,
) {
    Scaffold(
        topBar = { AppBar(title = stringResource(R.string.edit_question)) },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight(),
        ) {
            EditQuestionCard(
                question = uiState.question,
                selectedEditableAnswer = uiState.selectedEditableAnswer,
                onEditableAnswerSelected,
                padding,
            )
        }
    }
}