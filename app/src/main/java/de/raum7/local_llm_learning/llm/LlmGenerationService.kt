// app/src/main/java/de/raum7/local_llm_learning/llm/LlmGenerationService.kt
package de.raum7.local_llm_learning.llm

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock

class LlmGenerationService : LifecycleService() {

    companion object {
        const val ACTION_START = "LLM_START"
        const val ACTION_STOP = "LLM_STOP"

        const val EXTRA_PROMPT = "PROMPT"

        const val EXTRA_REQUEST_ID = "REQUEST_ID"
        const val EXTRA_STAGE = "STAGE"

        const val ACTION_RESULT = "LLM_RESULT"
        const val EXTRA_RESULT_TEXT = "RESULT_TEXT"
        const val EXTRA_RESULT_ERROR = "RESULT_ERROR"

        @Volatile
        private var sharedRepo: AssistantRepository? = null

        @Volatile
        private var sharedReady: Boolean = false

        private val sharedLock = kotlinx.coroutines.sync.Mutex()
    }

    private suspend fun getRepo(): AssistantRepository {
        return sharedLock.withLock {
            val existing = sharedRepo
            if (existing != null) return@withLock existing
            val created = AssistantRepository(applicationContext)
            sharedRepo = created
            created
        }
    }

    private var job: Job? = null
    private var isReady = false

    private val config = LlmConfig(
        contextLength = 4096, //4096 | 2062
        threads = 6,
        temperature = 0.3f,
        topP = 0.95f,
        seed = 0,
        maxTokens = 1000
    )

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        when (intent?.action) {
            ACTION_START -> {
                val prompt = intent.getStringExtra(EXTRA_PROMPT).orEmpty().trim()
                if (prompt.isEmpty()) return START_NOT_STICKY
                if (job != null) return START_NOT_STICKY
                val requestId = intent.getStringExtra(EXTRA_REQUEST_ID).orEmpty()
                val stage = intent.getIntExtra(EXTRA_STAGE, 0)
                startGeneration(prompt, requestId, stage)
            }

            ACTION_STOP -> stopGeneration()
        }

        return START_NOT_STICKY
    }

    private fun startGeneration(prompt: String, requestId: String, stage: Int) {
        job = lifecycleScope.launch(Dispatchers.Default) {
            var finalText: String? = null
            var finalError: String? = null
            try {
                val repo = getRepo()

                if (!sharedReady) {
                    repo.ensureReady(
                        assetModelPath = "models/model.gguf",
                        targetFileName = "model.gguf",
                        config = config
                    )
                    sharedReady = true
                }

                isReady = true

                val result = StringBuilder()
                val endMarker = "<END_JSON>"

                repo.generate(prompt, config).collect { ev ->
                    when (ev) {
                        is LlmEvent.Token -> {
                            result.append(ev.text)
                            val current = result.toString()
                            val idx = current.indexOf(endMarker)
                            if (idx >= 0) {
                                finalText = current.substring(0, idx).trim()
                                throw CancellationException("Stopped after END_JSON")
                            }
                        }

                        is LlmEvent.Completed -> {
                            val out = result.toString()
                            val idx = out.indexOf(endMarker)
                            val jsonOnly = if (idx >= 0) out.substring(0, idx).trim() else out.trim()
                            finalText = jsonOnly
                        }

                        is LlmEvent.Error -> {
                            finalError = ev.message
                            throw CancellationException("Stopped after error")
                        }
                    }
                }
            } catch (_: CancellationException) {
            } catch (t: Throwable) {
                finalError = t.message ?: "Unknown error"
            } finally {
                job = null
                if (finalText != null) {
                    sendResultBroadcast(text = finalText ?: "", error = null, requestId = requestId, stage = stage)
                } else {
                    sendResultBroadcast(text = "", error = finalError ?: "Unknown error", requestId = requestId, stage = stage)
                }
                stopSelf()
            }
        }
    }

    private fun stopGeneration() {
        job?.cancel()
        job = null
        stopSelf()
    }

    private fun sendResultBroadcast(text: String, error: String?, requestId: String, stage: Int) {
        val i = Intent(ACTION_RESULT).apply {
            setPackage(packageName)
            putExtra(EXTRA_RESULT_TEXT, text)
            putExtra(EXTRA_RESULT_ERROR, error)
            putExtra(EXTRA_REQUEST_ID, requestId)
            putExtra(EXTRA_STAGE, stage)
        }
        sendBroadcast(i)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}