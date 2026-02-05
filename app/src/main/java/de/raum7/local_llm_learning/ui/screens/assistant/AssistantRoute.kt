package de.raum7.local_llm_learning.ui.screens.assistant

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import de.raum7.local_llm_learning.llm.LlmGenerationService

@Composable
fun AssistantRoute(
    navigateToResult: () -> Unit
) {
    val appContext = LocalContext.current.applicationContext

    val viewModel: AssistantViewModel = viewModel(
        factory = AssistantViewModelFactory(
            repository = AssistantRepository(),
            appContext = appContext,
        )
    )

    val state = viewModel.uiState as AssistantUiState
    val activity = LocalActivity.current

    LaunchedEffect(state.generationResult, state.generationError) {
        val hasResult = state.generationResult.isNotBlank()
        val hasError = !state.generationError.isNullOrBlank()

        if (hasResult || hasError) {
            activity?.intent?.putExtra(
                LlmGenerationService.EXTRA_RESULT_TEXT,
                state.generationResult
            )
            activity?.intent?.putExtra(
                LlmGenerationService.EXTRA_RESULT_ERROR,
                state.generationError
            )
            navigateToResult()
        }
    }

    AssistantScreen(
        uiState = state,
        onContinue = viewModel::onContinue,
        onBack = viewModel::onBack,
        onChanged = viewModel::onChanged,
    )
}
