package de.raum7.local_llm_learning.ui.screens.assistant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.llm.LlmGenerationService
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantCardUiState
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantPhase
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantUiStateChange
import kotlin.math.max
import kotlin.math.min

class AssistantViewModel(
    private val repository: AssistantRepository,
    private val appContext: Context
) : BaseViewModel(repository) {

    private val resultReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action != LlmGenerationService.ACTION_RESULT) return

            val text =
                intent.getStringExtra(LlmGenerationService.EXTRA_RESULT_TEXT).orEmpty()
            val error =
                intent.getStringExtra(LlmGenerationService.EXTRA_RESULT_ERROR)

            val state = uiState as AssistantUiState

            _uiState.value = state.copy(
                isGenerating = false,
                generationResult = if (error.isNullOrBlank()) text else "",
                generationError = error,
                assistantCard = state.assistantCard.copy(isLocked = false),
            )
        }
    }

    init {
        _uiState.value = AssistantUiState.from()
        registerReceiver()
    }

    private fun registerReceiver() {
        val filter = IntentFilter(LlmGenerationService.ACTION_RESULT)

        appContext.registerReceiver(
            resultReceiver,
            filter,
            Context.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onCleared() {
        appContext.unregisterReceiver(resultReceiver)
        super.onCleared()
    }

    private fun isContinueAllowed(
        phase: AssistantPhase? = null,
        prompt: String? = null,
        filePath: Uri? = null,
    ): Boolean {
        val state = uiState as AssistantUiState

        return when (phase ?: state.assistantCard.phase) {
            AssistantPhase.INITIAL_DESCRIPTION ->
                (prompt ?: state.initialDescription.prompt).isNotBlank() ||
                        (filePath ?: state.initialDescription.filePath) != null

            else -> true
        }
    }

    fun onContinue() {
        val state = uiState as AssistantUiState
        if (state.isGenerating) return

        when (state.assistantCard.phase) {
            AssistantPhase.INITIAL_DESCRIPTION -> {
                _uiState.value = state.copy(
                    assistantCard = AssistantCardUiState(
                        phase = AssistantPhase.PARAMETER_SELECTION,
                        isContinueEnabled = true,
                        isLocked = false,
                    )
                )
            }

            AssistantPhase.PARAMETER_SELECTION -> {
                _uiState.value = state.copy(
                    assistantCard = AssistantCardUiState(
                        phase = AssistantPhase.FURTHER_SPECIFICATION,
                        isContinueEnabled = true,
                        isLocked = false,
                    )
                )
            }

            AssistantPhase.FURTHER_SPECIFICATION -> {
                startGeneration(state)
            }
        }
    }

    fun onBack() {
        val state = uiState as AssistantUiState
        if (state.isGenerating) return

        val phase =
            if (state.assistantCard.phase == AssistantPhase.FURTHER_SPECIFICATION)
                AssistantPhase.PARAMETER_SELECTION
            else
                AssistantPhase.INITIAL_DESCRIPTION

        val isContinueEnabled = isContinueAllowed(phase)

        _uiState.value = state.copy(
            assistantCard = state.assistantCard.copy(
                phase = phase,
                isContinueEnabled = isContinueEnabled,
                isLocked = false,
            )
        )
    }

    fun onChanged(change: AssistantUiStateChange) {
        val state = uiState as AssistantUiState
        if (state.isGenerating) return

        val newPrompt =
            change.prompt ?: state.initialDescription.prompt

        val newFilePath =
            change.filePath ?: state.initialDescription.filePath

        val isContinueEnabled = isContinueAllowed(
            phase = state.assistantCard.phase,
            prompt = newPrompt,
            filePath = newFilePath
        )

        _uiState.value = state.copy(
            assistantCard = state.assistantCard.copy(
                isContinueEnabled = isContinueEnabled
            ),
            initialDescription = state.initialDescription.copy(
                prompt = newPrompt,
                filePath = newFilePath,
            ),
            parameterSelection = state.parameterSelection.copy(
                questionCount = state.parameterSelection.questionCount.copy(
                    selected = change.questionCount?.value?.toString()
                        ?: state.parameterSelection.questionCount.selected,
                    expanded = change.questionCountExpanded
                        ?: state.parameterSelection.questionCount.expanded,
                ),
                depthOfTopic = state.parameterSelection.depthOfTopic.copy(
                    selected = change.depthOfTopic?.toString()
                        ?: state.parameterSelection.depthOfTopic.selected,
                    expanded = change.depthOfTopicExpanded
                        ?: state.parameterSelection.depthOfTopic.expanded,
                )
            ),
            furtherSpecification = state.furtherSpecification.copy(
                topicSpecification =
                    change.topicSpecification
                        ?: state.furtherSpecification.topicSpecification,
                goal =
                    change.goal ?: state.furtherSpecification.goal,
            )
        )
    }

    private fun startGeneration(state: AssistantUiState) {
        val fullPrompt = buildPrompt(state)
        if (fullPrompt.isBlank()) return

        _uiState.value = state.copy(
            isGenerating = true,
            assistantCard = state.assistantCard.copy(isLocked = true),
            generationError = null,
            generationResult = "",
        )

        val intent = Intent(appContext, LlmGenerationService::class.java).apply {
            action = LlmGenerationService.ACTION_START
            putExtra(LlmGenerationService.EXTRA_PROMPT, fullPrompt)
        }

        appContext.startForegroundService(intent)
    }

    private fun buildPrompt(state: AssistantUiState): String {
        val topic = state.initialDescription.prompt.trim()
        val count = state.parameterSelection.questionCount.selected.trim()
        val depth = state.parameterSelection.depthOfTopic.selected.trim()
        val spec = state.furtherSpecification.topicSpecification.trim()
        val goal = state.furtherSpecification.goal.trim()

        return """
You generate learning quizzes as STRICT JSON.

Task:
Create exact $count single choice questions.

Topic:
$topic

Depth:
$depth

Additional specification:
$spec

Learning goal:
$goal

Hard rules:
1) Output ONLY valid JSON. No markdown. No code fences. No comments.
2) Use double quotes for all strings.
3) No trailing commas anywhere.
4) Exactly 4 answers per question.
5) Exactly 1 answer has "correct": true, the other 3 are false.
6) No explanations.

Return JSON schema exactly:
{
  "title": "string",
  "questions": [
    {
      "question": "string",
      "answers": [
        { "text": "string", "correct": true },
        { "text": "string", "correct": false },
        { "text": "string", "correct": false },
        { "text": "string", "correct": false }
      ]
    }
  ]
}

End your output with: <END_JSON>
""".trimIndent()
    }
}
