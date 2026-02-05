
package de.raum7.local_llm_learning.data.store

import android.content.Context
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.data.models.Question
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

object LearningMaterialDiskStore {

    private const val FILE_NAME = "learning_materials.json"

    fun load(context: Context): List<LearningMaterial> {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) return emptyList()

        val raw = file.readText(Charsets.UTF_8).trim()
        if (raw.isBlank()) return emptyList()

        val arr = JSONArray(raw)
        val list = mutableListOf<LearningMaterial>()

        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            list.add(fromJson(obj))
        }
        return list
    }

    fun save(context: Context, materials: List<LearningMaterial>) {
        val file = File(context.filesDir, FILE_NAME)
        val arr = JSONArray()
        materials.forEach { arr.put(toJson(it)) }
        file.writeText(arr.toString(), Charsets.UTF_8)
    }

    private fun toJson(m: LearningMaterial): JSONObject {
        val obj = JSONObject()
        obj.put("id", m.id)
        obj.put("title", m.title)
        obj.put("progress", m.progress)

        val qArr = JSONArray()
        m.questions.forEach { q ->
            val qObj = JSONObject()
            qObj.put("id", q.id)
            qObj.put("question", q.question)

            val aArr = JSONArray()
            q.answers.forEach { a ->
                val aObj = JSONObject()
                aObj.put("id", a.id)
                aObj.put("answer", a.answer)
                aObj.put("isCorrect", a.isCorrect)
                aArr.put(aObj)
            }
            qObj.put("answers", aArr)
            qArr.put(qObj)
        }
        obj.put("questions", qArr)
        return obj
    }

    private fun fromJson(obj: JSONObject): LearningMaterial {
        val id = obj.getString("id")
        val title = obj.getString("title")
        val progress = obj.optDouble("progress", 0.0)

        val qArr = obj.getJSONArray("questions")
        val questions = mutableListOf<Question>()

        for (i in 0 until qArr.length()) {
            val qObj = qArr.getJSONObject(i)
            val qId = qObj.getString("id")
            val qText = qObj.getString("question")

            val aArr = qObj.getJSONArray("answers")
            val answers = mutableListOf<Answer>()

            for (a in 0 until aArr.length()) {
                val aObj = aArr.getJSONObject(a)
                answers.add(
                    Answer(
                        id = aObj.getString("id"),
                        answer = aObj.getString("answer"),
                        isCorrect = aObj.getBoolean("isCorrect")
                    )
                )
            }

            questions.add(
                Question(
                    id = qId,
                    question = qText,
                    answers = answers
                )
            )
        }

        return LearningMaterial(
            id = id,
            title = title,
            questions = questions,
            progress = progress
        )
    }
}
