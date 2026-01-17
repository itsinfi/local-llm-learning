package de.raum7.local_llm_learning.ui.pages

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun LearningMaterialCreationAssistant() {
    Scaffold { innerPadding ->
        Text(
            text = "Placeholder for LMCA",
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LearningMaterialCreationAssistantPreview() {
    AppTheme {
        LearningMaterialCreationAssistant()
    }
}