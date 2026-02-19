package de.raum7.local_llm_learning.ui.screens.assistant.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.shared.components.CustomFloatingActionButton
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun SaveLearningMaterialEFAB(onClick: () -> Unit, isEnabled: Boolean) {
    CustomFloatingActionButton(
        text = stringResource(R.string.assisant_result_save),
        imageVector = Icons.Default.Save,
        onClick = onClick,
        isEnabled = isEnabled,
    )
}

@Preview(showBackground = true)
@Composable
private fun SaveLearningMaterialFloatingActionButtonPreview_Enabled() {
    AppTheme {
        SaveLearningMaterialEFAB(onClick = {}, isEnabled = true)
    }
}

@Preview(showBackground = true)
@Composable
private fun SaveLearningMaterialFloatingActionButtonPreview_Disabled() {
    AppTheme {
        SaveLearningMaterialEFAB(onClick = {}, isEnabled = false)
    }
}