package de.raum7.local_llm_learning.ui.screens.assistant.types

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.screens.assistant.DEFAULT_DEPTH_OF_TOPIC

enum class DepthOfTopic(
    @StringRes val labelRes: Int,
) {
    BASIC_OVERVIEW(R.string.assistant_depth_of_topic_basic_overview),
    INTERMEDIATE(R.string.assistant_depth_of_topic_intermediate),
    DEEP_DIVE(R.string.assistant_depth_of_topic_deep_dive);

    @Composable
    fun getLabel(): String = stringResource(labelRes)

    companion object {
        fun fromString(value: String): DepthOfTopic {
            return try {
                valueOf(value)
            } catch (e: IllegalArgumentException) {
                DEFAULT_DEPTH_OF_TOPIC
            }
        }

        @Composable
        fun getTranslations(): Map<String, String> {
            return DepthOfTopic.entries.associate { depthOfTopic: DepthOfTopic ->
                depthOfTopic.toString() to depthOfTopic.getLabel()
            }
        }
    }
}