package de.raum7.local_llm_learning.ui.screens.assistant.types

enum class AssistantPhase {
    INITIAL_DESCRIPTION, // file and/or prompt
    PARAMETER_SELECTION, // number of questions, depth of topic,...
    FURTHER_SPECIFICATION, // narrowing the topic & goal
}