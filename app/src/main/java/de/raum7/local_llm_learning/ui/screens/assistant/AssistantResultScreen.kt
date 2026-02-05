package de.raum7.local_llm_learning.ui.screens.assistant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.data.parsing.LearningMaterialJsonParser
import de.raum7.local_llm_learning.ui.shared.components.AppBar
import de.raum7.local_llm_learning.ui.shared.components.ButtonClass
import de.raum7.local_llm_learning.ui.shared.components.CustomElevatedButton

@Composable
fun AssistantResultScreen(
    resultText: String,
    onSaveToLibrary: () -> Unit,
    onBackToLibrary: () -> Unit,
) {
    val parsed = try {
        LearningMaterialJsonParser.parse(resultText)
    } catch (_: Throwable) {
        null
    }

    Scaffold(
        topBar = { AppBar(title = "Generiertes Lernmaterial") }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            CustomElevatedButton(
                label = "In Bibliothek speichern",
                onclick = onSaveToLibrary,
                buttonClass = ButtonClass.PRIMARY,
                isEnabled = parsed != null
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomElevatedButton(
                label = "Zur Bibliothek",
                onclick = onBackToLibrary,
                buttonClass = ButtonClass.SECONDARY
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                if (parsed == null) {
                    Text(
                        text = "Ausgabe konnte nicht geparsed werden. Rohtext:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = resultText.ifBlank { "Kein Ergebnis erhalten." })
                    return@Column
                }

                Text(
                    text = parsed.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(12.dp))

                parsed.questions.forEachIndexed { index, q ->
                    Text(
                        text = "${index + 1}. ${q.question}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    q.answers.forEach { a ->
                        val prefix = if (a.isCorrect) "Richtig: " else "Antwort: "
                        Text(text = prefix + a.answer)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
