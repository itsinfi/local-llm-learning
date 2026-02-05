package de.raum7.local_llm_learning.ui.screens.assistant

import android.content.Context
import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.data.base.BaseViewModelFactory

class AssistantViewModelFactory(
    private val repository: AssistantRepository,
    private val appContext: Context,
) : BaseViewModelFactory(repository) {

    override fun createBaseViewModel(modelClass: Class<out BaseViewModel>): BaseViewModel {
        if (modelClass.isAssignableFrom(AssistantViewModel::class.java)) {
            return AssistantViewModel(repository, appContext)
        }
        throw IllegalArgumentException("Unknown BaseViewModel class")
    }
}
