package de.raum7.local_llm_learning.ui.screens.assistant.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantUiStateChange
import de.raum7.local_llm_learning.ui.screens.assistant.types.FurtherSpecification
import de.raum7.local_llm_learning.ui.shared.components.TextInput
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun FurtherSpecificationForm(
    uiState: FurtherSpecification,
    onChanged: (AssistantUiStateChange) -> Unit,
) {
    // topic specification
    TextInput(
        title = stringResource(R.string.assistant_topic_specification),
        placeholder = stringResource(R.string.assistant_topic_specification_placeholder),
        value = uiState.topicSpecification,
        onValueChange = { value -> onChanged(AssistantUiStateChange(topicSpecification = value)) },
    )

    // goal
    TextInput(
        title = stringResource(R.string.assistant_goal),
        placeholder = stringResource(R.string.assistant_goal_placeholder),
        value = uiState.goal,
        onValueChange = { value -> onChanged(AssistantUiStateChange(goal = value)) },
    )
}

@Preview(showBackground = true)
@Composable
fun FurtherSpecificationFormPreview_Empty() {
    AppTheme {
        Column {
            FurtherSpecificationForm(
                uiState = FurtherSpecification(
                    topicSpecification = "",
                    goal = "",
                ),
                onChanged = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FurtherSpecificationFormPreview_Filled() {
    AppTheme {
        Column {
            FurtherSpecificationForm(
                uiState = FurtherSpecification(
                    topicSpecification = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    goal = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                ),
                onChanged = {},
            )
        }
    }
}