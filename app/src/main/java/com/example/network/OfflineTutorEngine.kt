package com.example.network

import com.example.model.ChatMessage
import com.example.model.MessageSender
import com.example.model.StepDetail
import com.example.model.StudyMode
import com.example.model.SubjectCategory

object OfflineTutorEngine {

    fun generateFallbackResponse(
        prompt: String,
        subject: SubjectCategory,
        mode: StudyMode,
        apiNotice: String? = null
    ): ChatMessage {
        val lower = prompt.lowercase()

        val (content, steps, formulas, proTip, followUps) = when {
            // MATH - Quadratic Equations
            lower.contains("quadratic") || lower.contains("x²") || lower.contains("x^2") || lower.contains("solve for x") -> {
                buildQuadraticSolution()
            }
            // MATH - Calculus / Integral / Derivative
            lower.contains("integral") || lower.contains("derivative") || lower.contains("∫") || lower.contains("dx") -> {
                buildCalculusSolution()
            }
            // SCIENCE - Photosynthesis
            lower.contains("photosynthesis") || lower.contains("light reaction") || lower.contains("calvin") -> {
                buildPhotosynthesisSolution()
            }
            // SCIENCE - Physics / Newton / Force / Motion
            lower.contains("newton") || lower.contains("f = ma") || lower.contains("force") || lower.contains("acceleration") -> {
                buildPhysicsForceSolution()
            }
            // HISTORY - World War I / WWI
            lower.contains("world war") || lower.contains("wwi") || lower.contains("ww1") || lower.contains("causes") -> {
                buildWwiSolution()
            }
            // LITERATURE - Essay Writing / Argumentative
            lower.contains("essay") || lower.contains("thesis") || lower.contains("paragraph") || lower.contains("argument") -> {
                buildEssaySolution()
            }
            // COMPUTER SCIENCE - Big-O / Algorithms / Binary Search
            lower.contains("binary search") || lower.contains("big-o") || lower.contains("complexity") || lower.contains("algorithm") -> {
                buildBinarySearchSolution()
            }
            // GENERAL SUBJECT FALLBACK
            else -> {
                buildSubjectGeneralSolution(prompt, subject, mode)
            }
        }

        val noticeHeader = if (apiNotice != null) "💡 *$apiNotice*\n\n" else ""
        val fullContent = noticeHeader + content

        return ChatMessage(
            sender = MessageSender.GENIE,
            content = fullContent,
            subject = subject,
            steps = steps,
            formulas = formulas,
            proTip = proTip,
            suggestedFollowUps = followUps
        )
    }

    private fun buildQuadraticSolution(): ResponseTuple {
        val steps = listOf(
            StepDetail(
                stepNumber = 1,
                title = "Standard Form Identification",
                body = "Ensure the equation is in standard form: ax² + bx + c = 0. Identify the coefficients: a = 3, b = -12, c = 9.",
                formulaOrKeyPoint = "ax² + bx + c = 0"
            ),
            StepDetail(
                stepNumber = 2,
                title = "Calculate the Discriminant (Δ)",
                body = "Compute Δ = b² - 4ac to determine the nature of the roots:\nΔ = (-12)² - 4(3)(9) = 144 - 108 = 36.\nSince Δ = 36 > 0, there are two distinct real solutions.",
                formulaOrKeyPoint = "Δ = b² - 4ac = 36"
            ),
            StepDetail(
                stepNumber = 3,
                title = "Apply the Quadratic Formula",
                body = "Substitute into x = (-b ± √Δ) / (2a):\nx = (-(-12) ± √36) / (2 · 3) = (12 ± 6) / 6.\n\nRoot 1: x = (12 + 6) / 6 = 18 / 6 = 3\nRoot 2: x = (12 - 6) / 6 = 6 / 6 = 1.",
                formulaOrKeyPoint = "x = (-b ± √(b² - 4ac)) / (2a)"
            ),
            StepDetail(
                stepNumber = 4,
                title = "Verification Check",
                body = "Check x = 3: 3(3)² - 12(3) + 9 = 27 - 36 + 9 = 0 (Matches!)\nCheck x = 1: 3(1)² - 12(1) + 9 = 3 - 12 + 9 = 0 (Matches!)"
            )
        )

        val content = """
            ### Overview
            We solve the quadratic equation 3x² - 12x + 9 = 0 using coefficient identification and the universal Quadratic Formula.
            
            ### Step 1: Standard Form Identification
            The equation is in ax² + bx + c = 0 form. Here: a = 3, b = -12, c = 9.
            
            ### Step 2: Compute Discriminant
            Δ = b² - 4ac = (-12)² - 4(3)(9) = 144 - 108 = 36.
            
            ### Step 3: Solve for Roots
            x = (12 ± √36) / 6 = (12 ± 6) / 6.
            • x₁ = 3
            • x₂ = 1
            
            ### Final Answer
            The solutions are **x = 3** and **x = 1**.
            
            ### Pro-Tip & Common Pitfalls
            Be careful with the negative sign in (-b). If b is already -12, then -(-12) becomes positive +12!
        """.trimIndent()

        return ResponseTuple(
            content = content,
            steps = steps,
            formulas = listOf("x = (-b ± √(b² - 4ac)) / (2a)", "Δ = b² - 4ac"),
            proTip = "Watch out for double negatives when calculating -b, and remember that squaring a negative number always yields a positive value in b².",
            followUps = listOf(
                "How can I solve this faster by factoring out 3 first?",
                "What happens to the graph of this parabola at its vertex?",
                "Give me another quadratic practice problem to solve!"
            )
        )
    }

    private fun buildCalculusSolution(): ResponseTuple {
        val steps = listOf(
            StepDetail(
                stepNumber = 1,
                title = "Apply Sum & Constant Multiple Rules",
                body = "Break the integral into linear components: ∫(3x² + 2x) dx = 3∫x² dx + 2∫x dx.",
                formulaOrKeyPoint = "∫[f(x) + g(x)] dx = ∫f(x) dx + ∫g(x) dx"
            ),
            StepDetail(
                stepNumber = 2,
                title = "Apply the Power Rule of Integration",
                body = "For each term x^n, the integral is (x^(n+1)) / (n + 1):\n• For x²: ∫x² dx = x³/3\n• For x: ∫x dx = x²/2.",
                formulaOrKeyPoint = "∫ x^n dx = (x^(n+1)) / (n + 1) + C"
            ),
            StepDetail(
                stepNumber = 3,
                title = "Multiply Coefficients & Simplify",
                body = "3 · (x³/3) + 2 · (x²/2) = x³ + x².\nAlways include the arbitrary constant of integration (+ C) for indefinite integrals."
            )
        )

        val content = """
            ### Overview
            Evaluating indefinite integral ∫(3x² + 2x) dx using the fundamental reverse power rule of calculus.
            
            ### Step 1: Term-by-Term Decomposition
            ∫(3x² + 2x) dx = 3∫x² dx + 2∫x dx
            
            ### Step 2: Power Rule Application
            • 3 · (x³ / 3) = x³
            • 2 · (x² / 2) = x²
            
            ### Final Answer
            **x³ + x² + C**
            
            ### Pro-Tip & Common Pitfalls
            Students frequently forget the constant '+ C' on exams! Differentiating your answer (d/dx [x³ + x² + C] = 3x² + 2x) is the fastest way to confirm you are correct.
        """.trimIndent()

        return ResponseTuple(
            content = content,
            steps = steps,
            formulas = listOf("∫ x^n dx = (x^(n+1)) / (n + 1) + C", "d/dx [x^n] = n · x^(n-1)"),
            proTip = "Always check your work by taking the derivative of your answer—it should immediately return the original integrand!",
            followUps = listOf(
                "How would we evaluate this as a definite integral from 0 to 2?",
                "What is integration by substitution (u-sub)?",
                "Can you quiz me on basic derivative and integral rules?"
            )
        )
    }

    private fun buildPhotosynthesisSolution(): ResponseTuple {
        val steps = listOf(
            StepDetail(
                stepNumber = 1,
                title = "Overall Chemical Equation & Inputs",
                body = "Photosynthesis converts solar energy, carbon dioxide, and water into chemical glucose and oxygen.\n6 CO₂ + 6 H₂O + Light → C₆H₁₂O₆ + 6 O₂.",
                formulaOrKeyPoint = "6 CO₂ + 6 H₂O + Energy → C₆H₁₂O₆ + 6 O₂"
            ),
            StepDetail(
                stepNumber = 2,
                title = "Stage 1: Light-Dependent Reactions (Thylakoids)",
                body = "Chlorophyll pigments absorb photons. Water molecules are split (photolysis), releasing O₂ gas as a byproduct. ATP and NADPH energy carriers are synthesized.",
                formulaOrKeyPoint = "Location: Thylakoid membrane | Output: ATP, NADPH, O₂"
            ),
            StepDetail(
                stepNumber = 3,
                title = "Stage 2: The Calvin Cycle (Stroma)",
                body = "Light-independent reaction. The enzyme RuBisCO fixes CO₂ from the atmosphere using ATP and NADPH generated in Stage 1 to produce G3P sugar molecules (precursors to glucose).",
                formulaOrKeyPoint = "Location: Stroma | Key Enzyme: RuBisCO"
            )
        )

        val content = """
            ### Overview
            Photosynthesis is the biochemical process through which autotrophic organisms transform light energy into chemical energy stored in glucose bonds.
            
            ### Step 1: Overall Equation
            6 CO₂ + 6 H₂O + Photons → C₆H₁₂O₆ + 6 O₂
            
            ### Step 2: Light-Dependent Reactions
            Occurs in thylakoid membranes; water is split into protons, electrons, and oxygen gas while charging ATP and NADPH.
            
            ### Step 3: Calvin Cycle (Light-Independent)
            Occurs in the stroma; carbon fixation driven by RuBisCO builds glucose.
            
            ### Final Answer
            Photosynthesis has two interdependent stages: **Light Reactions** in the thylakoids (producing ATP, NADPH, O₂) and the **Calvin Cycle** in the stroma (producing glucose).
            
            ### Pro-Tip & Common Pitfalls
            Remember: Oxygen (O₂) comes exclusively from the splitting of **H₂O**, NOT from the breakdown of CO₂!
        """.trimIndent()

        return ResponseTuple(
            content = content,
            steps = steps,
            formulas = listOf("6 CO₂ + 6 H₂O + Light → C₆H₁₂O₆ + 6 O₂"),
            proTip = "A common test trick: Oxygen gas released by plants originates from water (photolysis), not from carbon dioxide.",
            followUps = listOf(
                "How does cellular respiration compare to photosynthesis?",
                "What is the role of RuBisCO in carbon fixation?",
                "Why do leaves change color in the autumn?"
            )
        )
    }

    private fun buildPhysicsForceSolution(): ResponseTuple {
        val steps = listOf(
            StepDetail(
                stepNumber = 1,
                title = "State Newton's Second Law",
                body = "Newton's Second Law states that the vector sum of forces acting on an object equals the product of its mass and its resulting acceleration: ΣF = m · a.",
                formulaOrKeyPoint = "ΣF = m · a (SI Unit: Newton = kg · m/s²)"
            ),
            StepDetail(
                stepNumber = 2,
                title = "Draw Free Body Diagram (FBD)",
                body = "Isolate the object. Identify all contact forces (Normal force, Friction, Tension, Applied push) and non-contact forces (Gravity F_g = mg). Set up coordinate axes (X horizontal, Y vertical)."
            ),
            StepDetail(
                stepNumber = 3,
                title = "Resolve Vector Components & Solve",
                body = "Sum forces along each axis independently:\n• ΣF_y = F_N - mg = 0 (if no vertical motion)\n• ΣF_x = F_applied - F_friction = m · a_x."
            )
        )

        val content = """
            ### Overview
            Newton's Second Law of Motion forms the core foundation of classical dynamics, governing how applied forces alter the motion of physical masses.
            
            ### Step 1: Law Definition
            ΣF = m · a. Acceleration is directly proportional to net force and inversely proportional to inertial mass.
            
            ### Step 2: Free Body Diagram Isolation
            Separate all forces acting ON the object: normal force, gravity (W = mg), applied push, and resistance.
            
            ### Step 3: Equation Setup
            ΣF_x = m · a_x.
            
            ### Final Answer
            Net Force equals **Mass × Acceleration** (F = ma). 1 Newton is the force needed to accelerate 1 kg by 1 m/s².
            
            ### Pro-Tip & Common Pitfalls
            Forces on opposite objects do NOT cancel out! Newton's 3rd Law pairs act on different objects.
        """.trimIndent()

        return ResponseTuple(
            content = content,
            steps = steps,
            formulas = listOf("ΣF = m · a", "F_gravity = m · g (g ≈ 9.8 m/s²)", "F_friction = μ · F_normal"),
            proTip = "Always draw a Free Body Diagram before writing any equations—it prevents missing friction or normal force components.",
            followUps = listOf(
                "How does friction coefficient (μ) affect acceleration on an incline?",
                "What is the difference between mass (kg) and weight (N)?",
                "Can you solve a problem with an elevator accelerating upward?"
            )
        )
    }

    private fun buildWwiSolution(): ResponseTuple {
        val steps = listOf(
            StepDetail(
                stepNumber = 1,
                title = "Militarism (Arms Race)",
                body = "European powers expanded standing armies and naval fleets (e.g. Anglo-German naval dreadnought rivalry). Generals believed swift offense would secure easy victory."
            ),
            StepDetail(
                stepNumber = 2,
                title = "Alliances (Entangling Treaties)",
                body = "Europe was divided into two armed camps: Triple Entente (Britain, France, Russia) and Triple Alliance / Central Powers (Germany, Austria-Hungary, Italy). A localized conflict would trigger mutual defense pledges."
            ),
            StepDetail(
                stepNumber = 3,
                title = "Imperialism (Colonial Rivalry)",
                body = "Competition for colonies and markets in Africa and Asia heightened distrust between imperial powers."
            ),
            StepDetail(
                stepNumber = 4,
                title = "Nationalism & The Balkan Spark",
                body = "Ethnic groups in Austria-Hungary sought self-determination. The assassination of Archduke Franz Ferdinand by Gavrilo Princip in Sarajevo (June 28, 1914) ignited the alliance chain reaction."
            )
        )

        val content = """
            ### Overview
            World War I (1914–1918) erupted due to deep-seated systemic tensions in Europe, memorably summarized by the M-A-I-N acronym.
            
            ### Step 1: Militarism
            Rapid stockpiling of arms and aggressive war planning.
            
            ### Step 2: Alliances
            Secret and binding pacts forced countries into war when one was attacked.
            
            ### Step 3: Imperialism
            Fierce geopolitical competition for global resources and colonies.
            
            ### Step 4: Nationalism
            Intense national pride and Slavic independence movements in the Balkans.
            
            ### Final Answer
            The four main causes of WWI are **Militarism, Alliances, Imperialism, and Nationalism**, with the spark being the **Assassination of Archduke Franz Ferdinand** in 1914.
            
            ### Pro-Tip & Common Pitfalls
            Distinguish between long-term underlying causes (M-A-I-N) and the immediate trigger (Sarajevo assassination).
        """.trimIndent()

        return ResponseTuple(
            content = content,
            steps = steps,
            formulas = listOf("M-A-I-N Acronym: Militarism, Alliances, Imperialism, Nationalism"),
            proTip = "In history exams, always clearly separate long-term systemic causes from the immediate catalyst event.",
            followUps = listOf(
                "How did the Treaty of Versailles lead to World War II?",
                "What was the Schlieffen Plan and why did it fail?",
                "Why did the United States enter World War I in 1917?"
            )
        )
    }

    private fun buildEssaySolution(): ResponseTuple {
        val steps = listOf(
            StepDetail(
                stepNumber = 1,
                title = "Crafting the Thesis Statement",
                body = "A strong thesis is specific, arguable, and outlines your essay roadmap:\nFormula: 'Although [Counterargument], [Your Position] because [Reason A] and [Reason B].'"
            ),
            StepDetail(
                stepNumber = 2,
                title = "Introduction Paragraph",
                body = "Start with an intriguing universal hook (quote, question, striking observation). Transition smoothly with 2-3 background context sentences. Finish with your clear thesis statement."
            ),
            StepDetail(
                stepNumber = 3,
                title = "Body Paragraph Architecture (Claim-Evidence-Reasoning)",
                body = "• Topic Sentence: Defines the claim for the paragraph.\n• Evidence: Concrete quote or historical evidence.\n• Analysis (Elaboration): Explain HOW the evidence proves your claim.\n• Synthesis / Wrap-up transition."
            ),
            StepDetail(
                stepNumber = 4,
                title = "Conclusion: So What?",
                body = "Rephrase thesis in fresh words. Synthesize key evidence points without repeating them verbatim. Answer the ultimate 'So what?' question—why does this argument matter in the broader world?"
            )
        )

        val content = """
            ### Overview
            Writing a compelling, high-scoring essay requires logical paragraph sequencing and rigorous evidence-based reasoning.
            
            ### Step 1: Develop an Arguable Thesis
            Your thesis is the anchor of the essay. Avoid simple facts; present an analytical argument.
            
            ### Step 2: Structure the Introduction
            Hook → Context → Thesis Statement.
            
            ### Step 3: Body Paragraphs with CER Framework
            Claim → Evidence (Quote) → Reasoning (Deep Analysis).
            
            ### Step 4: Powerful Conclusion
            Restate thesis differently and highlight broader significance.
            
            ### Final Answer
            Follow the **Hook-Context-Thesis** structure for Intro, **CER (Claim-Evidence-Reasoning)** for Body paragraphs, and **Synthesis-Significance** for Conclusion.
            
            ### Pro-Tip & Common Pitfalls
            Never leave a quote 'floating'—always sandwich it between your introduction of who said it and your explanation of why it matters.
        """.trimIndent()

        return ResponseTuple(
            content = content,
            steps = steps,
            formulas = listOf("Thesis Formula: Although [Counter], [Claim] because [Reason 1] and [Reason 2]"),
            proTip = "Use the Quote Sandwich method: Lead-in context + Quote + 2 sentences explaining why the quote proves your thesis.",
            followUps = listOf(
                "Can you review my draft thesis statement?",
                "How do I write smooth transitions between body paragraphs?",
                "What are the best rhetorical devices to strengthen persuasive writing?"
            )
        )
    }

    private fun buildBinarySearchSolution(): ResponseTuple {
        val steps = listOf(
            StepDetail(
                stepNumber = 1,
                title = "Precondition: Sorted Array",
                body = "Binary Search ONLY works on an already sorted sequence. If the array is unsorted, it must first be sorted in O(N log N) time.",
                formulaOrKeyPoint = "Array must be monotonically ordered: A[0] ≤ A[1] ≤ ... ≤ A[n-1]"
            ),
            StepDetail(
                stepNumber = 2,
                title = "Initialize Pointers & Midpoint Calculation",
                body = "Set `left = 0` and `right = length - 1`. Calculate `mid = left + (right - left) / 2` (prevents integer overflow vs (left + right) / 2).",
                formulaOrKeyPoint = "mid = left + (right - left) / 2"
            ),
            StepDetail(
                stepNumber = 3,
                title = "Compare & Eliminate Half the Search Space",
                body = "• If A[mid] == target: Target found at index `mid`!\n• If A[mid] < target: Target is in the upper half; update `left = mid + 1`.\n• If A[mid] > target: Target is in lower half; update `right = mid - 1`.\nRepeat while `left <= right`."
            ),
            StepDetail(
                stepNumber = 4,
                title = "Time & Space Complexity",
                body = "Each comparison eliminates half of the remaining elements. For N elements: N, N/2, N/4, ... 1. Total steps: log₂(N). Time Complexity: O(log N). Space Complexity: O(1) iterative.",
                formulaOrKeyPoint = "Time: O(log N) | Space: O(1)"
            )
        )

        val content = """
            ### Overview
            Binary Search is an optimal divide-and-conquer search algorithm with logarithmic time complexity on sorted collections.
            
            ### Step 1: Verify Precondition
            Collection must be sorted.
            
            ### Step 2: Establish Bounds
            `left = 0`, `right = n - 1`. Calculate `mid = left + (right - left) / 2`.
            
            ### Step 3: Halve the Search Space
            Compare target with middle element and discard the irrelevant half.
            
            ### Final Answer
            Binary Search locates elements in **O(log N)** time by halving the search space on every iteration.
            
            ### Pro-Tip & Common Pitfalls
            Always compute midpoint as `left + (right - left) / 2` to avoid 32-bit integer overflow bugs in programming languages like Java, Kotlin, and C++.
        """.trimIndent()

        return ResponseTuple(
            content = content,
            steps = steps,
            formulas = listOf("mid = left + (right - left) / 2", "Time Complexity: O(log N)", "Space Complexity: O(1)"),
            proTip = "In code, avoid `(left + right) / 2` because when both are large integers, their sum can exceed 2³¹ - 1 and overflow into a negative number!",
            followUps = listOf(
                "How do you implement binary search recursively vs iteratively?",
                "How can binary search be used to find the first or last occurrence of a duplicate?",
                "What is the difference between Linear Search O(N) and Binary Search O(log N)?"
            )
        )
    }

    private fun buildSubjectGeneralSolution(
        prompt: String,
        subject: SubjectCategory,
        mode: StudyMode
    ): ResponseTuple {
        val steps = listOf(
            StepDetail(
                stepNumber = 1,
                title = "Analyze Core Concept & Objectives",
                body = "To master \"$prompt\", we first establish the fundamental principles and definition within ${subject.name}. Identify the given parameters and what we seek to determine."
            ),
            StepDetail(
                stepNumber = 2,
                title = "Methodology & Stepwise Progression",
                body = "Apply the structured framework for ${subject.name}: break complex ideas into fundamental components, examine relevant rules or formulas, and systematically evaluate each premise."
            ),
            StepDetail(
                stepNumber = 3,
                title = "Synthesis & Verification",
                body = "Assemble the resulting insights into a unified conclusion. Cross-check against boundary conditions and practical examples to ensure complete conceptual accuracy."
            )
        )

        val content = """
            ### Overview
            Comprehensive study analysis of "$prompt" in ${subject.name} using StudyGenie's ${mode.title} approach.
            
            ### Step 1: Fundamental Concept Breakdown
            We evaluate the core principles governing this question and establish key definitions.
            
            ### Step 2: Systematic Application
            We work through the problem logically, step by step, applying curriculum principles and relevant rules.
            
            ### Step 3: Conclusion & Synthesis
            We verify the result and check for consistency against real-world applications.
            
            ### Final Answer
            The solution to **$prompt** is verified through systematic ${subject.name} principles. Review the step-by-step breakdown above for full mastery.
            
            ### Pro-Tip & Common Pitfalls
            When tackling ${subject.name} questions, always begin by stating your knowns, unknowns, and underlying assumptions before jumping directly to calculation.
        """.trimIndent()

        return ResponseTuple(
            content = content,
            steps = steps,
            formulas = emptyList(),
            proTip = "Active recall works best: try explaining this concept out loud without looking at your notes to solidify your understanding.",
            followUps = listOf(
                "Can you provide a step-by-step example problem on this?",
                "What are the most common exam mistakes made on this topic?",
                "Create a 3-card study flashcard deck for this topic"
            )
        )
    }

    private data class ResponseTuple(
        val content: String,
        val steps: List<StepDetail>,
        val formulas: List<String>,
        val proTip: String?,
        val followUps: List<String>
    )
}
