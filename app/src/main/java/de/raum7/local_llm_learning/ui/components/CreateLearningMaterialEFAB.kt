package de.raum7.local_llm_learning.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun CreateLearningMaterialEFAB(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        text = { Text(stringResource(R.string.lml_add_learning_material)) },
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.lml_add_learning_material),
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun CreateLearningMaterialFloatingActionButtonPreview() {
    AppTheme {
        CreateLearningMaterialEFAB(onClick = {})
    }
}