package com.example.data

import com.example.model.FlashcardDeck
import com.example.model.FlashcardItem
import com.example.model.ResourceSection
import com.example.model.ResourceType
import com.example.model.StudyResource
import com.example.model.SubjectCategory

object ResourceLibraryData {

    val RESOURCES: List<StudyResource> = listOf(
        // MATHEMATICS
        StudyResource(
            id = "math_calculus_essentials",
            title = "Calculus: Limits, Derivatives & Integrals",
            subject = SubjectCategory.MATH,
            type = ResourceType.FORMULA_SHEET,
            summary = "Complete quick-reference formula sheet with power rule, product rule, chain rule, and fundamental theorem of calculus.",
            readTimeMinutes = 5,
            sections = listOf(
                ResourceSection(
                    title = "Key Derivative Rules",
                    content = "Essential differential calculus rules used across all algebra & physics applications.",
                    formulaOrHighlight = "Power Rule: d/dx [x^n] = n · x^(n-1)\nProduct Rule: (uv)' = u'v + uv'\nQuotient Rule: (u/v)' = (u'v - uv') / v²\nChain Rule: d/dx [f(g(x))] = f'(g(x)) · g'(x)",
                    example = "d/dx [sin(3x²)] = cos(3x²) · (6x) = 6x cos(3x²)"
                ),
                ResourceSection(
                    title = "Fundamental Theorem of Calculus (FTC)",
                    content = "Connects differentiation and integration as inverse mathematical operations.",
                    formulaOrHighlight = "Part 1: d/dx ∫[a to x] f(t) dt = f(x)\nPart 2: ∫[a to b] f(x) dx = F(b) - F(a) where F'(x) = f(x)",
                    example = "∫[0 to 3] (2x + 1) dx = [x² + x] from 0 to 3 = (9 + 3) - 0 = 12"
                ),
                ResourceSection(
                    title = "Trigonometric Derivatives & Integrals",
                    content = "Crucial trigonometric transformations for engineering and math analysis.",
                    formulaOrHighlight = "d/dx [sin x] = cos x\nd/dx [cos x] = -sin x\nd/dx [tan x] = sec² x\n∫ sec² x dx = tan x + C\n∫ 1/x dx = ln|x| + C",
                    example = "∫ cos(4x) dx = (1/4) sin(4x) + C"
                )
            ),
            quickTips = listOf(
                "Always check for chain rule when functions are composed together.",
                "Don't forget the integration constant (+ C) for indefinite integrals.",
                "When integrating by parts, use the LIATE rule: Logarithmic, Inverse trig, Algebraic, Trig, Exponential."
            )
        ),
        StudyResource(
            id = "math_algebra_geometry",
            title = "Algebra & Geometry Master Cheat Sheet",
            subject = SubjectCategory.MATH,
            type = ResourceType.CHEAT_SHEET,
            summary = "Quadratic formula, discriminant test, distance, conic sections, and circle theorems.",
            readTimeMinutes = 4,
            sections = listOf(
                ResourceSection(
                    title = "Quadratic Equations & Discriminant",
                    content = "Solving ax² + bx + c = 0 and determining the nature of roots.",
                    formulaOrHighlight = "Quadratic Formula: x = (-b ± √(b² - 4ac)) / (2a)\nDiscriminant Δ = b² - 4ac:\n• Δ > 0: 2 distinct real roots\n• Δ = 0: 1 real repeated root\n• Δ < 0: 2 complex conjugate roots",
                    example = "For x² - 6x + 9 = 0, Δ = 36 - 36 = 0, root is x = 6/2 = 3"
                ),
                ResourceSection(
                    title = "Coordinate Geometry Essentials",
                    content = "Formulas for points, lines, midpoints, and slope-intercept forms.",
                    formulaOrHighlight = "Slope: m = (y₂ - y₁) / (x₂ - x₁)\nDistance: d = √((x₂ - x₁)² + (y₂ - y₁)²)\nMidpoint: M = ((x₁ + x₂)/2, (y₁ + y₂)/2)\nLine Equation: y - y₁ = m(x - x₁)",
                    example = "Distance between (1, 2) and (4, 6) is √(3² + 4²) = √25 = 5"
                )
            ),
            quickTips = listOf(
                "Perpendicular lines have slopes that are negative reciprocals: m₁ · m₂ = -1",
                "Factor out common terms first before applying the quadratic formula."
            )
        ),

        // SCIENCE
        StudyResource(
            id = "science_physics_mechanics",
            title = "Physics: Classical Mechanics & Energy",
            subject = SubjectCategory.SCIENCE,
            type = ResourceType.FORMULA_SHEET,
            summary = "Kinematics equations, Newton's Laws, Work-Energy theorem, Momentum, and Gravitation.",
            readTimeMinutes = 5,
            sections = listOf(
                ResourceSection(
                    title = "Kinematics Equations (Constant Acceleration)",
                    content = "The core equations governing motion in 1D and 2D projectile paths.",
                    formulaOrHighlight = "1) v = v₀ + at\n2) Δx = v₀t + (1/2)at²\n3) v² = v₀² + 2aΔx\n4) Δx = ((v + v₀)/2) · t",
                    example = "Car accelerating at 2 m/s² from rest for 5s: v = 0 + 2(5) = 10 m/s; distance = (1/2)(2)(25) = 25m"
                ),
                ResourceSection(
                    title = "Newton's Three Laws of Motion",
                    content = "Fundamental principles of classical dynamics and force interaction.",
                    formulaOrHighlight = "1st: Inertia (object remains at rest/constant velocity unless acted upon by net force)\n2nd: F_net = m · a\n3rd: For every action, an equal and opposite reaction: F_A_on_B = -F_B_on_A",
                    example = "A 10kg mass pushed with 50N net force experiences acceleration a = 50 / 10 = 5 m/s²"
                ),
                ResourceSection(
                    title = "Work, Energy & Conservation",
                    content = "Kinetic energy, gravitational potential energy, and mechanical energy conservation.",
                    formulaOrHighlight = "Work: W = F · d · cos(θ)\nKinetic Energy: KE = (1/2)mv²\nPotential Energy: PE = mgh\nConservation of Mechanical Energy: KE_i + PE_i = KE_f + PE_f (no friction)",
                    example = "Dropped ball from height h: (1/2)mv² = mgh ⇒ v = √(2gh)"
                )
            ),
            quickTips = listOf(
                "Draw a Free Body Diagram (FBD) for every force problem before writing equations.",
                "Always verify unit consistency (SI: kg, meters, seconds, Newtons, Joules)."
            )
        ),
        StudyResource(
            id = "science_chemistry_stoichiometry",
            title = "Chemistry: Stoichiometry & Gas Laws",
            subject = SubjectCategory.SCIENCE,
            type = ResourceType.CHEAT_SHEET,
            summary = "Mole concept, balancing chemical equations, Ideal Gas Law, molarity, and limiting reactants.",
            readTimeMinutes = 4,
            sections = listOf(
                ResourceSection(
                    title = "The Mole Concept & Molar Conversions",
                    content = "Connecting mass, particles, and volume in chemistry.",
                    formulaOrHighlight = "Moles (n) = Mass (m) / Molar Mass (M)\nParticles = Moles · Avogadro's Number (6.022 × 10²³)\nMolarity (M) = Moles of solute / Liters of solution (mol/L)",
                    example = "36g of H₂O (M = 18 g/mol): n = 36 / 18 = 2.0 moles = 1.204 × 10²⁴ molecules"
                ),
                ResourceSection(
                    title = "Ideal Gas Law & Combined Gas Law",
                    content = "Relationships between pressure, volume, temperature, and moles.",
                    formulaOrHighlight = "Ideal Gas Law: PV = nRT\n(R = 0.0821 L·atm/(mol·K) or 8.314 J/(mol·K))\nCombined Gas Law: (P₁V₁)/T₁ = (P₂V₂)/T₂\nNote: Temperature MUST always be in Kelvin (K = °C + 273.15)",
                    example = "If volume doubles at constant temp, pressure is halved (Boyle's Law)."
                )
            ),
            quickTips = listOf(
                "Always convert temperatures to Kelvin in gas law calculations.",
                "To find the limiting reactant: calculate theoretical moles of product for each reactant; the lower value wins."
            )
        ),

        // HISTORY & CIVICS
        StudyResource(
            id = "history_world_war_causes",
            title = "World History: Causes of WWI & WWII",
            subject = SubjectCategory.HISTORY,
            type = ResourceType.STUDY_GUIDE,
            summary = "The M-A-I-N causes of WWI, Treaty of Versailles fallout, and events leading to WWII.",
            readTimeMinutes = 6,
            sections = listOf(
                ResourceSection(
                    title = "M-A-I-N Causes of World War I (1914–1918)",
                    content = "The core systemic pressures that ignited the First World War.",
                    formulaOrHighlight = "• M - Militarism (Arms race, Dreadnought naval expansion)\n• A - Alliances (Triple Entente vs Triple Alliance; chain reaction)\n• I - Imperialism (Scramble for Africa, Balkan competition)\n• N - Nationalism (Pan-Slavism, Balkan powder keg)\nTrigger: Assassination of Archduke Franz Ferdinand in Sarajevo (June 28, 1914)",
                    example = "Austria-Hungary's ultimatum to Serbia triggered Russian mobilization, bringing Germany and France into conflict."
                ),
                ResourceSection(
                    title = "Path to World War II (1919–1939)",
                    content = "Failure of the League of Nations and the rise of totalitarian regimes.",
                    formulaOrHighlight = "• Treaty of Versailles: War guilt clause (Article 231) & heavy reparations fueled German economic distress.\n• Rise of Fascism: Mussolini (Italy, 1922) and Hitler (Germany, 1933).\n• Appeasement: Munich Agreement (1938) failed to stop Hitler's expansion.\n• Trigger: Nazi invasion of Poland (Sept 1, 1939).",
                    example = "British Prime Minister Neville Chamberlain's 'Peace for our time' policy demonstrated the collapse of appeasement."
                )
            ),
            quickTips = listOf(
                "Use the M-A-I-N acronym in essay introductions for clear thematic structure.",
                "Contrast the League of Nations (weak, no military enforcement) with the post-WWII United Nations."
            )
        ),

        // LITERATURE & WRITING
        StudyResource(
            id = "literature_essay_mastery",
            title = "Essay Writing & Rhetorical Analysis",
            subject = SubjectCategory.LITERATURE,
            type = ResourceType.STUDY_GUIDE,
            summary = "Framework for structuring 5-paragraph argumentative essays, thesis crafting, and Aristotle's rhetorical appeals.",
            readTimeMinutes = 5,
            sections = listOf(
                ResourceSection(
                    title = "The High-Scoring Essay Blueprint",
                    content = "A clean structural guide for persuasive and analytical academic essays.",
                    formulaOrHighlight = "1) Introduction: Engaging Hook → Context/Background → Strong 3-part Thesis statement\n2) Body Paragraphs (x3): Claim (Topic Sentence) → Context → Direct Evidence/Quote → In-depth Analysis (why it matters) → Transition\n3) Conclusion: Restate thesis in new words → Synthesize arguments → Broader societal takeaway / Call to action",
                    example = "Strong Thesis Formula: 'Although [Counterargument], [Your Position] because [Reason 1] and [Reason 2].'"
                ),
                ResourceSection(
                    title = "Aristotle's Rhetorical Triangle",
                    content = "The three pillars of persuasive writing and speech analysis.",
                    formulaOrHighlight = "• Ethos (Credibility): Establishing authority, ethical character, and trustworthiness.\n• Pathos (Emotion): Engaging reader empathy, sensory details, and vivid imagery.\n• Logos (Logic): Using statistics, syllogisms, empirical evidence, and clear reasoning.",
                    example = "Martin Luther King Jr.'s 'I Have a Dream' masterfully blends Pathos (dreams for children) and Logos (constitutional promises)."
                )
            ),
            quickTips = listOf(
                "Never drop a quotation without analysis; follow the 'Quote Sandwich' rule: Intro lead-in + Quote + 2 sentences of explanation.",
                "Avoid first person ('I think', 'In my opinion') in formal argumentative essays."
            )
        ),

        // COMPUTER SCIENCE
        StudyResource(
            id = "cs_algorithms_big_o",
            title = "Computer Science: Big-O & Data Structures",
            subject = SubjectCategory.COMPUTER_SCIENCE,
            type = ResourceType.CHEAT_SHEET,
            summary = "Time & Space complexity cheat sheet, array vs linked list, tree traversals, and search algorithms.",
            readTimeMinutes = 5,
            sections = listOf(
                ResourceSection(
                    title = "Big-O Complexity Hierarchy",
                    content = "From best performance to worst as input size N grows.",
                    formulaOrHighlight = "O(1) - Constant (Hash table lookup, array index)\nO(log N) - Logarithmic (Binary search in sorted array)\nO(N) - Linear (Linear scan, single loop)\nO(N log N) - Linearithmic (MergeSort, QuickSort average)\nO(N²) - Quadratic (Nested loops, BubbleSort)\nO(2^N) - Exponential (Naive Fibonacci recursion)\nO(N!) - Factorial (Traveling Salesperson permutations)",
                    example = "Binary search on 1,000,000 elements requires at most ~20 comparisons (log₂ 10⁶ ≈ 20)."
                ),
                ResourceSection(
                    title = "Core Data Structure Trade-offs",
                    content = "Strengths and weaknesses in memory and runtime.",
                    formulaOrHighlight = "• Array: O(1) random access by index; O(N) insertion/deletion.\n• LinkedList: O(1) insertion at head; O(N) access/search.\n• HashMap: O(1) average lookup, insert, delete; handles key-value pairs.\n• Binary Search Tree: O(log N) search/insert if balanced (AVL/Red-Black).",
                    example = "Choose HashMap when you need instant lookup by unique ID; choose Array for sequential numerical indexing."
                )
            ),
            quickTips = listOf(
                "Drop constants and non-dominant terms in Big-O analysis: 3N² + 5N + 10 becomes O(N²).",
                "Space complexity counts auxiliary memory, not including input space unless specified."
            )
        )
    )

    val BUILT_IN_DECKS: List<FlashcardDeck> = listOf(
        FlashcardDeck(
            id = "deck_math_calc",
            title = "Calculus Essentials",
            subject = SubjectCategory.MATH,
            description = "High-yield derivative rules and integral properties for exams.",
            cards = listOf(
                FlashcardItem(
                    question = "What is the derivative of sin(x)?",
                    answer = "cos(x)",
                    hint = "Trigonometric derivative rule"
                ),
                FlashcardItem(
                    question = "What is the derivative of e^(2x)?",
                    answer = "2e^(2x) (due to chain rule)",
                    hint = "Remember to multiply by the derivative of the exponent"
                ),
                FlashcardItem(
                    question = "What is ∫ (1/x) dx?",
                    answer = "ln|x| + C",
                    hint = "Natural logarithm with absolute value"
                ),
                FlashcardItem(
                    question = "State the Product Rule for differentiation",
                    answer = "(u · v)' = u'v + uv'",
                    hint = "Derivative of first times second plus first times derivative of second"
                ),
                FlashcardItem(
                    question = "What is the integral of sec²(x) dx?",
                    answer = "tan(x) + C",
                    hint = "Recall which function has derivative equal to sec²(x)"
                )
            )
        ),
        FlashcardDeck(
            id = "deck_science_bio",
            title = "Biology: Cell & Genetics",
            subject = SubjectCategory.SCIENCE,
            description = "Key organelles, mitosis stages, and DNA transcription fundamentals.",
            cards = listOf(
                FlashcardItem(
                    question = "What is the primary function of Mitochondria?",
                    answer = "Cellular respiration & ATP energy production ('powerhouse of the cell')",
                    hint = "Produces energy for metabolic reactions"
                ),
                FlashcardItem(
                    question = "What are the 4 stages of Mitosis in order?",
                    answer = "Prophase, Metaphase, Anaphase, Telophase (PMAT)",
                    hint = "Remember mnemonic PMAT"
                ),
                FlashcardItem(
                    question = "Which base pairs with Adenine (A) in RNA?",
                    answer = "Uracil (U) [in DNA it is Thymine]",
                    hint = "RNA replaces T with U"
                ),
                FlashcardItem(
                    question = "What organelle is responsible for protein synthesis?",
                    answer = "Ribosome",
                    hint = "Found floating or attached to Rough ER"
                )
            )
        ),
        FlashcardDeck(
            id = "deck_history_civics",
            title = "Civics & Governance",
            subject = SubjectCategory.HISTORY,
            description = "Branches of government, constitutional principles, and checks & balances.",
            cards = listOf(
                FlashcardItem(
                    question = "What are the 3 branches of the US Government?",
                    answer = "Legislative (Congress), Executive (President), Judicial (Supreme Court)",
                    hint = "Law-making, law-enforcing, law-interpreting"
                ),
                FlashcardItem(
                    question = "How does the Executive branch check the Legislative branch?",
                    answer = "The President can veto legislation passed by Congress",
                    hint = "Presidential power over bills"
                ),
                FlashcardItem(
                    question = "What Supreme Court case established Judicial Review?",
                    answer = "Marbury v. Madison (1803)",
                    hint = "Chief Justice John Marshall"
                ),
                FlashcardItem(
                    question = "What is Federalism?",
                    answer = "A system of government where power is divided between a central national authority and regional state governments",
                    hint = "Division between national and state powers"
                )
            )
        )
    )
}
