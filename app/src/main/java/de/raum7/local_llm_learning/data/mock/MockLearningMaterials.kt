package de.raum7.local_llm_learning.data.mock

import de.raum7.local_llm_learning.data.models.LearningMaterial

val MOCK_LEARNING_MATERIALS: List<LearningMaterial> = listOf(

    LearningMaterial(
        id = 3,
        title = "Mittelalterliche Schwerter",
        progress = 0.33, // left for previews, doesn't compute with default values for related questions and will be updated after submitting first answer
    ),

    LearningMaterial(
        id = 1,
        title = "Frequenzmodulation bei Synthesizern",
        progress = 0.66, // left for previews, doesn't compute with default values for related questions and will be updated after submitting first answer
    ),

    LearningMaterial(
        id = 2,
        title = "Dungeons and Dragons",
    ),

    LearningMaterial(
        id = 4,
        title = "Alles zusammen",
    )
)