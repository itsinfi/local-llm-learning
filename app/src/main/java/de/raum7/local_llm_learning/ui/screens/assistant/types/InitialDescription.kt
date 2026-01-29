package de.raum7.local_llm_learning.ui.screens.assistant.types

import android.net.Uri

data class InitialDescription(
    val filePath: Uri?,
    val prompt: String,
)
