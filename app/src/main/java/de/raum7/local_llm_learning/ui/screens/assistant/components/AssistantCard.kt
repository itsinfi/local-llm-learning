package de.raum7.local_llm_learning.ui.screens.assistant.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantCardUiState
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantPhase
import de.raum7.local_llm_learning.ui.shared.components.ButtonClass
import de.raum7.local_llm_learning.ui.shared.components.CustomElevatedButton
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun AssistantCard(
    uiState: AssistantCardUiState,
    onContinue: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val containerColor = MaterialTheme.colorScheme.surfaceContainer
    val contentColor = MaterialTheme.colorScheme.onSurface

    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Title(uiState.phase)
            content()
            ButtonSection(uiState, onContinue, onBack)
            PageIndicator(uiState.phase)
        }
    }
}

@Composable
private fun Title(phase: AssistantPhase) {
    Text(
        text = stringResource(
            when (phase) {
                AssistantPhase.INITIAL_DESCRIPTION -> R.string.assistant_initial_description
                AssistantPhase.PARAMETER_SELECTION -> R.string.assistant_parameter_selection
                AssistantPhase.FURTHER_SPECIFICATION -> R.string.assistant_further_specification
            }
        ),
        style = MaterialTheme.typography.headlineSmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(bottom = 32.dp)
    )
}

@Composable
private fun ButtonSection(
    uiState: AssistantCardUiState,
    onContinue: () -> Unit,
    onBack: () -> Unit,
) {
    val modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)

    val canNavigate = !uiState.isLocked
    val canContinue = uiState.isContinueEnabled && canNavigate

    when (uiState.phase) {
        AssistantPhase.INITIAL_DESCRIPTION ->
            Box(modifier) {
                CustomElevatedButton(
                    label = stringResource(R.string.assistant_continue),
                    isEnabled = canContinue,
                    onclick = onContinue,
                    // buttonClass = ButtonClass.PRIMARY, TODO:
                    modifier = Modifier.align(Alignment.CenterEnd),
                )
            }

        AssistantPhase.FURTHER_SPECIFICATION ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = modifier,
            ) {
                CustomElevatedButton(
                    label = stringResource(R.string.assistant_back),
                    isEnabled = canNavigate,
                    onclick = onBack,
                    // buttonClass = ButtonClass.SECONDARY, TODO:
                )

                Spacer(Modifier.width(8.dp))

                CustomElevatedButton(
                    label = stringResource(R.string.assistant_generate),
                    isEnabled = canContinue,
                    onclick = onContinue,
                    // buttonClass = ButtonClass.PRIMARY, TODO:
                )
            }

        else ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = modifier,
            ) {
                CustomElevatedButton(
                    label = stringResource(R.string.assistant_back),
                    isEnabled = canNavigate,
                    onclick = onBack,
                    // buttonClass = ButtonClass.SECONDARY, TODO:
                )

                Spacer(Modifier.width(8.dp))

                CustomElevatedButton(
                    label = stringResource(R.string.assistant_continue),
                    isEnabled = canContinue,
                    onclick = onContinue,
                    // buttonClass = ButtonClass.PRIMARY, TODO:
                )
            }
    }
}

@Preview(showBackground = false)
@Composable
fun AssistantCardPreview_InitialDescriptionEnabled() {
    AppTheme {
        AssistantCard(
            uiState = AssistantCardUiState(
                phase = AssistantPhase.INITIAL_DESCRIPTION,
                isContinueEnabled = true,
                isLocked = false,
            ),
            onContinue = {},
            onBack = {},
        ) {
            Text("ITEM 1")
            Text("ITEM 2")
        }
    }
}

@Preview(showBackground = false)
@Composable
fun AssistantCardPreview_InitialDescriptionDisabled() {
    AppTheme {
        AssistantCard(
            uiState = AssistantCardUiState(
                phase = AssistantPhase.INITIAL_DESCRIPTION,
                isContinueEnabled = false,
                isLocked = false,
            ),
            onContinue = {},
            onBack = {},
        ) {
            Text("ITEM 1")
            Text("ITEM 2")
        }
    }
}

@Preview(showBackground = false)
@Composable
fun AssistantCardPreview_ParameterSelectionEnabled() {
    AppTheme {
        AssistantCard(
            uiState = AssistantCardUiState(
                phase = AssistantPhase.PARAMETER_SELECTION,
                isContinueEnabled = true,
                isLocked = false,
            ),
            onContinue = {},
            onBack = {},
        ) {
            Text("ITEM 1")
            Text("ITEM 2")
        }
    }
}

@Preview(showBackground = false)
@Composable
fun AssistantCardPreview_ParameterSelectionDisabled() {
    AppTheme {
        AssistantCard(
            uiState = AssistantCardUiState(
                phase = AssistantPhase.PARAMETER_SELECTION,
                isContinueEnabled = false,
                isLocked = false,
            ),
            onContinue = {},
            onBack = {},
        ) {
            Text("ITEM 1")
            Text("ITEM 2")
        }
    }
}

@Preview(showBackground = false)
@Composable
fun AssistantCardPreview_FurtherSpecificationEnabled() {
    AppTheme {
        AssistantCard(
            uiState = AssistantCardUiState(
                phase = AssistantPhase.FURTHER_SPECIFICATION,
                isContinueEnabled = true,
                isLocked = false,
            ),
            onContinue = {},
            onBack = {},
        ) {
            Text("ITEM 1")
            Text("ITEM 2")
        }
    }
}

@Preview(showBackground = false)
@Composable
fun AssistantCardPreview_FurtherDescriptionDisabled() {
    AppTheme {
        AssistantCard(
            uiState = AssistantCardUiState(
                phase = AssistantPhase.FURTHER_SPECIFICATION,
                isContinueEnabled = false,
                isLocked = false,
            ),
            onContinue = {},
            onBack = {},
        ) {
            Text("ITEM 1")
            Text("ITEM 2")
        }
    }
}
