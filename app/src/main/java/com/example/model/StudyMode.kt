package com.example.model

enum class StudyMode(
    val title: String,
    val subtitle: String,
    val iconName: String,
    val systemPromptDirective: String
) {
    STEP_BY_STEP(
        title = "Step-by-Step",
        subtitle = "Numbered logical stages with formulas and checks",
        iconName = "format_list_numbered",
        systemPromptDirective = "Break down the solution into crystal-clear numbered steps. For each step, provide: 1) What we are doing, 2) The reasoning/formula, 3) The calculation or logical deduction. Conclude with a final boxed answer, a key takeaway / pro-tip, and a self-check test question."
    ),
    DEEP_TUTOR(
        title = "Deep Tutor",
        subtitle = "Intuitive explanations, analogies & fundamental concepts",
        iconName = "psychology",
        systemPromptDirective = "Act as an encouraging, world-class Socratic tutor. Explain the core intuition, why this concept matters, use an intuitive real-world analogy, demystify confusing terms, and build deep conceptual understanding before presenting the solution."
    ),
    QUICK_SOLVE(
        title = "Quick Summary",
        subtitle = "High-yield summary, core answer & fast review notes",
        iconName = "bolt",
        systemPromptDirective = "Provide a high-yield, concise answer immediately. Highlight the core result, bullet-pointed key concepts, and essential formulas in a rapid-review format."
    ),
    FLASHCARDS(
        title = "Card Generator",
        subtitle = "Converts topic into study questions & memory recall cards",
        iconName = "style",
        systemPromptDirective = "Extract key question-and-answer pairs and definitions from this topic suitable for flashcards and active recall."
    )
}
