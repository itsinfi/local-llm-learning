package de.raum7.local_llm_learning.data.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

abstract class BaseViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.Factory {

    final override fun <T: ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (!BaseViewModel::class.java.isAssignableFrom(modelClass)) {
            error("Factory can only create BaseViewModel subclasses and not: $modelClass")
        }

        @Suppress("UNCHECKED_CAST")
        return createBaseViewModel(modelClass as Class<out BaseViewModel>) as T
    }

    protected abstract fun createBaseViewModel(modelClass: Class<out BaseViewModel>): BaseViewModel
}