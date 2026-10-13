package com.example.model

enum class ResourceType {
    CHEAT_SHEET,
    FORMULA_SHEET,
    STUDY_GUIDE,
    PROBLEM_SET
}

data class ResourceSection(
    val title: String,
    val content: String,
    val formulaOrHighlight: String? = null,
    val example: String? = null
)

data class StudyResource(
    val id: String,
    val title: String,
    val subject: SubjectCategory,
    val type: ResourceType,
    val summary: String,
    val readTimeMinutes: Int,
    val sections: List<ResourceSection>,
    val quickTips: List<String> = emptyList(),
    val isBookmarked: Boolean = false
)
