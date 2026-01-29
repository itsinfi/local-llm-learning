package de.raum7.local_llm_learning.ui.screens.assistant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.raum7.local_llm_learning.ui.components.AppBar

/**
 * Hauptscreen für den Assistant.
 * Bindet das ViewModel und stellt die UI dar.
 */
@Composable
fun AssistantScreen(
    vm: AssistantViewModel = viewModel()
) {
    // State aus dem ViewModel abonnieren
    val state by vm.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = { AppBar(title = "Assistant") }
    ) { innerPadding ->

        // Hauptlayout in Spaltenform
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            // Karte für die Antwortausgabe
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    // Fehleranzeige, falls vorhanden
                    if (state.error != null) {
                        Text(
                            text = "Error: ${state.error}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Platzhaltertext, wenn noch keine Antwort existiert
                    if (state.answer.isBlank() && state.error == null) {
                        Text(
                            text = "Antwort erscheint hier.",
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        // Anzeige der generierten Antwort
                        Text(
                            text = state.answer,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Texteingabe für den Prompt
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.prompt,
                onValueChange = vm::onPromptChanged,
                label = { Text("Prompt") },
                singleLine = false,
                minLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Button-Leiste für Send und Stop
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // Startet die Generierung
                Button(
                    onClick = { vm.send() },
                    enabled = !state.isGenerating,
                    modifier = Modifier.weight(1f)
                ) { Text("Send") }

                // Bricht die Generierung ab
                OutlinedButton(
                    onClick = { vm.stop() },
                    enabled = state.isGenerating,
                    modifier = Modifier.weight(1f)
                ) { Text("Stop") }
            }
        }
    }
}
