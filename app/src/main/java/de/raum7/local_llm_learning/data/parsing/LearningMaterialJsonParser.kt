package de.raum7.local_llm_learning.data.parsing

import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.data.models.Question
import org.json.JSONObject
import java.util.UUID

object LearningMaterialJsonParser {

    fun parse(raw: String): LearningMaterial {
        val json = extractJsonObject(raw)
        val root = JSONObject(json)

        val title = root.optString("title", "Unbenannt").ifBlank { "Unbenannt" }
        val questionsArr = root.getJSONArray("questions")

        val questions = mutableListOf<Question>()

        for (i in 0 until questionsArr.length()) {
            val qObj = questionsArr.getJSONObject(i)
            val qText = qObj.getString("question")

            val answersArr = qObj.getJSONArray("answers")
            val answers = mutableListOf<Answer>()

            for (a in 0 until answersArr.length()) {
                val aObj = answersArr.getJSONObject(a)
                val text = aObj.getString("text")
                val correct = aObj.getBoolean("correct")

                answers.add(
                    Answer(
                        id = UUID.randomUUID().toString(),
                        answer = text,
                        isCorrect = correct
                    )
                )
            }

            questions.add(
                Question(
                    id = UUID.randomUUID().toString(),
                    question = qText,
                    answers = answers
                )
            )
        }

        return LearningMaterial(
            id = UUID.randomUUID().toString(),
            title = title,
            questions = questions,
            progress = 0.0
        )
    }

    private fun extractJsonObject(raw: String): String {
        val cleaned = raw
            .trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()

        val start = cleaned.indexOf('{')
        val end = cleaned.lastIndexOf('}')

        if (start == -1 || end == -1 || end <= start) {
            throw IllegalArgumentException("No JSON object found in model output")
        }

        return cleaned.substring(start, end + 1)
    }
}
