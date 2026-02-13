package de.raum7.local_llm_learning.ui.screens.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.shared.components.CustomCard
import de.raum7.local_llm_learning.ui.shared.components.CustomCardColors
import de.raum7.local_llm_learning.ui.theme.AppTheme

@Composable
fun ErrorCard(resultText: String) {
    CustomCard(
        colors = CustomCardColors.from(
            outlineColor = MaterialTheme.colorScheme.error,
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                val errorMessage = stringResource(R.string.assisant_result_parsing_error)

                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = errorMessage,
                    tint = MaterialTheme.colorScheme.error
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = errorMessage,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when(resultText.trim()) {
                    "" -> stringResource(R.string.assisant_result_no_result_error)
                    else -> "${stringResource(R.string.assisant_result_raw_text)}:\n" +
                            resultText
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(0.1f),
                        shape = CardDefaults.shape,
                    )
                    .padding(8.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorCardPreview_Empty() {
    AppTheme { ErrorCard("") }
}

@Preview(showBackground = true)
@Composable
private fun ErrorCardPreview_Parsing() {
    AppTheme { ErrorCard("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.") }
}