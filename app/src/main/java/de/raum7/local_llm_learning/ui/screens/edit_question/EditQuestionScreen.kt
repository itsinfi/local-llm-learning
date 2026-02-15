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
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.data.mock.MOCK_ANSWERS
import de.raum7.local_llm_learning.data.mock.MOCK_QUESTIONS
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.ui.screens.edit_question.components.EditQuestionCard
import de.raum7.local_llm_learning.ui.shared.components.AppBar
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun EditQuestionScreen(
    uiState: EditQuestionUiState,
    onEditableAnswerSelected: (Answer) -> Unit,
    onQuestionSave: (List<Answer>) -> Unit
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
                answers = uiState.answers,
                selectedEditableAnswer = uiState.selectedEditableAnswer,
                onEditableAnswerSelected = onEditableAnswerSelected,
                onQuestionSave = onQuestionSave,
                padding = padding,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditQuestionScreenPreview_Unselected() {
    val mockQuestion = MOCK_QUESTIONS[0]
    val mockAnswers = MOCK_ANSWERS.filter { it.questionId == mockQuestion.id }

    AppTheme {
        EditQuestionScreen(
            uiState = EditQuestionUiState(
                question = mockQuestion,
                answers = mockAnswers,
                selectedEditableAnswer = null
            ),
            onEditableAnswerSelected = {},
            onQuestionSave = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditQuestionScreenPreview_Selected() {
    val mockQuestion = MOCK_QUESTIONS[0]
    val mockAnswers = MOCK_ANSWERS.filter { it.questionId == mockQuestion.id }

    AppTheme {
        EditQuestionScreen(
            uiState = EditQuestionUiState(
                question = mockQuestion,
                answers = mockAnswers,
                selectedEditableAnswer = mockAnswers[0]
            ),
            onEditableAnswerSelected = {},
            onQuestionSave = {},
        )
    }
}
