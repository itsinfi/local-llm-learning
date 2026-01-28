package de.raum7.local_llm_learning.ui.screens.quiz

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
import de.raum7.local_llm_learning.data.mock.MOCK_QUIZ_RESULTS
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.ui.screens.quiz.components.AnsweringPhaseCard
import de.raum7.local_llm_learning.ui.shared.components.AppBar
import de.raum7.local_llm_learning.ui.shared.components.EmptyPlaceholder
import de.raum7.local_llm_learning.ui.screens.quiz.components.ResultsPhaseCard
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun QuizScreen(
    uiState: QuizUiState,
    onAnswerSelected: (Answer) -> Unit,
    onContinue: () -> Unit,
    onEdit: () -> Unit
) {
    Scaffold(
        topBar = { AppBar(title = stringResource(R.string.quiz)) },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        when (uiState.phase) {
            QuizPhase.ANSWERING -> Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxHeight(),
            ) {
                AnsweringPhaseCard(
                    question = uiState.question,
                    selectedAnswer = uiState.selectedAnswer,
                    elapsedTime = uiState.elapsedTime,
                    onAnswerSelected,
                    onContinue,
                    padding
                )
            }

            QuizPhase.RESULTS -> when (uiState.result) {
                null -> EmptyPlaceholder(stringResource(R.string.quiz_error_invalid_result), padding)
                else -> ResultsPhaseCard(uiState.result, onContinue, onEdit, padding)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview_Answering() {
    AppTheme {
        QuizScreen(
            uiState = QuizUiState(
                phase = QuizPhase.ANSWERING,
                question = MOCK_QUIZ_RESULTS[0].question,
                selectedAnswer = MOCK_QUIZ_RESULTS[0].selectedAnswer,
                result = MOCK_QUIZ_RESULTS[0],
                totalQuestions = 1,
                startedAt = 0L,
                elapsedTime = 10000L,
            ),
            onContinue = {},
            onAnswerSelected = {},
            onEdit = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview_CorrectResult() {
    AppTheme {
        QuizScreen(
            uiState = QuizUiState(
                phase = QuizPhase.RESULTS,
                question = MOCK_QUIZ_RESULTS[0].question,
                selectedAnswer = MOCK_QUIZ_RESULTS[0].selectedAnswer,
                result = MOCK_QUIZ_RESULTS[0],
                totalQuestions = 1,
                startedAt = 0L,
                elapsedTime = 10000L,
            ),
            onContinue = {},
            onAnswerSelected = {},
            onEdit = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview_IncorrectResult() {
    AppTheme {
        QuizScreen(
            uiState = QuizUiState(
                phase = QuizPhase.RESULTS,
                question = MOCK_QUIZ_RESULTS[1].question,
                selectedAnswer = MOCK_QUIZ_RESULTS[1].selectedAnswer,
                result = MOCK_QUIZ_RESULTS[1],
                totalQuestions = 1,
                startedAt = 0L,
                elapsedTime = 10000L,
            ),
            onContinue = {},
            onAnswerSelected = {},
            onEdit = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview_Invalid() {
    AppTheme {
        QuizScreen(
            uiState = QuizUiState(
                phase = QuizPhase.RESULTS,
                question = MOCK_QUIZ_RESULTS[0].question,
                selectedAnswer = MOCK_QUIZ_RESULTS[0].selectedAnswer,
                result = null,
                totalQuestions = 1,
                startedAt = 0L,
                elapsedTime = 10000L,
            ),
            onContinue = {},
            onAnswerSelected = {},
            onEdit = {}
        )
    }
}