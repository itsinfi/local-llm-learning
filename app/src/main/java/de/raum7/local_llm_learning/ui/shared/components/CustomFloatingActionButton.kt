package de.raum7.local_llm_learning.ui.shared.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.ui.theme.AppTheme

const val DISABLED_ALPHA = 0.5f

@Composable
fun CustomFloatingActionButton(
    imageVector: ImageVector,
    text: String? = null,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
) {
    val defaultContainerColor = FloatingActionButtonDefaults.containerColor
    val defaultContentColor = contentColorFor(defaultContainerColor)

    val containerColor = when(isEnabled) {
        true -> defaultContainerColor
        false -> defaultContainerColor.copy(DISABLED_ALPHA)
    }

    val contentColor = when(isEnabled) {
        true -> defaultContentColor
        false -> defaultContentColor.copy(DISABLED_ALPHA)
    }

    val executeOnClickIfEnabled = {
        if (isEnabled) {
            onClick()
        }
    }

    val icon = @Composable {
        Icon(
            imageVector = imageVector,
            contentDescription = text,
            tint = contentColor,
        )
    }

    if (text != null) {
        ExtendedFloatingActionButton(
            text = {
                Text(
                    text = text,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = contentColor,
                )
            },
            icon = icon,
            onClick = executeOnClickIfEnabled,
            containerColor = containerColor,
            contentColor = defaultContentColor,
        )
    } else {
        FloatingActionButton(
            content = icon,
            onClick = executeOnClickIfEnabled,
            containerColor = containerColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomFloatingActionButtonPreview_RegularEnabled() {
    AppTheme {
        Scaffold(
            floatingActionButton = {
                CustomFloatingActionButton(
                    text = "Hello, World!",
                    imageVector = Icons.Default.Add,
                    onClick = {},
                    isEnabled = true,
                )
            }
        ) { padding ->
            Text("Hello, World!", modifier = Modifier.padding(padding))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomFloatingActionButtonPreview_ExtendedEnabled() {
    AppTheme {
        Scaffold(
            floatingActionButton = {
                CustomFloatingActionButton(
                    imageVector = Icons.Default.Add,
                    onClick = {},
                    isEnabled = true,
                )
            }
        ) { padding ->
            Text("Hello, World!", modifier = Modifier.padding(padding))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomFloatingActionButtonPreview_RegularDisabled() {
    AppTheme {
        Scaffold(
            floatingActionButton = {
                CustomFloatingActionButton(
                    text = "Hello, World!",
                    imageVector = Icons.Default.Add,
                    onClick = {},
                    isEnabled = false,
                )
            }
        ) { padding ->
            Text("Hello, World!", modifier = Modifier.padding(padding))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomFloatingActionButtonPreview_ExtendedDisabled() {
    AppTheme {
        Scaffold(
            floatingActionButton = {
                CustomFloatingActionButton(
                    imageVector = Icons.Default.Add,
                    onClick = {},
                    isEnabled = false,
                )
            }
        ) { padding ->
            Text("Hello, World!", modifier = Modifier.padding(padding))
        }
    }
}