package com.example.model

data class FlashcardItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val question: String,
    val answer: String,
    val hint: String? = null,
    val category: String? = null,
    val isMastered: Boolean = false
)

data class FlashcardDeck(
    val id: String,
    val title: String,
    val subject: SubjectCategory,
    val description: String,
    val cards: List<FlashcardItem>
) {
    val masteredCount: Int get() = cards.count { it.isMastered }
    val progressPercentage: Float
        get() = if (cards.isEmpty()) 0f else masteredCount.toFloat() / cards.size.toFloat()
}

data class StudyNote(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val subject: SubjectCategory,
    val updatedAt: Long = System.currentTimeMillis(),
    val tags: List<String> = emptyList()
)
