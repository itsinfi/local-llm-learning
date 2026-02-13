package de.raum7.local_llm_learning.ui.shared.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    navigationIcon: @Composable (() -> Unit) = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    textStyle: TextStyle = MaterialTheme.typography.headlineLarge,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = textStyle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
        modifier = modifier,
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