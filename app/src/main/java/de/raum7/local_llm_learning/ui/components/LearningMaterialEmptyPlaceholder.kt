package de.raum7.local_llm_learning.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun LearningMaterialEmptyPlaceholder(padding: PaddingValues) {
    Text(
        text = stringResource(R.string.lml_no_material),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .padding(padding)
            .padding(top = 32.dp)
            .fillMaxWidth(),
    )
}

@Preview(showBackground = true)
@Composable
fun LearningMaterialEmptyPlaceholderPreview() {
    AppTheme {
        LearningMaterialEmptyPlaceholder(padding = PaddingValues.Zero)
    }
}
