package de.raum7.local_llm_learning.ui.screens.assistant

import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantPhase
import de.raum7.local_llm_learning.ui.screens.assistant.types.DepthOfTopic
import de.raum7.local_llm_learning.ui.screens.assistant.types.QuestionCount

val DEFAULT_ASSISTANT_PHASE = AssistantPhase.INITIAL_DESCRIPTION
val DEFAULT_QUESTION_COUNT = QuestionCount.TWENTY
val DEFAULT_DEPTH_OF_TOPIC = DepthOfTopic.BASIC_OVERVIEW
val MIME_TYPES = mapOf(
    ".pdf" to "application/pdf",
    ".docx" to "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    ".txt" to "text/plain",
)