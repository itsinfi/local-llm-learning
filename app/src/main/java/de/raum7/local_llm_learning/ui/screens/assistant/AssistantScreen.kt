package de.raum7.local_llm_learning.ui.screens.assistant

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.screens.assistant.components.BasePhaseCard
import de.raum7.local_llm_learning.ui.screens.assistant.components.SelectionInput
import de.raum7.local_llm_learning.ui.screens.assistant.components.TextInput
import de.raum7.local_llm_learning.ui.shared.components.AppBar
import de.raum7.local_llm_learning.ui.theme.AppTheme

// TODO: move phase cards to new files

@Composable
fun AssistantScreen(
    uiState: AssistantUiState,
    onContinue: () -> Unit,
    onChanged: (AssistantUiStateChange) -> Unit,
    onSelected: (String, AssistantSelectionUiStateType) -> Unit,
    onExpandedChange: (Boolean, AssistantSelectionUiStateType) -> Unit,
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
                    InitialDescriptionCard(uiState, onChanged)

                AssistantPhase.PARAMETER_SELECTION ->
                    ParameterSelectionCard(uiState, onSelected, onExpandedChange)

                AssistantPhase.FURTHER_SPECIFICATION ->
                    FurtherSpecificationCard(uiState, onChanged)
            }
        }
    }
}

@Composable
fun InitialDescriptionCard(
    uiState: AssistantUiState,
    onChanged: (AssistantUiStateChange) -> Unit,
) {
    // TODO: implement file input component
    Text("FILE INPUT PLACEHOLDER", modifier = Modifier.padding(bottom = 16.dp))

    TextInput(
        title = stringResource(R.string.assistant_prompt),
        placeholder = stringResource(R.string.assistant_prompt_placeholder),
        value = uiState.prompt,
        onValueChange = { value -> onChanged(AssistantUiStateChange(prompt = value)) },
    )
}

@Composable
fun ParameterSelectionCard(
    uiState: AssistantUiState,
    onSelected: (String, AssistantSelectionUiStateType) -> Unit,
    onExpandedChange: (Boolean, AssistantSelectionUiStateType) -> Unit,
) {
    SelectionInput(
        title = stringResource(R.string.assistant_question_count),
        placeholder = stringResource(R.string.assistant_question_count_placeholder),
        type = AssistantSelectionUiStateType.QUESTION_COUNT,
        options = uiState.questionCountSelectionUiState.options,
        selected = uiState.questionCountSelectionUiState.selected,
        expanded = uiState.questionCountSelectionUiState.expanded,
        onSelected = onSelected,
        onExpandedChange = onExpandedChange,
    )

    SelectionInput(
        title = stringResource(R.string.assistant_depth_of_topic),
        placeholder = stringResource(R.string.assistant_depth_of_topic_placeholder),
        type = AssistantSelectionUiStateType.DEPTH_OF_TOPIC,
        options = uiState.depthOfTopicSelectionUiState.options,
        selected = uiState.depthOfTopicSelectionUiState.selected,
        expanded = uiState.depthOfTopicSelectionUiState.expanded,
        onSelected = onSelected,
        onExpandedChange = onExpandedChange,
    )
}

@Composable
fun FurtherSpecificationCard(
    uiState: AssistantUiState,
    onChanged: (AssistantUiStateChange) -> Unit,
) {
    TextInput(
        title = stringResource(R.string.assistant_topic_specification),
        placeholder = stringResource(R.string.assistant_topic_specification_placeholder),
        value = uiState.topicSpecification,
        onValueChange = { value -> onChanged(AssistantUiStateChange(topicSpecification = value)) },
    )

    TextInput(
        title = stringResource(R.string.assistant_goal),
        placeholder = stringResource(R.string.assistant_goal_placeholder),
        value = uiState.goal,
        onValueChange = { value -> onChanged(AssistantUiStateChange(goal = value)) },
    )
}

// TODO: implement further preview states

@Preview(showBackground = true)
@Composable
fun AssistantScreenPreview_initial() {
    AppTheme {
        AssistantScreen(
            AssistantUiState(
                phase = DEFAULT_ASSISTANT_PHASE,
                filePath = null,
                prompt = "",
                questionCount = DEFAULT_QUESTION_COUNT,
                depthOfTopic = DEFAULT_DEPTH_OF_TOPIC,
                topicSpecification = "",
                goal = "",
                isContinueEnabled = true,
                questionCountSelectionUiState = AssistantSelectionUiState(
                    options = listOf("Option 1", "Option 2", "Option 3"),
                    selected = "Option 1",
                    expanded = false,
                    type = AssistantSelectionUiStateType.QUESTION_COUNT,
                ),
                depthOfTopicSelectionUiState = AssistantSelectionUiState(
                    options = listOf("Option 1", "Option 2", "Option 3"),
                    selected = "Option 1",
                    expanded = false,
                    type = AssistantSelectionUiStateType.DEPTH_OF_TOPIC,
                )
            ),
            onContinue = {},
            onChanged = {},
            onSelected = { _: String, _: AssistantSelectionUiStateType -> },
            onExpandedChange = { _: Boolean, _: AssistantSelectionUiStateType -> },
        )
    }
}