package de.raum7.local_llm_learning.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.data.models.Question
import de.raum7.local_llm_learning.data.models.QuizResult
import de.raum7.local_llm_learning.ui.components.AppBar
import de.raum7.local_llm_learning.ui.components.EmptyPlaceholder
import de.raum7.local_llm_learning.ui.theme.AppTheme
import java.util.UUID

// TODO: seperate into new files/components
enum class QuizPhase {
    ANSWERING,
    RESULTS,
}

@Composable
fun SpacedRepetitionAlgorithmSingleChoiceQuestionBasedQuiz(
    learningMaterial: LearningMaterial
) {
    var index by remember { mutableIntStateOf(0) }
    var currentPhase: QuizPhase by remember { mutableStateOf(QuizPhase.ANSWERING) }

    val selectedQuestion: Question = learningMaterial.questions[index]
    var selectedAnswer: Answer? by remember { mutableStateOf(null) }
    var currentResult: QuizResult? by remember { mutableStateOf(null) }

    val onPhaseChange = {
       when (currentPhase) {
           QuizPhase.ANSWERING -> {
               currentPhase = QuizPhase.RESULTS

               val correctAnswer: Answer? = selectedQuestion.answers.firstOrNull { answer: Answer ->
                   answer.isCorrect
               }

               if (correctAnswer == null || selectedAnswer == null) {
                   error("NO CORRECT ANSWER FOUND") // TODO: improve error handling
               } else {
                   currentResult = QuizResult(
                       id = UUID.randomUUID().toString(),
                       question = selectedQuestion,
                       isCorrect = selectedAnswer == correctAnswer,
                       selectedAnswer = selectedAnswer!!,
                       correctAnswer = correctAnswer,
                   )
               }
           }

           QuizPhase.RESULTS -> {
               currentPhase = QuizPhase.ANSWERING

               selectedAnswer = null
               currentResult = null

               if (index < learningMaterial.questions.size - 1) {
                   index += 1
               } else {
                   index = 0
               }
           }
       }
    }

    val onAnswerSelection = { answer: Answer -> selectedAnswer = answer }

    Scaffold(
        topBar = { AppBar(title = stringResource(R.string.srascqbq)) },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        when (currentPhase) {
            QuizPhase.ANSWERING -> AnsweringPhaseCard(
                selectedQuestion,
                selectedAnswer,
                onAnswerSelection,
                onPhaseChange,
                padding
            )

            QuizPhase.RESULTS -> when (currentResult) {
                null -> EmptyPlaceholder(stringResource(R.string.srascqbq_error_invalid_result), padding)
                else -> ResultsPhaseCard(currentResult!!, onPhaseChange, padding)
            }
        }
    }
}

@Composable
fun ResultsPhaseCard(currentResult: QuizResult, onPhaseChange: () -> Unit, padding: PaddingValues) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = when (currentResult.isCorrect) {
                true -> MaterialTheme.colorScheme.primary
                false -> MaterialTheme.colorScheme.errorContainer
            }
        ),
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 16.dp)
            .padding(top = 64.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(16.dp)
        ) {
            ResultsHeader(currentResult.isCorrect)

            InfoText(
                header = stringResource(R.string.srascqbq_actual_answer),
                body = currentResult.correctAnswer.answer,
            )

            InfoText(
                header = stringResource(R.string.srascqbq_your_answer),
                body = currentResult.selectedAnswer.answer,
            )

            ContinueButton(
                isContinueButtonEnabled = true,
                onclick = onPhaseChange,
                isError = !currentResult.isCorrect,
            )
        }
    }
}

@Composable
fun ResultsHeader(isCorrect: Boolean) {
    val text = when (isCorrect) {
        true -> stringResource(R.string.srascqbq_correct_result)
        false -> stringResource(R.string.srascqbq_incorrect_result)
    }

    val imageVector = when(isCorrect) {
        true -> Icons.Default.CheckCircle
        false -> Icons.Default.Error
    }

    val tint = when(isCorrect) {
        true -> MaterialTheme.colorScheme.primaryContainer
        false -> MaterialTheme.colorScheme.error
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(bottom = 32.dp)
            .fillMaxWidth(),
    ) {
        Icon(
            imageVector,
            contentDescription = text,
            tint = tint,
        )
        Text(
            text,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun InfoText(header: String, body: String) {
    Text(
        text = header,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
    Text(
        text = body,
        style = MaterialTheme.typography.bodyLarge,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun AnsweringPhaseCard(
    selectedQuestion: Question,
    selectedAnswer: Answer?,
    onAnswerSelection: (Answer) -> Unit,
    onPhaseChange: () -> Unit,
    padding: PaddingValues
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 16.dp)
            .padding(top = 64.dp),
    ) {
        LazyColumn (
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(16.dp)
        ) {
            item {
                QuestionTitle(selectedQuestion)
            }

            items(selectedQuestion.answers) { answer: Answer ->
                AnswerSelection(answer, isSelected = answer == selectedAnswer, onClick = { onAnswerSelection(answer) })
            }

            item {
                ContinueButton(
                    isContinueButtonEnabled = selectedAnswer != null,
                    onclick = onPhaseChange
                )
            }
        }
    }
}

@Composable
fun QuestionTitle(question: Question) {
    Text(
        text = question.question,
        style = MaterialTheme.typography.headlineMedium,
        maxLines = 4,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(bottom = 32.dp)
    )
}

@Composable
fun AnswerSelection(answer: Answer, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton
            )
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = answer.answer,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun ContinueButton(
    isContinueButtonEnabled: Boolean,
    onclick: () -> Unit,
    isError: Boolean = false,
) {
    val colors: ButtonColors = when (isError) {
        true -> ButtonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
            disabledContainerColor = MaterialTheme.colorScheme.onErrorContainer,
            disabledContentColor = MaterialTheme.colorScheme.errorContainer,
        )
        false -> ButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ElevatedButton(
            onclick,
            enabled = isContinueButtonEnabled,
            colors = colors,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Text(text = stringResource(R.string.srascqbq_continue))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpacedRepetitionAlgorithmSingleChoiceQuestionBasedQuizPreview() {
    AppTheme {
        SpacedRepetitionAlgorithmSingleChoiceQuestionBasedQuiz(
            learningMaterial = MOCK_LEARNING_MATERIALS[1]
        )
    }
}