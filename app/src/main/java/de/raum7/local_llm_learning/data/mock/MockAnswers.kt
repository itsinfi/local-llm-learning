package de.raum7.local_llm_learning.data.mock

import de.raum7.local_llm_learning.data.models.Answer

val MOCK_ANSWERS: List<Answer> = listOf(
    /*
    Template
    Answer(
        id = x,
        questionId = y,
        answer = "insertHere",
        isCorrect = insertHere
    ),
     */
    Answer(
        id = 0,
        questionId = 3,
        answer = "Langschwert",
        isCorrect = true
    ),
    Answer(
        id = 1,
        questionId = 3,
        answer = "Katana",
        isCorrect = false
    ),
    Answer(
        id = 2,
        questionId = 3,
        answer = "Scimitar",
        isCorrect = false
    ),
    Answer(
        id = 3,
        questionId = 3,
        answer = "Rapier",
        isCorrect = false
    ),

    Answer(
        id = 10,
        questionId = 1,
        answer = "Kampf in engen Gängen",
        isCorrect = false
    ),
    Answer(
        id = 11,
        questionId = 1,
        answer = "Durchbrechen von Pikenformationen",
        isCorrect = true
    ),
    Answer(
        id = 12,
        questionId = 1,
        answer = "Reiterkampf",
        isCorrect = false
    ),
    Answer(
        id = 13,
        questionId = 1,
        answer = "Duellieren",
        isCorrect = false
    ),

    Answer(
        id = 20,
        questionId = 2,
        answer = "Bronze",
        isCorrect = false
    ),
    Answer(
        id = 21,
        questionId = 2,
        answer = "Eisen",
        isCorrect = true
    ),
    Answer(
        id = 22,
        questionId = 2,
        answer = "Aluminium",
        isCorrect = false
    ),
    Answer(
        id = 23,
        questionId = 2,
        answer = "Kupfer",
        isCorrect = false
    ),

    Answer(
        id = 30,
        questionId = 10,
        answer = "Amplitude",
        isCorrect = false
    ),
    Answer(
        id = 31,
        questionId = 10,
        answer = "Frequenz",
        isCorrect = true
    ),
    Answer(
        id = 32,
        questionId = 10,
        answer = "Phase",
        isCorrect = false
    ),
    Answer(
        id = 33,
        questionId = 10,
        answer = "Filter",
        isCorrect = false
    ),

    Answer(
        id = 40,
        questionId = 11,
        answer = "Moog Model D",
        isCorrect = false
    ),
    Answer(
        id = 41,
        questionId = 11,
        answer = "Yamaha DX7",
        isCorrect = true
    ),
    Answer(
        id = 42,
        questionId = 11,
        answer = "Roland TB-303",
        isCorrect = false
    ),
    Answer(
        id = 43,
        questionId = 11,
        answer = "Korg MS-20",
        isCorrect = false
    ),

    Answer(
        id = 50,
        questionId = 12,
        answer = "warme Pads",
        isCorrect = false
    ),
    Answer(
        id = 51,
        questionId = 12,
        answer = "metallische Klänge",
        isCorrect = true
    ),
    Answer(
        id = 52,
        questionId = 12,
        answer = "realistische Streicher",
        isCorrect = false
    ),
    Answer(
        id = 53,
        questionId = 12,
        answer = "analoge Verzerrung",
        isCorrect = false
    ),

    Answer(
        id = 60,
        questionId = 20,
        answer = "W6",
        isCorrect = false
    ),
    Answer(
        id = 61,
        questionId = 20,
        answer = "W8",
        isCorrect = false
    ),
    Answer(
        id = 62,
        questionId = 20,
        answer = "W20",
        isCorrect = true
    ),
    Answer(
        id = 63,
        questionId = 20,
        answer = "W12",
        isCorrect = false
    ),

    Answer(
        id = 70,
        questionId = 21,
        answer = "Ein Spielercharakter",
        isCorrect = false
    ),
    Answer(
        id = 71,
        questionId = 21,
        answer = "Regelerfinder",
        isCorrect = false
    ),
    Answer(
        id = 72,
        questionId = 21,
        answer = "Spielleiter",
        isCorrect = true
    ),
    Answer(
        id = 73,
        questionId = 21,
        answer = "Gegner",
        isCorrect = false
    ),

    Answer(
        id = 80,
        questionId = 22,
        answer = "Krieger",
        isCorrect = false
    ),
    Answer(
        id = 81,
        questionId = 22,
        answer = "Dieb",
        isCorrect = false
    ),
    Answer(
        id = 82,
        questionId = 22,
        answer = "Magier",
        isCorrect = true
    ),
    Answer(
        id = 83,
        questionId = 22,
        answer = "Barbar",
        isCorrect = false
    ),
)