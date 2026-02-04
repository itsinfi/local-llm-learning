package de.raum7.local_llm_learning.ui.screens.quiz

import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.data.base.BaseViewModelFactory

class QuizViewModelFactory(
    private val learningMaterialId: String,
    private val repository: QuizRepository,
) : BaseViewModelFactory(repository) {

    override fun createBaseViewModel(modelClass: Class<out BaseViewModel>): BaseViewModel {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(
                learningMaterialId,
                repository
            )
        }
        throw IllegalArgumentException("Unknown BaseViewModel class")
    }

}