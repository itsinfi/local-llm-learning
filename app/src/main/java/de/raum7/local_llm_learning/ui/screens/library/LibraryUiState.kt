package de.raum7.local_llm_learning.ui.screens.library

import de.raum7.local_llm_learning.data.base.BaseUiState
import de.raum7.local_llm_learning.data.models.LearningMaterial

data class LibraryUiState(
    val learningMaterials: List<LearningMaterial>
) : BaseUiState() {
    companion object {
        fun from(
            learningMaterials: List<LearningMaterial>
        ): LibraryUiState {
            return LibraryUiState(learningMaterials)
        }
    }
}
