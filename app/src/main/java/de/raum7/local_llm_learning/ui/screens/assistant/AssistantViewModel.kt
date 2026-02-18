// app/src/main/java/de/raum7/local_llm_learning/ui/screens/assistant/AssistantViewModel.kt
package de.raum7.local_llm_learning.ui.screens.assistant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.viewModelScope
import de.raum7.local_llm_learning.data.base.BaseViewModel
import de.raum7.local_llm_learning.llm.LlmGenerationService
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantCardUiState
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantPhase
import de.raum7.local_llm_learning.ui.screens.assistant.types.AssistantUiStateChange
import de.raum7.local_llm_learning.util.DocumentTextExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class AssistantViewModel(
    private val repository: AssistantRepository,
    private val appContext: Context
) : BaseViewModel(repository) {

    private var currentRequestId: String? = null
    private var currentStage: Int = 0
    private var expectedQuestionCount: Int = 0

    private var stage1Draft: String = ""
    private var stage2Validated: String = ""

    private val logTag = "AssistantGen"

    private val documentTextExtractor = DocumentTextExtractor(appContext)
    private var sourceMaterialText: String = ""
    private var sourceProvidedButEmpty: Boolean = false
    private var sourceMetaLine: String = ""

    private val resultReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action != LlmGenerationService.ACTION_RESULT) return

            val text = intent.getStringExtra(LlmGenerationService.EXTRA_RESULT_TEXT).orEmpty()
            val error = intent.getStringExtra(LlmGenerationService.EXTRA_RESULT_ERROR)

            val requestId = intent.getStringExtra(LlmGenerationService.EXTRA_REQUEST_ID)
            val stage = intent.getIntExtra(LlmGenerationService.EXTRA_STAGE, 0)

            if (currentRequestId.isNullOrBlank() || requestId.isNullOrBlank()) return
            if (requestId != currentRequestId) return

            val state = uiState as AssistantUiState

            if (!error.isNullOrBlank()) {
                logLong(logTag, "stage=$stage error", error)
                _uiState.value = state.copy(
                    isGenerating = false,
                    generationResult = "",
                    generationError = error,
                    assistantCard = state.assistantCard.copy(isLocked = false)
                )
                currentStage = 0
                currentRequestId = null
                return
            }

            when (stage) {
                1 -> {
                    stage1Draft = text
                    logLong(logTag, "stage=1 draft", stage1Draft)
                    currentStage = 2

                    val prompt2 = buildPromptValidate(
                        state = state,
                        expectedCount = expectedQuestionCount,
                        draft = stage1Draft
                    )
                    logLong(logTag, "stage=2 prompt", prompt2)
                    startServiceGeneration(prompt2, requestId, stage = 2)
                }

                2 -> {
                    stage2Validated = text
                    logLong(logTag, "stage=2 validated", stage2Validated)
                    currentStage = 3

                    val prompt3 = buildPromptJson(
                        expectedCount = expectedQuestionCount,
                        validated = stage2Validated
                    )
                    logLong(logTag, "stage=3 prompt", prompt3)
                    startServiceGeneration(prompt3, requestId, stage = 3)
                }

                3 -> {
                    val cleaned = extractJsonObject(text)
                    logLong(logTag, "stage=3 json cleaned", cleaned)

                    _uiState.value = state.copy(
                        isGenerating = false,
                        generationResult = cleaned,
                        generationError = null,
                        assistantCard = state.assistantCard.copy(isLocked = false)
                    )

                    currentStage = 0
                    currentRequestId = null
                }

                else -> {
                    logLong(logTag, "stage=$stage unexpected", text)
                    _uiState.value = state.copy(
                        isGenerating = false,
                        generationResult = text,
                        generationError = null,
                        assistantCard = state.assistantCard.copy(isLocked = false)
                    )
                    currentStage = 0
                    currentRequestId = null
                }
            }
        }
    }

    init {
        _uiState.value = AssistantUiState.from()
        registerReceiver()
    }

    private fun extractJsonObject(raw: String): String {
        val s = raw.trim()
        val start = s.indexOf('{')
        val end = s.lastIndexOf('}')
        return if (start >= 0 && end > start) {
            s.substring(start, end + 1).trim()
        } else {
            s
        }
    }

    private fun registerReceiver() {
        val filter = IntentFilter(LlmGenerationService.ACTION_RESULT)
        appContext.registerReceiver(resultReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
    }

    override fun onCleared() {
        appContext.unregisterReceiver(resultReceiver)
        super.onCleared()
    }

    private fun isContinueAllowed(
        phase: AssistantPhase? = null,
        prompt: String? = null,
        filePath: Uri? = null
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
                        isLocked = false
                    )
                )
            }

            AssistantPhase.PARAMETER_SELECTION -> {
                _uiState.value = state.copy(
                    assistantCard = AssistantCardUiState(
                        phase = AssistantPhase.FURTHER_SPECIFICATION,
                        isContinueEnabled = true,
                        isLocked = false
                    )
                )
            }

            AssistantPhase.FURTHER_SPECIFICATION -> startGeneration(state)
        }
    }

    fun onBack() {
        val state = uiState as AssistantUiState
        if (state.isGenerating) return

        val newPhase =
            if (state.assistantCard.phase == AssistantPhase.FURTHER_SPECIFICATION)
                AssistantPhase.PARAMETER_SELECTION
            else
                AssistantPhase.INITIAL_DESCRIPTION

        _uiState.value = state.copy(
            assistantCard = state.assistantCard.copy(
                phase = newPhase,
                isContinueEnabled = isContinueAllowed(newPhase),
                isLocked = false
            )
        )
    }

    fun onChanged(change: AssistantUiStateChange) {
        val state = uiState as AssistantUiState
        if (state.isGenerating) return

        val newPrompt = change.prompt ?: state.initialDescription.prompt
        val newFilePath = change.filePath ?: state.initialDescription.filePath

        _uiState.value = state.copy(
            assistantCard = state.assistantCard.copy(
                isContinueEnabled = isContinueAllowed(
                    phase = state.assistantCard.phase,
                    prompt = newPrompt,
                    filePath = newFilePath
                ),
                isLocked = false
            ),
            initialDescription = state.initialDescription.copy(
                prompt = newPrompt,
                filePath = newFilePath
            ),
            parameterSelection = state.parameterSelection.copy(
                questionCount = state.parameterSelection.questionCount.copy(
                    selected = change.questionCount?.value?.toString()
                        ?: state.parameterSelection.questionCount.selected,
                    expanded = change.questionCountExpanded
                        ?: state.parameterSelection.questionCount.expanded
                ),
                depthOfTopic = state.parameterSelection.depthOfTopic.copy(
                    selected = change.depthOfTopic?.toString()
                        ?: state.parameterSelection.depthOfTopic.selected,
                    expanded = change.depthOfTopicExpanded
                        ?: state.parameterSelection.depthOfTopic.expanded
                )
            ),
            furtherSpecification = state.furtherSpecification.copy(
                topicSpecification = change.topicSpecification
                    ?: state.furtherSpecification.topicSpecification,
                goal = change.goal ?: state.furtherSpecification.goal
            )
        )
    }

    private fun startGeneration(state: AssistantUiState) {
        val expectedCount = state.parameterSelection.questionCount.selected.toIntOrNull() ?: 5
        val requestId = UUID.randomUUID().toString()

        expectedQuestionCount = expectedCount
        currentRequestId = requestId
        currentStage = 1
        stage1Draft = ""
        stage2Validated = ""
        sourceMaterialText = ""
        sourceProvidedButEmpty = false
        sourceMetaLine = ""

        _uiState.value = state.copy(
            isGenerating = true,
            assistantCard = state.assistantCard.copy(isLocked = true),
            generationError = null,
            generationResult = ""
        )

        viewModelScope.launch {
            val uri = state.initialDescription.filePath

            if (uri != null) {
                takePersistableReadPermissionIfPossible(uri)
                sourceMetaLine = buildSourceMetaLine(uri)
                Log.i(logTag, sourceMetaLine)
            } else {
                sourceMetaLine = "source meta: no file selected"
                Log.i(logTag, sourceMetaLine)
            }

            sourceMaterialText = withContext(Dispatchers.IO) {
                try {
                    documentTextExtractor.extractText(
                        uri = uri,
                        maxChars = 12000,
                        maxPdfPages = 5
                    )
                } catch (t: Throwable) {
                    Log.e(logTag, "source extract failed: ${t.javaClass.simpleName}: ${t.message}")
                    ""
                }
            }.trim()

            sourceProvidedButEmpty = (uri != null && sourceMaterialText.isBlank())

            if (sourceMaterialText.isNotBlank()) {
                logLong(logTag, "sourceMaterial", sourceMaterialText.take(6000))
            } else {
                Log.w(logTag, "sourceMaterial len=0 (fileSelected=${uri != null})")
            }

            if (currentRequestId != requestId) {
                Log.w(logTag, "request changed while preparing prompt, aborting")
                return@launch
            }

            val prompt1 = buildPromptDraft(state, expectedCount)
            if (prompt1.isBlank()) {
                val s = uiState as AssistantUiState
                _uiState.value = s.copy(
                    isGenerating = false,
                    assistantCard = s.assistantCard.copy(isLocked = false),
                    generationError = "Prompt is blank",
                    generationResult = ""
                )
                currentStage = 0
                currentRequestId = null
                return@launch
            }

            logLong(logTag, "stage=1 prompt", prompt1)
            startServiceGeneration(prompt1, requestId, stage = 1)
        }
    }

    private fun startServiceGeneration(prompt: String, requestId: String, stage: Int) {
        Log.i(logTag, "startService stage=$stage promptChars=${prompt.length}")

        val intent = Intent(appContext, LlmGenerationService::class.java).apply {
            action = LlmGenerationService.ACTION_START
            putExtra(LlmGenerationService.EXTRA_PROMPT, prompt)
            putExtra(LlmGenerationService.EXTRA_REQUEST_ID, requestId)
            putExtra(LlmGenerationService.EXTRA_STAGE, stage)
        }

        appContext.startService(intent)
    }

    private fun buildSourceMaterialBlock(): String {
        if (sourceMaterialText.isNotBlank()) {
            return """
$sourceMetaLine

Source material
Use the following extracted text as your primary reference.
If the source material does not contain enough information, generate a simpler question you are confident is correct.
Do not invent details that contradict the source.

<SOURCE_MATERIAL>
${sourceMaterialText.trim()}
</SOURCE_MATERIAL>

""".trimIndent()
        }

        if (sourceProvidedButEmpty) {
            return """
$sourceMetaLine

Source material: a file was provided, but text extraction returned empty.
This usually means the PDF is scanned (no text layer) or the provider denied reading.
Do not claim facts from the file.
Generate a question only from the Topic and other context.

""".trimIndent()
        }

        return """
$sourceMetaLine

Source material: none provided.

""".trimIndent()
    }

    private fun buildPromptDraft(state: AssistantUiState, expectedCount: Int): String {
        val topic = state.initialDescription.prompt.trim()
        val depth = state.parameterSelection.depthOfTopic.selected.trim()
        val spec = state.furtherSpecification.topicSpecification.trim()
        val goal = state.furtherSpecification.goal.trim()

        return """
You generate multiple choice study questions.

Output language rule
1 If the user explicitly requests German or English in the input, follow that request.
2 Otherwise use the same language as the user input text.
3 Do not mix languages.

Output requirements
Return only the questions in the exact format below. No explanations. No extra text. No markdown.

FORMAT
TITLE: <short title>
Q1: <question>
A: <answer>
B: <answer>
C: <answer>
D: <answer>
CORRECT: <A|B|C|D>

Hard rules
1 Create exactly $expectedCount questions in this format.
2 Each question has exactly 4 answers.
3 Each question has exactly 1 correct answer.
4 All answers must be clearly distinct. No duplicates and no near duplicates.
5 Wrong answers must match the topic but must be clearly wrong. Use realistic misconceptions.
6 Keep answer lengths similar. Do not make the correct answer noticeably longer or shorter.
7 Avoid placeholders like type name or value. Use concrete identifiers and concrete examples.
8 Avoid using double quotes and avoid backticks in questions and answers.
9 Do not include the words "Example", "Mini example", or any sample content. Generate original content only.
10 If more than one answer could be considered partially correct, rewrite the set so only one is unambiguously correct.
11 Prefer questions that are easy to verify: short code output, clear rule application, or a single well known definition.
12 Before writing the final output, silently verify that exactly one option is fully correct and the other three are clearly false. If not, regenerate the question and answers.
13 Do not use vague options like "They are different" or "It depends". Every option must be a specific checkable statement.
14 Avoid tricky edge cases unless Depth is ADVANCED.

Depth guidance
Depth=$depth
If BASIC_OVERVIEW then ask about fundamentals but include a small concrete situation or code like statement without quotes.
If INTERMEDIATE then include typical mistakes, scope, reference vs value behavior, control flow, collections.
If ADVANCED then include edge cases, immutability, equals vs ==, exceptions, generics, basic concurrency concepts.

Quality targets
1 Prefer questions that test understanding and application, not just definitions.
2 The correct answer must be unambiguously correct.
3 Wrong answers must be plausible and on topic, but clearly incorrect.

${buildSourceMaterialBlock()}

Context
Topic: $topic
Additional specification: $spec
Learning goal: $goal

At the very end output exactly the marker <END_JSON>.
""".trimIndent()
    }

    private fun buildPromptValidate(state: AssistantUiState, expectedCount: Int, draft: String): String {
        val topic = state.initialDescription.prompt.trim()
        val depth = state.parameterSelection.depthOfTopic.selected.trim()
        val spec = state.furtherSpecification.topicSpecification.trim()
        val goal = state.furtherSpecification.goal.trim()

        return """
You are an independent reviewer. The draft is likely wrong.

Rules
1 Ignore the draft CORRECT label. Determine the correct option yourself from factual correctness.
2 If none of the four options is fully correct, you MUST rewrite the answers so that exactly one becomes fully correct.
3 If more than one option is partially correct, you MUST rewrite options so only one is unambiguously correct.
4 If you are not confident about the factual correctness, replace the entire question with a simpler one you are confident about.
5 Never use vague answers. Use concrete statements.
6 Output only the final corrected question in the required format.

${buildSourceMaterialBlock()}

Context
Topic: $topic
Depth: $depth
Additional specification: $spec
Learning goal: $goal

DRAFT INPUT
""".trimIndent() + "\n" + draft.trim() + "\n\nAt the very end output exactly the marker <END_JSON>."
    }

    private fun buildPromptJson(expectedCount: Int, validated: String): String {
        return """
Convert the input questions into valid JSON.

Absolute output constraints
1 Output ONLY JSON and then a newline with <END_JSON>.
2 The first character must be { and the last character before the marker must be }.
3 Do not output markdown. Do not use triple backticks. Do not add any intro text.
4 Use double quotes only.
5 correct must be true or false only.
6 No trailing commas.

Strict mapping rules
1 title is the text after "TITLE:".
2 question is the text after "Q1:" (or Q<number>: for more questions).
3 answers are the exact texts after "A:", "B:", "C:", "D:" in this order.
4 Do not replace answers with placeholders. Copy the actual answer texts.
5 The CORRECT letter defines which answer has correct true.
6 questions length must be exactly $expectedCount.
7 Do not translate. Keep the same language as the input.
8 Do not include any text outside the JSON except the marker line.

If any answer text contains a double quote, escape it as \" inside the JSON string.

INPUT
""".trimIndent() + "\n" + validated.trim() + "\n"
    }

    private fun logLong(tag: String, label: String, text: String) {
        val chunkSize = 3500
        Log.i(tag, "$label len=${text.length}")

        if (text.isEmpty()) return
        var i = 0
        var part = 1
        while (i < text.length) {
            val end = (i + chunkSize).coerceAtMost(text.length)
            Log.i(tag, "$label part=$part\n" + text.substring(i, end))
            i = end
            part += 1
        }
    }

    private fun takePersistableReadPermissionIfPossible(uri: Uri) {
        try {
            appContext.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            Log.i(logTag, "persistable read permission ok")
        } catch (t: Throwable) {
            Log.w(logTag, "persistable permission not granted: ${t.javaClass.simpleName}")
        }
    }

    private fun buildSourceMetaLine(uri: Uri): String {
        val mime = appContext.contentResolver.getType(uri)
        var name: String? = null
        var size: Long? = null

        try {
            appContext.contentResolver.query(uri, null, null, null, null)?.use { c ->
                val nameIdx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIdx = c.getColumnIndex(OpenableColumns.SIZE)
                if (c.moveToFirst()) {
                    if (nameIdx >= 0) name = c.getString(nameIdx)
                    if (sizeIdx >= 0) size = c.getLong(sizeIdx)
                }
            }
        } catch (_: Throwable) {
        }

        return "source meta: uri=$uri scheme=${uri.scheme} mime=$mime name=$name size=$size"
    }
}