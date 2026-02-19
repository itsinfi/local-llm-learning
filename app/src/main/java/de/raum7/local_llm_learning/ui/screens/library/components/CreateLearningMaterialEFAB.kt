package de.raum7.local_llm_learning.ui.screens.library.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.shared.components.CustomFloatingActionButton
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun CreateLearningMaterialEFAB(onClick: () -> Unit) {
    CustomFloatingActionButton(
        text = stringResource(R.string.library_add_learning_material),
        imageVector = Icons.Default.AddCircle,
        onClick = onClick,
    )
}

@Preview(showBackground = true)
@Composable
private fun CreateLearningMaterialFloatingActionButtonPreview_Enabled() {
    AppTheme {
        CreateLearningMaterialEFAB(onClick = {})
    }
}