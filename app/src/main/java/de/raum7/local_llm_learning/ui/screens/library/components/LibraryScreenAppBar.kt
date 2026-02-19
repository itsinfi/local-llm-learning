package de.raum7.local_llm_learning.ui.screens.library.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.shared.components.AppBar
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun LibraryScreenAppBar() {
    val appBarTitle = stringResource(R.string.library)

    AppBar(
        title = appBarTitle,
        actions = {
            Icon(
                imageVector = Icons.Default.RocketLaunch,
                contentDescription = appBarTitle,
                tint = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .padding(start = 8.dp)
                    .size(24.dp),
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun LibraryScreenAppBarPreview() {
    AppTheme {
        Scaffold(topBar = { LibraryScreenAppBar() }) {
            Text("Hello, World!")
        }
    }
}