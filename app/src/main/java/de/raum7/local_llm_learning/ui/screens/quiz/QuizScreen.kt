package de.raum7.local_llm_learning.ui.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.data.mock.MOCK_ANSWERS
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.mock.MOCK_QUIZ_RESULTS
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.ui.screens.quiz.components.AnsweringPhaseCard
import de.raum7.local_llm_learning.ui.shared.components.AppBar
import de.raum7.local_llm_learning.ui.shared.components.EmptyPlaceholder
import de.raum7.local_llm_learning.ui.screens.quiz.components.ResultsPhaseCard
import de.raum7.local_llm_learning.ui.screens.quiz.types.QuizPhase
import de.raum7.local_llm_learning.ui.shared.components.ProgressBar
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun QuizScreen(
    uiState: QuizUiState,
    onAnswerSelected: (Answer) -> Unit,
    onContinue: () -> Unit,
    onEdit: () -> Unit
) {
    Scaffold(
        topBar = { AppBar(title = uiState.learningMaterial.title) },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            ProgressBar(
                perc = uiState.learningMaterial.progress.toFloat(),
                size = 10.dp,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
            )

            when (uiState.phase) {
                QuizPhase.ANSWERING -> AnsweringPhaseCard(
                    question = uiState.question,
                    answers = uiState.answers,
                    selectedAnswer = uiState.selectedAnswer,
                    elapsedTime = uiState.elapsedTime,
                    onAnswerSelected,
                    onContinue,
                )

                QuizPhase.RESULTS -> when (uiState.result) {
                    null -> EmptyPlaceholder(stringResource(R.string.quiz_error_invalid_result))
                    else -> ResultsPhaseCard(uiState.result, onContinue, onEdit)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizScreenPreview_Answering() {
    AppTheme {
        QuizScreen(
            uiState = QuizUiState(
                learningMaterial = MOCK_LEARNING_MATERIALS[0],
                phase = QuizPhase.ANSWERING,
                question = MOCK_QUIZ_RESULTS[0].question,
                answers = listOf(MOCK_ANSWERS[0], MOCK_ANSWERS[1], MOCK_ANSWERS[2],MOCK_ANSWERS[3]),
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
private fun QuizScreenPreview_CorrectResult() {
    AppTheme {
        QuizScreen(
            uiState = QuizUiState(
                learningMaterial = MOCK_LEARNING_MATERIALS[0],
                phase = QuizPhase.RESULTS,
                question = MOCK_QUIZ_RESULTS[0].question,
                answers = listOf(MOCK_ANSWERS[0], MOCK_ANSWERS[1], MOCK_ANSWERS[2],MOCK_ANSWERS[3]),
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
private fun QuizScreenPreview_IncorrectResult() {
    AppTheme {
        QuizScreen(
            uiState = QuizUiState(
                learningMaterial = MOCK_LEARNING_MATERIALS[0],
                phase = QuizPhase.RESULTS,
                question = MOCK_QUIZ_RESULTS[1].question,
                answers = listOf(MOCK_ANSWERS[0], MOCK_ANSWERS[1], MOCK_ANSWERS[2],MOCK_ANSWERS[3]),
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
private fun QuizScreenPreview_Invalid() {
    AppTheme {
        QuizScreen(
            uiState = QuizUiState(
                learningMaterial = MOCK_LEARNING_MATERIALS[0],
                phase = QuizPhase.RESULTS,
                question = MOCK_QUIZ_RESULTS[0].question,
                answers = listOf(MOCK_ANSWERS[0], MOCK_ANSWERS[1], MOCK_ANSWERS[2],MOCK_ANSWERS[3]),
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