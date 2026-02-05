package de.raum7.local_llm_learning.ui.screens.assistant

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AssistantRoute() {
    val viewModel: AssistantViewModel = viewModel(
        factory = AssistantViewModelFactory(
            repository = AssistantRepository(),
        )
    )

    AssistantScreen(
        uiState = viewModel.uiState as AssistantUiState,
        onContinue = viewModel::onContinue,
        onBack = viewModel::onBack,
        onChanged = viewModel::onChanged,
    )
}