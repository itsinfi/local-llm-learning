package de.raum7.local_llm_learning.ui.screens.edit_question

import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.data.base.BaseViewModelFactory

class EditQuestionViewModelFactory(
    private val learningMaterialId: String,
    private val questionId: String,
    private val repository: EditQuestionRepository

) : BaseViewModelFactory(repository) {

    override fun createBaseViewModel(modelClass: Class<out BaseViewModel>): BaseViewModel {
        if (modelClass.isAssignableFrom(EditQuestionViewModel::class.java)) {
            return EditQuestionViewModel(
                learningMaterialId,
                questionId,
                repository
            )
        }
        throw IllegalArgumentException("Unknown BaseViewModel class")
    }

}