package de.raum7.local_llm_learning.ui.screens.quiz.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.data.mock.MOCK_QUIZ_RESULTS
import de.raum7.local_llm_learning.data.models.QuizResult
import de.raum7.local_llm_learning.ui.shared.components.ButtonClass
import de.raum7.local_llm_learning.ui.shared.components.ButtonStatus
import de.raum7.local_llm_learning.ui.shared.components.CustomElevatedButton
import de.raum7.local_llm_learning.ui.shared.components.InfoTextBlock
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun ResultsPhaseCard(
    result: QuizResult,
    onContinue: () -> Unit,
    onEdit: () -> Unit,
    padding: PaddingValues,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = when (result.isCorrect) {
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
            ResultsHeader(result)

            InfoTextBlock(
                title = stringResource(R.string.quiz_actual_answer),
                content = result.correctAnswer.answer,
            )

            InfoTextBlock(
                title = stringResource(R.string.quiz_your_answer),
                content = result.selectedAnswer.answer,
            )
            ButtonSection(isError = !result.isCorrect, onEdit, onContinue)
        }
    }
}

@Composable
fun ButtonSection(isError: Boolean, onEdit: () -> Unit, onContinue: () -> Unit) {
    val buttonStatus: ButtonStatus = when(isError) {
        false -> ButtonStatus.SUCCESS
        true -> ButtonStatus.ERROR
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        CustomElevatedButton(
            label = stringResource(R.string.quiz_edit_button),
            onclick = onEdit,
            buttonStatus = buttonStatus,
            buttonClass = ButtonClass.SECONDARY,
        )

        Spacer(modifier = Modifier.width(8.dp))

        CustomElevatedButton(
            label = stringResource(R.string.quiz_continue),
            onclick = onContinue,
            buttonStatus = buttonStatus,
            buttonClass = ButtonClass.PRIMARY,
        )
    }
}

@Preview(showBackground = false)
@Composable
fun ResultsPhaseCardPreview_CorrectAnswer() {
    AppTheme {
        ResultsPhaseCard(
            result = MOCK_QUIZ_RESULTS[0],
            onContinue = {},
            padding = PaddingValues.Zero,
            onEdit = {}
        )
    }
}

@Preview(showBackground = false)
@Composable
fun ResultsPhaseCardPreview_IncorrectAnswer() {
    AppTheme {
        ResultsPhaseCard(
            result = MOCK_QUIZ_RESULTS[1],
            onContinue = {},
            padding = PaddingValues.Zero,
            onEdit = {}
        )
    }
}