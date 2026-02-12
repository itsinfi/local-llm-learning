package de.raum7.local_llm_learning.ui.screens.edit_question

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import de.raum7.local_llm_learning.data.database.dao.AnswerDao
import de.raum7.local_llm_learning.data.database.dao.QuestionDao
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS

@Composable
fun EditQuestionRoute(
    learningMaterialId: Int,
    questionId: Int,
    questionDao: QuestionDao,
    answerDao: AnswerDao,
) {

    val viewModel: EditQuestionViewModel = viewModel(
        factory = EditQuestionViewModelFactory(
            learningMaterialId = learningMaterialId,
            questionId = questionId,
            repository = EditQuestionRepository(questionDao, answerDao)
        )
    )

    EditQuestionScreen(
        uiState = viewModel.uiState as EditQuestionUiState,
        onEditableAnswerSelected = viewModel::onEditableAnswerSelected,
        onQuestionSave = viewModel::onQuestionSave,
    )
}