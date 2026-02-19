package de.raum7.local_llm_learning.ui.screens.library

import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.data.base.BaseViewModelFactory

class LibraryViewModelFactory(
    private val navigateToAssistantCallback: () -> Unit,
    private val navigateToQuizCallback: (Int) -> Unit,
    private val repository: LibraryRepository
) : BaseViewModelFactory(repository) {

    override fun createBaseViewModel(modelClass: Class<out BaseViewModel>): BaseViewModel {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            return LibraryViewModel(
                navigateToAssistantCallback,
                navigateToQuizCallback,
                repository,
            )
        }
        throw IllegalArgumentException("Unknown BaseViewModel class")
    }
}