package de.raum7.local_llm_learning.ui.screens.quiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.store.LearningMaterialStore
import de.raum7.local_llm_learning.ui.shared.components.EmptyPlaceholder

@Composable
fun QuizRoute(
    learningMaterialId: String,
    navigateToEditQuestionCallback: (String, String) -> Unit,
) {
    /*val data by LearningMaterialStore.items.collectAsState(initial = emptyList())

    val materialExists = data.any { it.id == learningMaterialId }
    if (!materialExists) {
        androidx.compose.material3.Text("Lernmaterial nicht gefunden")
        return
    }*/

    val data = MOCK_LEARNING_MATERIALS // TODO: remove


    val viewModel: QuizViewModel = viewModel(
        factory = QuizViewModelFactory(
            learningMaterialId = learningMaterialId,
            repository = QuizRepository(data),
        )
    )

    val quizUiState = viewModel.uiState as QuizUiState

    val onEdit = {
        navigateToEditQuestionCallback(learningMaterialId, quizUiState.question.id)
    }

    QuizScreen(
        uiState = quizUiState,
        onAnswerSelected = viewModel::onAnswerSelected,
        onContinue = viewModel::onContinue,
        onEdit = onEdit
    )
}
