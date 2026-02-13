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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.data.mock.MOCK_LEARNING_MATERIALS
import de.raum7.local_llm_learning.data.parsing.LearningMaterialJsonParser
import de.raum7.local_llm_learning.ui.shared.components.AppBar
import de.raum7.local_llm_learning.ui.shared.components.CustomElevatedButton
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
        topBar = { AppBar(title = stringResource(R.string.assisant_result)) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            CustomElevatedButton(
                label = stringResource(R.string.assisant_result_save),
                onclick = onSaveToLibrary,
                // buttonClass = ButtonClass.PRIMARY, TODO:
                isEnabled = parsed != null
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomElevatedButton(
                label = stringResource(R.string.assisant_back_to_library),
                onclick = onBackToLibrary,
                // buttonClass = ButtonClass.SECONDARY TODO:
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                if (parsed == null) {
                    Text(
                        text = "${stringResource(R.string.assisant_result_parsing_error)}:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = resultText.ifBlank { stringResource(R.string.assisant_result_no_result_error) })
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
                        val prefix = if (a.isCorrect)
                            "${stringResource(R.string.assisant_result_correct)}: "
                        else
                            "${stringResource(R.string.assisant_result_answer)}: "
                        Text(text = prefix + a.answer)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
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