package de.raum7.local_llm_learning.data.base

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel

abstract class BaseViewModel(
    private val repository: BaseRepository,
) : ViewModel() {
    protected abstract val _uiState: MutableState<BaseUiState>
    open val uiState: BaseUiState get() = _uiState.value
}