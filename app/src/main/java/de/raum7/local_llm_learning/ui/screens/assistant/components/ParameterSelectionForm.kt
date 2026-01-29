package de.raum7.local_llm_learning.ui.screens.assistant.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.screens.assistant.DEFAULT_DEPTH_OF_TOPIC
import de.raum7.local_llm_learning.ui.screens.assistant.DEFAULT_QUESTION_COUNT
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantUiStateChange
import de.raum7.local_llm_learning.ui.screens.assistant.types.DepthOfTopic
import de.raum7.local_llm_learning.ui.screens.assistant.types.ParameterSelection
import de.raum7.local_llm_learning.ui.screens.assistant.types.QuestionCount
import de.raum7.local_llm_learning.ui.shared.components.SelectionInput
import de.raum7.local_llm_learning.ui.shared.components.SelectionUiState
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun ParameterSelectionForm(
    uiState: ParameterSelection,
    onChanged: (AssistantUiStateChange) -> Unit,
) {
    val translatedDepthOfTopicUiState = uiState.depthOfTopic.copy(
        translations = DepthOfTopic.getTranslations(),
    )

    // question count
    SelectionInput(
        title = stringResource(R.string.assistant_question_count),
        placeholder = stringResource(R.string.assistant_question_count_placeholder),
        uiState = uiState.questionCount,
        onSelected = { selected: String ->
            onChanged(
                AssistantUiStateChange(
                    questionCount = QuestionCount.fromValue(selected),
                    questionCountExpanded = false,
                )
            )
        },
        onExpandedChange = { expanded: Boolean ->
            onChanged(
                AssistantUiStateChange(
                    questionCountExpanded = expanded
                )
            )
        },
    )

    // depth of topic
    SelectionInput(
        title = stringResource(R.string.assistant_depth_of_topic),
        placeholder = stringResource(R.string.assistant_depth_of_topic_placeholder),
        uiState = translatedDepthOfTopicUiState,
        onSelected = { selected: String ->
            onChanged(
                AssistantUiStateChange(
                    depthOfTopic = DepthOfTopic.fromString(selected),
                    depthOfTopicExpanded = false,
                )
            )
        },
        onExpandedChange = { expanded: Boolean ->
            onChanged(
                AssistantUiStateChange(
                    depthOfTopicExpanded = expanded
                )
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun ParameterSelectionFormPreview() {
    AppTheme {
        Column {
            ParameterSelectionForm(
                uiState = ParameterSelection(
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
                onChanged = {},
            )
        }
    }
}