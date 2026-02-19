package de.raum7.local_llm_learning.ui.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun CustomRadioButton(isSelected: Boolean) {
    val selectedColor = MaterialTheme.colorScheme.primary
    val unselectedColor = MaterialTheme.colorScheme.surfaceVariant
    val borderWidth = 1.dp
    val size = 16.dp
    val innerSize = 8.dp
    val padding = 4.dp
    val shape = CircleShape
    val baseModifier = Modifier
        .padding(padding)
        .size(size)

    Box(
        modifier = baseModifier
            .border(
                width = borderWidth,
                color = when(isSelected) {
                    true -> selectedColor
                    false -> unselectedColor
                },
                shape = shape,
            )
    ) {
        if (isSelected) {
            Box(
                Modifier
                    .padding(start = (size - innerSize) / 2)
                    .padding(top = (size - innerSize) / 2)
                    .size(innerSize)
                    .background(
                        color = selectedColor,
                        shape = shape
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomRadioButtonPreview_Unselected() {
    AppTheme {
        CustomRadioButton(false)
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomRadioButtonPreview_Selected() {
    AppTheme {
        CustomRadioButton(true)
    }
}