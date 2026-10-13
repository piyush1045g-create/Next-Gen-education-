package com.example.model

enum class MessageSender {
    USER,
    GENIE
}

data class StepDetail(
    val stepNumber: Int,
    val title: String,
    val body: String,
    val formulaOrKeyPoint: String? = null
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val content: String,
    val subject: SubjectCategory = SubjectCategory.ALL,
    val timestamp: Long = System.currentTimeMillis(),
    val steps: List<StepDetail> = emptyList(),
    val formulas: List<String> = emptyList(),
    val proTip: String? = null,
    val suggestedFollowUps: List<String> = emptyList(),
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
