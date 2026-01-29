package de.raum7.local_llm_learning.ui.screens.assistant.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.shared.components.CustomElevatedButton
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun BasePhaseCard(
    modifier: Modifier = Modifier,
    isContinueEnabled: Boolean,
    onContinue: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val containerColor = MaterialTheme.colorScheme.secondaryContainer
    val contentColor = MaterialTheme.colorScheme.onSecondaryContainer

    Card(
        colors = CardDefaults.cardColors().copy(
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
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                content = content,
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                CustomElevatedButton(
                    label = stringResource(R.string.quiz_continue),
                    isEnabled = isContinueEnabled,
                    onclick = onContinue,
                    modifier = Modifier.align(Alignment.CenterEnd),
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun BasePhaseCardPreview_Enabled() {
    AppTheme {
        BasePhaseCard(
            isContinueEnabled = true,
            onContinue = {}
        ) {
            Text("ITEM 1")
            Text("ITEM 2")
        }
    }
}

@Preview(showBackground = false)
@Composable
fun BasePhaseCardPreview_Disabled() {
    AppTheme {
        BasePhaseCard(
            isContinueEnabled = false,
            onContinue = {}
        ) {
            Text("ITEM 1")
            Text("ITEM 2")
        }
    }
}