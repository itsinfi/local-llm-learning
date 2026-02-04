package de.raum7.local_llm_learning.ui.screens.edit_question

import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.data.base.BaseRepository
import de.raum7.local_llm_learning.data.models.Question

class EditQuestionRepository(
    private val data: List<LearningMaterial>
) : BaseRepository(data) {
    fun getQuestion(questionId: String, learningMaterialId: String): Question {
        val learningMaterial: LearningMaterial = data.firstOrNull { it.id == learningMaterialId } ?:error("LearningMaterial with id $learningMaterialId not found")
        return learningMaterial.questions.firstOrNull { it.id == questionId } ?:error("Question with id $questionId not found in LearningMaterial with id $learningMaterialId ")
    }
}