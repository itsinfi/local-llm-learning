package de.raum7.local_llm_learning.data.mock

import de.raum7.local_llm_learning.data.models.Question

val MOCK_QUESTIONS: List<Question> = listOf(
    Question(
        id = 0,
        learningMaterialId = 0,
        question = "Welches Schwert wurde hauptsächlich von Rittern verwendet?",
    ),
    Question(
        id = 1,
        learningMaterialId = 0,
        question = "Wofür war ein Zweihänder bekannt?",
    ),
    Question(
        id = 2,
        learningMaterialId = 0,
        question = "Welches Material wurde häufig für Klingen verwendet?",
    ),
    Question(
        id = 10,
        learningMaterialId = 1,
        question = "Was moduliert bei der Frequenzmodulation den Träger?",
    ),
    Question(
        id = 11,
        learningMaterialId = 1,
        question = "Welcher Synthesizer machte FM populär?",
    ),
    Question(
        id = 12,
        learningMaterialId = 1,
        question = "FM-Synthese ist besonders bekannt für…",
    ),
    Question(
        id = 20,
        learningMaterialId = 2,
        question = "Welche Würfelart wird in D&D am häufigsten benutzt?",
    ),
    Question(
        id = 21,
        learningMaterialId = 2,
        question = "Was ist die Rolle des Dungeon Masters?",
    ),
    Question(
        id = 22,
        learningMaterialId = 2,
        question = "Welche Klasse wirkt typischerweise Zauber?",
    )
)