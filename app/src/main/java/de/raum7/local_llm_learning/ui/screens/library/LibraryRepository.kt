package de.raum7.local_llm_learning.ui.screens.library

import de.raum7.local_llm_learning.data.base.BaseRepository
import de.raum7.local_llm_learning.data.models.LearningMaterial

class LibraryRepository(
    private val data: List<LearningMaterial>
) : BaseRepository(data) {
    fun getLearningMaterials(): List<LearningMaterial> {
        return data
    }
}