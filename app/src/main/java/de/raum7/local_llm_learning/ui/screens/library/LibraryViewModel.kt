package de.raum7.local_llm_learning.ui.screens.library

import androidx.lifecycle.viewModelScope
import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.data.models.LearningMaterial
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LibraryViewModel(
    private val navigateToAssistantCallback: () -> Unit,
    private val navigateToQuizCallback: (Int) -> Unit,
    private val repository: LibraryRepository
) : BaseViewModel(repository) {

    init {
        runBlocking {
            val learningMaterial: List<LearningMaterial> = this@LibraryViewModel.repository.getLearningMaterials()
            val learningMaterialQuestionCounts = learningMaterial.associate { it.id to repository.getQuestionCountForLearningMaterial(it.id) }
            val initialState = LibraryUiState.from(learningMaterial, learningMaterialQuestionCounts)
            this@LibraryViewModel._uiState.value = initialState
        }

    }

    fun onCreateButtonClick() {
        navigateToAssistantCallback()
    }

    fun onCardClick(learningMaterial: LearningMaterial) {
        navigateToQuizCallback(learningMaterial.id)
    }
}