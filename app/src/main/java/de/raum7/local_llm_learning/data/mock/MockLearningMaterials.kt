package de.raum7.local_llm_learning.data.mock

import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.data.models.Question

val MOCK_LEARNING_MATERIALS: List<LearningMaterial> = listOf(

    LearningMaterial(
        id = "lm1",
        title = "Mittelalterliche Schwerter",
        progress = 0.33,
        questions = listOf(
            Question(
                id = "q1",
                question = "Welches Schwert wurde hauptsächlich von Rittern verwendet?",
                answers = listOf(
                    Answer("a1", "Langschwert", true),
                    Answer("a2", "Katana", false),
                    Answer("a3", "Scimitar", false),
                    Answer("a4", "Rapier", false)
                )
            ),
            Question(
                id = "q2",
                question = "Wofür war ein Zweihänder bekannt?",
                answers = listOf(
                    Answer("a1", "Kampf in engen Gängen", false),
                    Answer("a2", "Durchbrechen von Pikenformationen", true),
                    Answer("a3", "Reiterkampf", false),
                    Answer("a4", "Duellieren", false)
                )
            ),
            Question(
                id = "q3",
                question = "Welches Material wurde häufig für Klingen verwendet?",
                answers = listOf(
                    Answer("a1", "Bronze", false),
                    Answer("a2", "Eisen", true),
                    Answer("a3", "Aluminium", false),
                    Answer("a4", "Kupfer", false)
                )
            )
        )
    ),

    LearningMaterial(
        id = "lm2",
        title = "Frequenzmodulation bei Synthesizern",
        progress = 0.66,
        questions = listOf(
            Question(
                id = "q1",
                question = "Was moduliert bei der Frequenzmodulation den Träger?",
                answers = listOf(
                    Answer("a1", "Amplitude", false),
                    Answer("a2", "Frequenz", true),
                    Answer("a3", "Phase", false),
                    Answer("a4", "Filter", false)
                )
            ),
            Question(
                id = "q2",
                question = "Welcher Synthesizer machte FM populär?",
                answers = listOf(
                    Answer("a1", "Moog Model D", false),
                    Answer("a2", "Yamaha DX7", true),
                    Answer("a3", "Roland TB-303", false),
                    Answer("a4", "Korg MS-20", false)
                )
            ),
            Question(
                id = "q3",
                question = "FM-Synthese ist besonders bekannt für…",
                answers = listOf(
                    Answer("a1", "warme Pads", false),
                    Answer("a2", "metallische Klänge", true),
                    Answer("a3", "realistische Streicher", false),
                    Answer("a4", "analoge Verzerrung", false)
                )
            )
        )
    ),

    LearningMaterial(
        id = "lm3",
        title = "Dungeons and Dragons",
        progress = 0.0,
        questions = listOf(
            Question(
                id = "q1",
                question = "Welche Würfelart wird in D&D am häufigsten benutzt?",
                answers = listOf(
                    Answer("a1", "W6", false),
                    Answer("a2", "W8", false),
                    Answer("a3", "W20", true),
                    Answer("a4", "W12", false)
                )
            ),
            Question(
                id = "q2",
                question = "Was ist die Rolle des Dungeon Masters?",
                answers = listOf(
                    Answer("a1", "Ein Spielercharakter", false),
                    Answer("a2", "Regelerfinder", false),
                    Answer("a3", "Spielleiter", true),
                    Answer("a4", "Gegner", false)
                )
            ),
            Question(
                id = "q3",
                question = "Welche Klasse wirkt typischerweise Zauber?",
                answers = listOf(
                    Answer("a1", "Krieger", false),
                    Answer("a2", "Dieb", false),
                    Answer("a3", "Magier", true),
                    Answer("a4", "Barbar", false)
                )
            )
        )
    )
)