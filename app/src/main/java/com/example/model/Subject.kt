package com.example.model

import androidx.compose.ui.graphics.Color

enum class SubjectCategory {
    ALL,
    MATH,
    SCIENCE,
    HISTORY,
    LITERATURE,
    COMPUTER_SCIENCE,
    LANGUAGES,
    BUSINESS
}

data class Subject(
    val category: SubjectCategory,
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val accentColorHex: Long,
    val samplePrompts: List<String>
) {
    val accentColor: Color get() = Color(accentColorHex)

    companion object {
        val ALL_SUBJECTS = listOf(
            Subject(
                category = SubjectCategory.MATH,
                title = "Mathematics",
                subtitle = "Algebra, Calculus, Geometry, Stats",
                iconEmoji = "📐",
                accentColorHex = 0xFF6366F1,
                samplePrompts = listOf(
                    "Solve for x: 3x² - 12x + 9 = 0",
                    "How to evaluate the integral ∫(3x² + 2x) dx?",
                    "Explain Pythagorean theorem and give 3 examples",
                    "How do I find eigenvalues of a 2x2 matrix?"
                )
            ),
            Subject(
                category = SubjectCategory.SCIENCE,
                title = "Science",
                subtitle = "Physics, Chemistry, Biology",
                iconEmoji = "🔬",
                accentColorHex = 0xFF06B6D4,
                samplePrompts = listOf(
                    "Explain the stages of photosynthesis step by step",
                    "How to balance: Fe + O₂ → Fe₂O₃?",
                    "Derive and explain Newton's 2nd Law (F = ma)",
                    "How does DNA transcription and translation work?"
                )
            ),
            Subject(
                category = SubjectCategory.HISTORY,
                title = "History & Civics",
                subtitle = "World, US, Governance, Revolutions",
                iconEmoji = "🏛️",
                accentColorHex = 0xFFF59E0B,
                samplePrompts = listOf(
                    "What were the 4 main causes of World War I?",
                    "Explain the system of checks and balances in US Gov",
                    "Analyze the economic impact of the Industrial Revolution",
                    "Summarize the French Revolution timeline"
                )
            ),
            Subject(
                category = SubjectCategory.LITERATURE,
                title = "Literature & English",
                subtitle = "Essays, Literary Devices, Grammar",
                iconEmoji = "📚",
                accentColorHex = 0xFFEC4899,
                samplePrompts = listOf(
                    "How do I structure a 5-paragraph argumentative essay?",
                    "Explain the difference between metaphor, simile, and analogy",
                    "Analyze the theme of ambition in Shakespeare's Macbeth",
                    "Active vs. Passive voice: rules and corrections"
                )
            ),
            Subject(
                category = SubjectCategory.COMPUTER_SCIENCE,
                title = "Computer Science",
                subtitle = "Algorithms, Python, Data Structures",
                iconEmoji = "💻",
                accentColorHex = 0xFF10B981,
                samplePrompts = listOf(
                    "Explain Binary Search algorithm with time complexity",
                    "How do recursive functions work with a call stack?",
                    "What is Big-O notation and how to calculate it?",
                    "Difference between Array and Linked List in memory"
                )
            ),
            Subject(
                category = SubjectCategory.LANGUAGES,
                title = "World Languages",
                subtitle = "Spanish, French, Grammar & Vocab",
                iconEmoji = "🌍",
                accentColorHex = 0xFF8B5CF6,
                samplePrompts = listOf(
                    "Explain Spanish 'Por' vs 'Para' with clear examples",
                    "How do French verb conjugations work for regular -er verbs?",
                    "Explain German case system (Nominativ, Akkusativ, Dativ)",
                    "Japanese hiragana vs katakana differences"
                )
            ),
            Subject(
                category = SubjectCategory.BUSINESS,
                title = "Economics & Business",
                subtitle = "Micro, Macro, Accounting, Finance",
                iconEmoji = "📈",
                accentColorHex = 0xFF14B8A6,
                samplePrompts = listOf(
                    "How does Supply and Demand reach market equilibrium?",
                    "Explain Opportunity Cost with 3 real-world scenarios",
                    "What is the difference between Fiscal and Monetary policy?",
                    "How to read an Income Statement vs Balance Sheet"
                )
            )
        )

        fun find(category: SubjectCategory): Subject {
            return ALL_SUBJECTS.firstOrNull { it.category == category } ?: ALL_SUBJECTS.first()
        }
    }
}
