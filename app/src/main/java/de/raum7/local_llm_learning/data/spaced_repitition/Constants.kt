package de.raum7.local_llm_learning.data.spaced_repitition

const val ENFORCED_DELAY: Int = 1
const val A: Double = 0.1 // weighting constant
const val B: Double = 1.1 // weighting constant
const val R: Double = 2.0 // weighting constant
const val W: Double = 20.0 // priority increment for an error
val DEFAULT_PRIORITY: Double? = null

// mastery criteria
const val MAX_RESPONSE_TIME: Double = 3.0 // response time user must achieve for question to be counted towards progression
const val TARGET_STREAK: Int = 3 // streak of correct answers that must be given in less max_response_time seconds in order to be filtered out