package de.raum7.local_llm_learning.data.mock

import de.raum7.local_llm_learning.data.models.QuizResult

val MOCK_QUIZ_RESULTS: List<QuizResult> = listOf(

    QuizResult(
        id = "q1_r1",
        question = MOCK_LEARNING_MATERIALS[0].questions[0],
        isCorrect = true,
        selectedAnswer = MOCK_LEARNING_MATERIALS[0].questions[0].answers[0],
        correctAnswer = MOCK_LEARNING_MATERIALS[0].questions[0].answers[0],
        elapsedNanoSeconds = 1_000_000_000L * 1 // 1s
    ),

    QuizResult(
        id = "q1_r2",
        question = MOCK_LEARNING_MATERIALS[0].questions[1],
        isCorrect = false,
        selectedAnswer = MOCK_LEARNING_MATERIALS[0].questions[1].answers[0],
        correctAnswer = MOCK_LEARNING_MATERIALS[0].questions[1].answers[1],
        elapsedNanoSeconds = 1_000_000_000L * 2 // 2s
    ),

    QuizResult(
        id = "q1_r3",
        question = MOCK_LEARNING_MATERIALS[0].questions[2],
        isCorrect = true,
        selectedAnswer = MOCK_LEARNING_MATERIALS[0].questions[2].answers[1],
        correctAnswer = MOCK_LEARNING_MATERIALS[0].questions[2].answers[1],
        elapsedNanoSeconds = 1_000_000_000L * 3 // 3s
    ),

    QuizResult(
        id = "q2_r1",
        question = MOCK_LEARNING_MATERIALS[1].questions[0],
        isCorrect = true,
        selectedAnswer = MOCK_LEARNING_MATERIALS[1].questions[0].answers[3],
        correctAnswer = MOCK_LEARNING_MATERIALS[1].questions[0].answers[1],
        elapsedNanoSeconds = 1_000_000_000L * 4 // 4s
    ),

    QuizResult(
        id = "q2_r2",
        question = MOCK_LEARNING_MATERIALS[1].questions[1],
        isCorrect = false,
        selectedAnswer = MOCK_LEARNING_MATERIALS[1].questions[1].answers[0],
        correctAnswer = MOCK_LEARNING_MATERIALS[1].questions[1].answers[1],
        elapsedNanoSeconds = 1_000_000_000L * 5 // 5s
    ),

    QuizResult(
        id = "q2_r3",
        question = MOCK_LEARNING_MATERIALS[1].questions[2],
        isCorrect = true,
        selectedAnswer = MOCK_LEARNING_MATERIALS[1].questions[2].answers[1],
        correctAnswer = MOCK_LEARNING_MATERIALS[1].questions[2].answers[1],
        elapsedNanoSeconds = 1_000_000_000L * 4 // 4s
    ),

    QuizResult(
        id = "q3_r1",
        question = MOCK_LEARNING_MATERIALS[2].questions[0],
        isCorrect = true,
        selectedAnswer = MOCK_LEARNING_MATERIALS[2].questions[0].answers[2],
        correctAnswer = MOCK_LEARNING_MATERIALS[2].questions[0].answers[2],
        elapsedNanoSeconds = 1_000_000_000L * 3 // 3s
    ),

    QuizResult(
        id = "q3_r2",
        question = MOCK_LEARNING_MATERIALS[2].questions[1],
        isCorrect = true,
        selectedAnswer = MOCK_LEARNING_MATERIALS[2].questions[1].answers[2],
        correctAnswer = MOCK_LEARNING_MATERIALS[2].questions[1].answers[2],
        elapsedNanoSeconds = 1_000_000_000L * 2 // 2s
    ),

    QuizResult(
        id = "q3_r3",
        question = MOCK_LEARNING_MATERIALS[2].questions[2],
        isCorrect = true,
        selectedAnswer = MOCK_LEARNING_MATERIALS[2].questions[2].answers[2],
        correctAnswer = MOCK_LEARNING_MATERIALS[2].questions[2].answers[2],
        elapsedNanoSeconds = 1_000_000_000L * 10 // 10s
    ),

)