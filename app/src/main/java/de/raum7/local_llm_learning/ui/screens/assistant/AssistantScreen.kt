package de.raum7.local_llm_learning.ui.screens.assistant

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.screens.assistant.components.AssistantCard
import de.raum7.local_llm_learning.ui.screens.assistant.components.FurtherSpecificationForm
import de.raum7.local_llm_learning.ui.screens.assistant.components.InitialDescriptionForm
import de.raum7.local_llm_learning.ui.screens.assistant.components.ParameterSelectionForm
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantCardUiState
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantPhase
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantUiStateChange
import de.raum7.local_llm_learning.ui.screens.assistant.types.DepthOfTopic
import de.raum7.local_llm_learning.ui.screens.assistant.types.FurtherSpecificationUiState
import de.raum7.local_llm_learning.ui.screens.assistant.types.InitialDescriptionUiState
import de.raum7.local_llm_learning.ui.screens.assistant.types.ParameterSelectionUiState
import de.raum7.local_llm_learning.ui.screens.assistant.types.QuestionCount
import de.raum7.local_llm_learning.ui.shared.components.AppBar
import de.raum7.local_llm_learning.ui.shared.components.SelectionUiState
import de.raum7.local_llm_learning.ui.theme.AppTheme


@Composable
fun AssistantScreen(
    uiState: AssistantUiState,
    onContinue: () -> Unit,
    onBack: () -> Unit,
    onChanged: (AssistantUiStateChange) -> Unit,
) {
    if (uiState.isGenerating) {
        BackHandler(enabled = true) { }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            topBar = { AppBar(title = stringResource(R.string.assistant)) },
            modifier = Modifier.fillMaxSize(),
        ) { padding ->
            AssistantCard(
                uiState = uiState.assistantCard,
                onContinue = {
                    if (!uiState.isGenerating) onContinue()
                },
                onBack = {
                    if (!uiState.isGenerating) onBack()
                },
                modifier = Modifier
                    .padding(padding)
                    .padding(top = 64.dp)
                    .padding(horizontal = 16.dp),
            ) {
                when (uiState.assistantCard.phase) {
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

        if (uiState.isGenerating) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f))
                    .clickable(
                        enabled = true,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        // absichtlich leer: blockiert alle Klicks
                    }
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.assistant_generating),
                        color = Color.White
                    )

                    Text(
                        text = stringResource(R.string.assistant_please_wait),
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AssistantScreenPreview_InitialDescriptionEmpty() {
    AppTheme {
        AssistantScreen(
            AssistantUiState(
                assistantCard = AssistantCardUiState(
                    phase = AssistantPhase.INITIAL_DESCRIPTION,
                    isContinueEnabled = false,
                ),
                initialDescription = InitialDescriptionUiState(
                    filePath = null,
                    prompt = "",
                ),
                parameterSelection = ParameterSelectionUiState(
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
                furtherSpecification = FurtherSpecificationUiState(
                    topicSpecification = "",
                    goal = "",
                ),
                isGenerating = false,
            ),
            onContinue = {},
            onBack = {},
            onChanged = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AssistantScreenPreview_InitialDescriptionFilled() {
    AppTheme {
        AssistantScreen(
            AssistantUiState(
                assistantCard = AssistantCardUiState(
                    phase = AssistantPhase.INITIAL_DESCRIPTION,
                    isContinueEnabled = true,
                ),
                initialDescription = InitialDescriptionUiState(
                    filePath = "lorem-ipsum/dolor-sit-amet-consectetur-adipiscing-elit-sed.pdf".toUri(),
                    prompt = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                ),
                parameterSelection = ParameterSelectionUiState(
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
                furtherSpecification = FurtherSpecificationUiState(
                    topicSpecification = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    goal = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                ),
                isGenerating = false,
            ),
            onContinue = {},
            onBack = {},
            onChanged = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AssistantScreenPreview_ParameterSelection() {
    AppTheme {
        AssistantScreen(
            AssistantUiState(
                assistantCard = AssistantCardUiState(
                    phase = AssistantPhase.PARAMETER_SELECTION,
                    isContinueEnabled = true,
                ),
                initialDescription = InitialDescriptionUiState(
                    filePath = null,
                    prompt = "",
                ),
                parameterSelection = ParameterSelectionUiState(
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
                furtherSpecification = FurtherSpecificationUiState(
                    topicSpecification = "",
                    goal = "",
                ),
                isGenerating = false,
            ),
            onContinue = {},
            onBack = {},
            onChanged = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AssistantScreenPreview_FurtherSpecificationEmpty() {
    AppTheme {
        AssistantScreen(
            AssistantUiState(
                assistantCard = AssistantCardUiState(
                    phase = AssistantPhase.FURTHER_SPECIFICATION,
                    isContinueEnabled = true,
                ),
                initialDescription = InitialDescriptionUiState(
                    filePath = null,
                    prompt = "",
                ),
                parameterSelection = ParameterSelectionUiState(
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
                furtherSpecification = FurtherSpecificationUiState(
                    topicSpecification = "",
                    goal = "",
                ),
                isGenerating = false,
            ),
            onContinue = {},
            onBack = {},
            onChanged = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AssistantScreenPreview_FurtherSpecificationFilled() {
    AppTheme {
        AssistantScreen(
            AssistantUiState(
                assistantCard = AssistantCardUiState(
                    phase = AssistantPhase.FURTHER_SPECIFICATION,
                    isContinueEnabled = true,
                ),
                initialDescription = InitialDescriptionUiState(
                    filePath = "lorem-ipsum/dolor-sit-amet-consectetur-adipiscing-elit-sed.pdf".toUri(),
                    prompt = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                ),
                parameterSelection = ParameterSelectionUiState(
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
                furtherSpecification = FurtherSpecificationUiState(
                    topicSpecification = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    goal = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                ),
                isGenerating = false,
            ),
            onContinue = {},
            onBack = {},
            onChanged = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AssistantScreenPreview_Generating() {
    AppTheme {
        AssistantScreen(
            AssistantUiState(
                assistantCard = AssistantCardUiState(
                    phase = AssistantPhase.FURTHER_SPECIFICATION,
                    isContinueEnabled = true,
                ),
                initialDescription = InitialDescriptionUiState(
                    filePath = "lorem-ipsum/dolor-sit-amet-consectetur-adipiscing-elit-sed.pdf".toUri(),
                    prompt = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                ),
                parameterSelection = ParameterSelectionUiState(
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
                furtherSpecification = FurtherSpecificationUiState(
                    topicSpecification = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    goal = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                ),
                isGenerating = true,
            ),
            onContinue = {},
            onBack = {},
            onChanged = {},
        )
    }
}
