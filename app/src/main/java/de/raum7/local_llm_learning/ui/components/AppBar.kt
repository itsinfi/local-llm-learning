package de.raum7.local_llm_learning.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
    AppTheme {
        Scaffold(
            topBar = { AppBar("App Bar") }
        ) { innerPadding -> Text("Content", modifier = Modifier.padding(innerPadding)) }
    }
}