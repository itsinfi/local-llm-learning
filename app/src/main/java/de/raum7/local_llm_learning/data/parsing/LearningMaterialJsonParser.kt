package de.raum7.local_llm_learning.data.parsing

import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.data.models.Question
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

object LearningMaterialJsonParser {

    fun parse(raw: String): LearningMaterial {
        val json = extractAndSanitizeJson(raw)
        val root = JSONObject(json)

        val title = root.optString("title", "Unbenannt").ifBlank { "Unbenannt" }
        val questionsArr = root.optJSONArray("questions")
            ?: throw IllegalArgumentException("JSON missing 'questions' array")

        val questions = mutableListOf<Question>()

        for (i in 0 until questionsArr.length()) {
            val qObj = questionsArr.optJSONObject(i) ?: continue

            val qText = qObj.optString("question", "").trim()
            if (qText.isBlank()) continue

            val answersArr = qObj.optJSONArray("answers") ?: JSONArray()
            val answers = mutableListOf<Answer>()

            for (a in 0 until answersArr.length()) {
                val aObj = answersArr.optJSONObject(a) ?: continue

                val text = aObj.optString("text", aObj.optString("answer", "")).trim()
                val correct = when {
                    aObj.has("correct") -> aObj.optBoolean("correct", false)
                    aObj.has("isCorrect") -> aObj.optBoolean("isCorrect", false)
                    else -> false
                }

                if (text.isBlank()) continue

                answers.add(
                    Answer(
                        id = UUID.randomUUID().toString(),
                        answer = text,
                        isCorrect = correct
                    )
                )
            }

            if (answers.isEmpty()) continue

            questions.add(
                Question(
                    id = UUID.randomUUID().toString(),
                    question = qText,
                    answers = answers
                )
            )
        }

        if (questions.isEmpty()) {
            throw IllegalArgumentException("No questions parsed from model output")
        }

        return LearningMaterial(
            id = UUID.randomUUID().toString(),
            title = title,
            questions = questions,
            progress = 0.0
        )
    }

    private fun extractAndSanitizeJson(raw: String): String {
        val cleaned = raw
            .trim()
            .replace("\uFEFF", "") // BOM
            .replace("```json", "")
            .replace("```", "")
            .replace("<END_JSON>", "")
            .trim()

        val start = cleaned.indexOf('{')
        val end = cleaned.lastIndexOf('}')

        if (start == -1 || end == -1 || end <= start) {
            throw IllegalArgumentException("No JSON object found in model output")
        }

        var json = cleaned.substring(start, end + 1)

        // Smart quotes zu normalen Quotes
        json = json
            .replace('“', '"')
            .replace('”', '"')
            .replace('„', '"')
            .replace('’', '\'')
            .replace('‘', '\'')

        // Trailing commas entfernen: , } oder , ]
        json = json.replace(Regex(",\\s*([}\\]])"), "$1")

        // Steuerzeichen entfernen, die JSON brechen können
        json = json.replace(Regex("[\\u0000-\\u0008\\u000B\\u000C\\u000E-\\u001F]"), "")

        return json
    }
}
