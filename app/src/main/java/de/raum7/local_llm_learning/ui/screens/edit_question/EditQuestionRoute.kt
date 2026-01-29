package de.raum7.local_llm_learning.ui.screens.edit_question

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.ui.screens.edit_question.EditQuestionUiState

@Composable
fun EditQuestionRoute(
    learningMaterialId: String,
    questionId: String
) {
    val data = MOCK_LEARNING_MATERIALS

    val viewModel: EditQuestionViewModel = viewModel(
        factory = EditQuestionViewModelFactory(
            learningMaterialId = learningMaterialId,
            questionId = questionId,
            repository = EditQuestionRepository(data)
        )
    )

    EditQuestionScreen(
        uiState = viewModel.uiState as EditQuestionUiState,
        onEditableAnswerSelected = viewModel::onEditableAnswerSelected,
    )
}