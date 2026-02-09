package de.raum7.local_llm_learning.data.mock

import de.raum7.local_llm_learning.data.models.LearningMaterial

val MOCK_LEARNING_MATERIALS: List<LearningMaterial> = listOf(

    LearningMaterial(
        id = 0,
        title = "Mittelalterliche Schwerter",
        progress = 0.33,
    ),

    LearningMaterial(
        id = 1,
        title = "Frequenzmodulation bei Synthesizern",
        progress = 0.66,
    ),

    LearningMaterial(
        id = 2,
        title = "Dungeons and Dragons",
        progress = 0.0,
    )
)