package de.raum7.local_llm_learning.ui.screens.quiz.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import de.raum7.local_llm_learning.ui.shared.components.ButtonColorFillType
import de.raum7.local_llm_learning.ui.shared.components.ButtonStyle
import de.raum7.local_llm_learning.ui.shared.components.CustomCard
import de.raum7.local_llm_learning.ui.shared.components.CustomCardColors
import de.raum7.local_llm_learning.ui.shared.components.CustomElevatedButton
import de.raum7.local_llm_learning.ui.shared.components.InfoTextBlock
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun ResultsPhaseCard(
    result: QuizResult,
    onContinue: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CustomCard(
        colors = CustomCardColors.from(
            outlineColor = when(result.isCorrect) {
                true -> MaterialTheme.colorScheme.secondary
                false -> MaterialTheme.colorScheme.error
            }
        ),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 64.dp)
    ) {
        ResultsPhaseCardContent(result, onEdit, onContinue)
    }

}

@Composable
private fun ResultsPhaseCardContent(result: QuizResult, onEdit: () -> Unit, onContinue: () -> Unit) {
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

        ButtonSection(
            isCorrect = result.isCorrect,
            onEdit = onEdit,
            onContinue = onContinue,
        )
    }
}

@Composable
private fun ButtonSection(
    isCorrect: Boolean,
    onEdit: () -> Unit,
    onContinue: () -> Unit,
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        val buttonClass = when(isCorrect) {
            true -> ButtonClass.SECONDARY
            false -> ButtonClass.ERROR
        }

        CustomElevatedButton(
            label = stringResource(R.string.quiz_edit_button),
            onclick = onEdit,
            style = ButtonStyle(
                buttonClass = buttonClass,
                fillType = ButtonColorFillType.OUTLINE,
            ),
        )

        Spacer(modifier = Modifier.width(8.dp))

        CustomElevatedButton(
            label = stringResource(R.string.quiz_continue),
            onclick = onContinue,
            style = ButtonStyle(
                buttonClass = buttonClass,
                fillType = ButtonColorFillType.FILLED,
            ),
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
            onEdit = {}
        )
    }
}