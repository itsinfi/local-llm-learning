package de.raum7.local_llm_learning.data.base

open class BaseUiState {
    companion object {
        fun from(repository: BaseRepository): BaseUiState {
            return BaseUiState()
        }
    }
}