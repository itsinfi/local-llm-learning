package de.raum7.local_llm_learning.data.models

data class Answer (
    val id: String,
    val answer: String,
    val isCorrect: Boolean,
)
