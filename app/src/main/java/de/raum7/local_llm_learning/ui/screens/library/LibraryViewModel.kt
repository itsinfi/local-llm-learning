package de.raum7.local_llm_learning.ui.screens.library

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import de.raum7.local_llm_learning.data.base.BaseUiState
import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.data.models.LearningMaterial

class LibraryViewModel(
    private val navigateToAssistantCallback: () -> Unit,
    private val navigateToQuizCallback: (String) -> Unit,
    private val repository: LibraryRepository
) : BaseViewModel(repository) {
    private val learningMaterial: List<LearningMaterial> =
        repository.getLearningMaterials()

    override val _uiState: MutableState<BaseUiState> = mutableStateOf(
        LibraryUiState.from(learningMaterial)
    )

    fun onCreateButtonClick() {
        navigateToAssistantCallback()
    }

    fun onCardClick(learningMaterial: LearningMaterial) {
        navigateToQuizCallback(learningMaterial.id)
    }

    override val uiState: BaseUiState get() = _uiState.value


}