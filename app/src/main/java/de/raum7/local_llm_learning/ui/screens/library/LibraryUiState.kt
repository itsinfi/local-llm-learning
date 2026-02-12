package de.raum7.local_llm_learning.ui.screens.library

import de.raum7.local_llm_learning.data.base.BaseUiState
import de.raum7.local_llm_learning.data.models.LearningMaterial

data class LibraryUiState(
    val learningMaterials: List<LearningMaterial>,
    val learningMaterialsQuestionCounts: Map<Int, Int>,
) : BaseUiState() {
    companion object {
        fun from(
            learningMaterials: List<LearningMaterial>,
            learningMaterialsQuestionCounts: Map<Int, Int>
        ): LibraryUiState {
            return LibraryUiState(learningMaterials, learningMaterialsQuestionCounts)
        }
    }
}
