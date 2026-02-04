package de.raum7.local_llm_learning.ui.screens.assistant.types

import de.raum7.local_llm_learning.ui.screens.assistant.DEFAULT_QUESTION_COUNT

enum class QuestionCount(val value: Int) {
    TEN(10),
    TWENTY(20),
    FIFTY(50);

    companion object {
        private val byValue = entries.associateBy { it.value }

        fun fromValue(value: Int): QuestionCount? = byValue[value]

        fun fromValue(value: String): QuestionCount? {
            val parsed = value.toIntOrNull() ?: DEFAULT_QUESTION_COUNT
            return byValue[parsed]
        }
    }
}