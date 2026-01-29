package de.raum7.local_llm_learning.ui.screens.assistant

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.screens.assistant.components.BasePhaseCard
import de.raum7.local_llm_learning.ui.screens.assistant.components.FurtherSpecificationForm
import de.raum7.local_llm_learning.ui.screens.assistant.components.InitialDescriptionForm
import de.raum7.local_llm_learning.ui.screens.assistant.components.ParameterSelectionForm
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantPhase
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantUiStateChange
import de.raum7.local_llm_learning.ui.screens.assistant.types.DepthOfTopic
import de.raum7.local_llm_learning.ui.screens.assistant.types.FurtherSpecification
import de.raum7.local_llm_learning.ui.screens.assistant.types.InitialDescription
import de.raum7.local_llm_learning.ui.screens.assistant.types.ParameterSelection
import de.raum7.local_llm_learning.ui.screens.assistant.types.QuestionCount
import de.raum7.local_llm_learning.ui.shared.components.AppBar
import de.raum7.local_llm_learning.ui.shared.components.SelectionUiState
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun AssistantScreen(
    uiState: AssistantUiState,
    onContinue: () -> Unit,
    onChanged: (AssistantUiStateChange) -> Unit,
) {

    Scaffold(
        topBar = { AppBar(title = stringResource(R.string.assistant)) },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        BasePhaseCard(
            isContinueEnabled = uiState.isContinueEnabled,
            onContinue = onContinue,
            modifier = Modifier
                .padding(padding)
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
        ) {
            when (uiState.phase) {
                AssistantPhase.INITIAL_DESCRIPTION ->
                    InitialDescriptionForm(
                        uiState = uiState.initialDescription,
                        onChanged = onChanged
                    )

                AssistantPhase.PARAMETER_SELECTION ->
                    ParameterSelectionForm(
                        uiState = uiState.parameterSelection,
                        onChanged = onChanged,
                    )

                AssistantPhase.FURTHER_SPECIFICATION ->
                    FurtherSpecificationForm(
                        uiState = uiState.furtherSpecification,
                        onChanged = onChanged,
                    )
            }
        }
    }
}

// TODO: implement further preview states

@Preview(showBackground = true)
@Composable
fun AssistantScreenPreview_initial() {
    AppTheme {
        AssistantScreen(
            AssistantUiState(
                phase = DEFAULT_ASSISTANT_PHASE,
                isContinueEnabled = true,
                initialDescription = InitialDescription(
                    filePath = null,
                    prompt = "",
                ),
                parameterSelection = ParameterSelection(
                    questionCount = SelectionUiState(
                        options = QuestionCount.entries.map { it.value.toString() },
                        selected = DEFAULT_QUESTION_COUNT.value.toString(),
                        expanded = false,
                        translations = null,
                    ),
                    depthOfTopic = SelectionUiState(
                        options = DepthOfTopic.entries.map { it.toString() },
                        selected = DEFAULT_DEPTH_OF_TOPIC.toString(),
                        expanded = false,
                        translations = DepthOfTopic.getTranslations(),
                    )
                ),
                furtherSpecification = FurtherSpecification(
                    topicSpecification = "",
                    goal = "",
                )
            ),
            onContinue = {},
            onChanged = {},
        )
    }
}