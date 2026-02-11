package de.raum7.local_llm_learning.ui.screens.quiz

import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.data.base.BaseRepository

class QuizRepository(
    private val data: List<LearningMaterial>
) : BaseRepository() {
    fun getLearningMaterialById(id: String): LearningMaterial {
        return data.firstOrNull { it.id == id } ?:error("LearningMaterial with $id not found")
    }
}