package de.raum7.local_llm_learning.ui.screens.library

import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.data.models.LearningMaterial

class LibraryViewModel(
    private val navigateToAssistantCallback: () -> Unit,
    private val navigateToQuizCallback: (String) -> Unit,
    private val repository: LibraryRepository
) : BaseViewModel(repository) {
    private val learningMaterial: List<LearningMaterial> =
        this.repository.getLearningMaterials()

    init {
        val initialState = LibraryUiState.from(this.learningMaterial)
        this._uiState.value = initialState
    }

    fun onCreateButtonClick() {
        navigateToAssistantCallback()
    }

    fun onCardClick(learningMaterial: LearningMaterial) {
        navigateToQuizCallback(learningMaterial.id)
    }
}