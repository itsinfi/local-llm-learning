package de.raum7.local_llm_learning.ui.screens.assistant.types

import android.net.Uri

data class AssistantUiStateChange(
    val filePath: Uri? = null,
    val prompt: String? = null,
    val questionCount: QuestionCount? = null,
    val questionCountExpanded: Boolean? = null,
    val depthOfTopic: DepthOfTopic? = null,
    val depthOfTopicExpanded: Boolean? = null,
    val topicSpecification: String? = null,
    val goal: String? = null,
)