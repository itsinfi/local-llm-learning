package de.raum7.local_llm_learning.data.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

abstract class BaseViewModel(
    private val repository: BaseRepository,
) : ViewModel() {
    protected val _uiState: MutableState<BaseUiState> = mutableStateOf(
        BaseUiState.from(this.repository)
    )
    open val uiState: BaseUiState get() = _uiState.value
}