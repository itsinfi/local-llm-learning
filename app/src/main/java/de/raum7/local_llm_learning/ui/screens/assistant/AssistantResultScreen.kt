package de.raum7.local_llm_learning.ui.screens.assistant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.parsing.LearningMaterialJsonParser
import de.raum7.local_llm_learning.ui.screens.assistant.components.AssistantResultScreenAppBar
import de.raum7.local_llm_learning.ui.screens.assistant.components.ErrorCard
import de.raum7.local_llm_learning.ui.screens.assistant.components.QuestionCardList
import de.raum7.local_llm_learning.ui.screens.assistant.components.SaveLearningMaterialEFAB
import de.raum7.local_llm_learning.ui.theme.AppTheme
import org.json.JSONArray
import org.json.JSONObject

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
        topBar = { AssistantResultScreenAppBar(onBack = onBackToLibrary) },
        floatingActionButton = {
            SaveLearningMaterialEFAB(
                onClick = onSaveToLibrary,
                isEnabled = parsed != null,
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(top = 32.dp)
                .padding(horizontal = 32.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            when(parsed) {
                null -> ErrorCard(resultText)
                else -> QuestionCardList(parsed)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AssistantResultScreenPreview_NoResult() {
    AppTheme {
        AssistantResultScreen(
            resultText = "",
            onSaveToLibrary = {},
            onBackToLibrary = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AssistantResultScreenPreview_ParsingError() {
    AppTheme {
        AssistantResultScreen(
            resultText = "Frage 1: Lorem ipsum dolor sit amet\n" +
                    "Antwort: Lorem ipsum dolor sit amet\n" +
                    "Richtig: Lorem ipsum dolor sit amet\n" +
                    "Antwort: Lorem ipsum dolor sit amet\n" +
                    "Antwort: Lorem ipsum dolor sit amet",
            onSaveToLibrary = {},
            onBackToLibrary = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AssistantResultScreenPreview_Success() {
    AppTheme {
        AppTheme {
            val jsonObject = JSONObject()
                .put("title", MOCK_LEARNING_MATERIALS[1].title)
                .put("questions", JSONArray()
                    .put(JSONObject()
                        .put("question", MOCK_LEARNING_MATERIALS[1].questions[0].question)
                        .put("answers", JSONArray()
                            .put(JSONObject()
                                .put("answer", MOCK_LEARNING_MATERIALS[1].questions[0].answers[0].answer)
                                .put("isCorrect", MOCK_LEARNING_MATERIALS[1].questions[0].answers[0].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_LEARNING_MATERIALS[1].questions[0].answers[1].answer)
                                .put("isCorrect", MOCK_LEARNING_MATERIALS[1].questions[0].answers[1].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_LEARNING_MATERIALS[1].questions[0].answers[2].answer)
                                .put("isCorrect", MOCK_LEARNING_MATERIALS[1].questions[0].answers[2].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_LEARNING_MATERIALS[1].questions[0].answers[3].answer)
                                .put("isCorrect", MOCK_LEARNING_MATERIALS[1].questions[0].answers[3].isCorrect)
                            )
                        )
                    )
                    .put(JSONObject()
                        .put("question", MOCK_LEARNING_MATERIALS[1].questions[0].question)
                        .put("answers", JSONArray()
                            .put(JSONObject()
                                .put("answer", MOCK_LEARNING_MATERIALS[1].questions[1].answers[0].answer)
                                .put("isCorrect", MOCK_LEARNING_MATERIALS[1].questions[1].answers[0].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_LEARNING_MATERIALS[1].questions[1].answers[1].answer)
                                .put("isCorrect", MOCK_LEARNING_MATERIALS[1].questions[1].answers[1].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_LEARNING_MATERIALS[1].questions[1].answers[2].answer)
                                .put("isCorrect", MOCK_LEARNING_MATERIALS[1].questions[1].answers[2].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_LEARNING_MATERIALS[1].questions[1].answers[3].answer)
                                .put("isCorrect", MOCK_LEARNING_MATERIALS[1].questions[1].answers[3].isCorrect)
                            )
                        )
                    )
                    .put(JSONObject()
                        .put("question", MOCK_LEARNING_MATERIALS[1].questions[2].question)
                        .put("answers", JSONArray()
                            .put(JSONObject()
                                .put("answer", MOCK_LEARNING_MATERIALS[1].questions[2].answers[0].answer)
                                .put("isCorrect", MOCK_LEARNING_MATERIALS[1].questions[2].answers[0].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_LEARNING_MATERIALS[1].questions[2].answers[1].answer)
                                .put("isCorrect", MOCK_LEARNING_MATERIALS[1].questions[2].answers[1].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_LEARNING_MATERIALS[1].questions[2].answers[2].answer)
                                .put("isCorrect", MOCK_LEARNING_MATERIALS[1].questions[2].answers[2].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_LEARNING_MATERIALS[1].questions[2].answers[3].answer)
                                .put("isCorrect", MOCK_LEARNING_MATERIALS[1].questions[2].answers[3].isCorrect)
                            )
                        )
                    )
                )

            val parsed = jsonObject.toString()

            AssistantResultScreen(
                resultText = parsed,
                onSaveToLibrary = {},
                onBackToLibrary = {},
            )
        }
    }
}