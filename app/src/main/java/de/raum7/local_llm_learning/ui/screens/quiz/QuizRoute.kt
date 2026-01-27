package de.raum7.local_llm_learning.ui.screens.quiz

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS

@Composable
fun QuizRoute(
    learningMaterialId: String,
) {
    val data = MOCK_LEARNING_MATERIALS

    val viewModel: QuizViewModel = viewModel(
        factory = QuizViewModelFactory(
            learningMaterialId = learningMaterialId,
            repository = QuizRepository(data),
        )
    )

    QuizScreen(
        uiState = viewModel.uiState as QuizUiState,
        onAnswerSelected = viewModel::onAnswerSelected,
        onContinue = viewModel::onContinue,
    )
}