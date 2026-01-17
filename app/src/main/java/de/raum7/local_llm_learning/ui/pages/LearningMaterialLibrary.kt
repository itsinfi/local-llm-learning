package de.raum7.local_llm_learning.ui.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.ui.components.AppBar
import de.raum7.local_llm_learning.ui.theme.AppTheme

var PAGE_TITLE = "Learning Material Library"

@Composable
fun LearningMaterialLibrary() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppBar(title = PAGE_TITLE) }
    ) { innerPadding ->
        Card (
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Hello World!",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LearningMaterialLibraryPreview() {
    AppTheme {
        LearningMaterialLibrary()
    }
}