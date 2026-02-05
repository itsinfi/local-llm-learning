package de.raum7.local_llm_learning.ui.screens.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantPhase
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
private fun Circle(
    color: Color,
    radius: Dp,
) {
    Box(
        modifier = Modifier
            .size(radius * 2)
            .background(color, shape = CircleShape)
    )
}

@Composable
fun PageIndicator(
    phase: AssistantPhase,
    color: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        val selectedColor: Color = color
        val unselectedColor: Color = color.copy(alpha = 0.5f)

        val radius: Dp = 2.dp
        val padding: Dp = 2.dp

        Circle(
            color = when(phase) {
                AssistantPhase.INITIAL_DESCRIPTION -> selectedColor
                else -> unselectedColor
            },
            radius = radius,
        )
        Spacer(Modifier.width(padding))
        Circle(
            color = when(phase) {
                AssistantPhase.PARAMETER_SELECTION -> selectedColor
                else -> unselectedColor
            },
            radius = radius,
        )
        Spacer(Modifier.width(padding))
        Circle(
            color = when(phase) {
                AssistantPhase.FURTHER_SPECIFICATION -> selectedColor
                else -> unselectedColor
            },
            radius = radius,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PageIndicatorPreview() {
    AppTheme {
        PageIndicator(AssistantPhase.PARAMETER_SELECTION)
    }
}