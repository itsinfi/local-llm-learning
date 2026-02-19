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
import de.raum7.local_llm_learning.data.mock.MOCK_ANSWERS
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.mock.MOCK_QUESTIONS
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
// TODO: fix preview
@Preview(showBackground = true)
@Composable
private fun AssistantResultScreenPreview_Success() {
    AppTheme {
        AppTheme {
            val jsonObject = JSONObject()
                .put("title", MOCK_LEARNING_MATERIALS[1].title)
                .put("questions", JSONArray()
                    .put(JSONObject()
                        .put("question", MOCK_QUESTIONS[4].question)
                        .put("answers", JSONArray()
                            .put(JSONObject()
                                .put("answer", MOCK_ANSWERS[13].answer)
                                .put("isCorrect", MOCK_ANSWERS[13].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_ANSWERS[14].answer)
                                .put("isCorrect", MOCK_ANSWERS[14].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_ANSWERS[15].answer)
                                .put("isCorrect", MOCK_ANSWERS[15].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_ANSWERS[16].answer)
                                .put("isCorrect", MOCK_ANSWERS[16].isCorrect)
                            )
                        )
                    )
                    .put(JSONObject()
                        .put("question", MOCK_QUESTIONS[5].question)
                        .put("answers", JSONArray()
                            .put(JSONObject()
                                .put("answer", MOCK_ANSWERS[17].answer)
                                .put("isCorrect", MOCK_ANSWERS[17].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_ANSWERS[18].answer)
                                .put("isCorrect", MOCK_ANSWERS[18].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_ANSWERS[19].answer)
                                .put("isCorrect", MOCK_ANSWERS[19].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_ANSWERS[20].answer)
                                .put("isCorrect", MOCK_ANSWERS[20].isCorrect)
                            )
                        )
                    )
                    .put(JSONObject()
                        .put("question", MOCK_QUESTIONS[6].question)
                        .put("answers", JSONArray()
                            .put(JSONObject()
                                .put("answer", MOCK_ANSWERS[21].answer)
                                .put("isCorrect", MOCK_ANSWERS[21].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_ANSWERS[22].answer)
                                .put("isCorrect", MOCK_ANSWERS[22].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_ANSWERS[23].answer)
                                .put("isCorrect", MOCK_ANSWERS[23].isCorrect)
                            )
                            .put(JSONObject()
                                .put("answer", MOCK_ANSWERS[24].answer)
                                .put("isCorrect", MOCK_ANSWERS[24].isCorrect)
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