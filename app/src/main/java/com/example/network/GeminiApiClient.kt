package com.example.network

import android.util.Log
import com.example.BuildConfig
import com.example.model.ChatMessage
import com.example.model.MessageSender
import com.example.model.StepDetail
import com.example.model.StudyMode
import com.example.model.SubjectCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiApiClient {
    private const val TAG = "GeminiApiClient"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"
    private const val MODEL = "gemini-3.5-flash"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    fun isApiKeyConfigured(): Boolean {
        val key = BuildConfig.GEMINI_API_KEY
        return !key.isNullOrBlank() && key != "MY_GEMINI_API_KEY"
    }

    suspend fun generateStudySolution(
        prompt: String,
        subject: SubjectCategory,
        mode: StudyMode,
        conversationHistory: List<ChatMessage> = emptyList()
    ): Result<ChatMessage> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        if (!isApiKeyConfigured()) {
            // Intelligent local tutor fallback engine with rich step-by-step breakdown
            return@withContext Result.success(
                OfflineTutorEngine.generateFallbackResponse(prompt, subject, mode)
            )
        }

        try {
            val systemInstructionText = """
                You are StudyGenie, an elite, friendly, and pedagogical AI study helper and academic tutor.
                Your mission is to help students truly understand concepts across every school subject (Math, Science, History, Literature, Computer Science, Economics, Languages).
                
                Current Subject Context: ${subject.name}
                Study Mode: ${mode.title} - ${mode.systemPromptDirective}
                
                FORMAT YOUR RESPONSE CLEARLY USING THIS EXACT STRUCTURE:
                
                ### Overview
                [1-2 sentences summarizing the core idea or what we need to solve]
                
                ### Step 1: [Step Title]
                [Detailed explanation of what is done and why]
                Formula: [Any mathematical formula, equation, or grammar rule used, or omit if none]
                
                ### Step 2: [Step Title]
                [Detailed explanation and progression]
                
                ### Step 3: [Step Title]
                [Continue steps as needed until complete]
                
                ### Final Answer
                [Clear, definitive solution or conclusion]
                
                ### Pro-Tip & Common Pitfalls
                [Crucial insight, exam tip, memory trick, or common mistake to avoid]
                
                ### Follow-up Questions
                - [Follow-up question 1]
                - [Follow-up question 2]
                - [Follow-up question 3]
            """.trimIndent()

            val contentsArray = JSONArray()

            // Include last 4 messages of history for conversation context
            val recentHistory = conversationHistory.takeLast(4)
            for (msg in recentHistory) {
                val role = if (msg.sender == MessageSender.USER) "user" else "model"
                val partObj = JSONObject().put("text", msg.content)
                val partsArray = JSONArray().put(partObj)
                contentsArray.put(JSONObject().put("role", role).put("parts", partsArray))
            }

            // Current user prompt
            val currentPart = JSONObject().put("text", prompt)
            val currentParts = JSONArray().put(currentPart)
            contentsArray.put(JSONObject().put("role", "user").put("parts", currentParts))

            val requestJson = JSONObject().apply {
                put("contents", contentsArray)
                put(
                    "systemInstruction",
                    JSONObject().put(
                        "parts",
                        JSONArray().put(JSONObject().put("text", systemInstructionText))
                    )
                )
                put(
                    "generationConfig",
                    JSONObject().apply {
                        put("temperature", 0.3)
                        put("topP", 0.95)
                    }
                )
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = requestJson.toString().toRequestBody(mediaType)

            val url = "$BASE_URL/$MODEL:generateContent?key=$apiKey"
            val httpRequest = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(httpRequest).execute()
            val responseBodyString = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                Log.e(TAG, "Gemini API error code: ${response.code}, body: $responseBodyString")
                // Return gracefully with fallback + helpful note
                return@withContext Result.success(
                    OfflineTutorEngine.generateFallbackResponse(
                        prompt,
                        subject,
                        mode,
                        apiNotice = "Note: Cloud Gemini API returned HTTP ${response.code}. Displaying StudyGenie's built-in step-by-step curriculum engine."
                    )
                )
            }

            val jsonResponse = JSONObject(responseBodyString)
            val candidates = jsonResponse.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val contentObj = firstCandidate?.optJSONObject("content")
            val partsArr = contentObj?.optJSONArray("parts")
            val text = partsArr?.optJSONObject(0)?.optString("text")

            if (text.isNullOrBlank()) {
                return@withContext Result.success(
                    OfflineTutorEngine.generateFallbackResponse(prompt, subject, mode)
                )
            }

            val parsedMessage = parseGeminiResponse(text, subject)
            Result.success(parsedMessage)
        } catch (e: Exception) {
            Log.e(TAG, "Exception calling Gemini API: ${e.message}", e)
            Result.success(
                OfflineTutorEngine.generateFallbackResponse(
                    prompt,
                    subject,
                    mode,
                    apiNotice = "Offline mode active: Displaying StudyGenie's instant built-in solution."
                )
            )
        }
    }

    private fun parseGeminiResponse(rawText: String, subject: SubjectCategory): ChatMessage {
        val steps = mutableListOf<StepDetail>()
        val formulas = mutableListOf<String>()
        var proTip: String? = null
        val followUps = mutableListOf<String>()

        val stepRegex = Regex("###\\s*Step\\s*(\\d+):?\\s*(.*?)(?=(###|$))", RegexOption.DOT_MATCHES_ALL)
        val stepMatches = stepRegex.findAll(rawText)

        for (match in stepMatches) {
            val stepNumber = match.groupValues[1].toIntOrNull() ?: (steps.size + 1)
            val rawBody = match.groupValues[2].trim()
            val lines = rawBody.lines()
            val title = lines.firstOrNull()?.trim() ?: "Step $stepNumber"
            val rest = lines.drop(1).joinToString("\n").trim()

            // Look for Formula: ... in step
            var formulaInStep: String? = null
            val cleanedBody = rest.lines().filter { line ->
                if (line.trim().startsWith("Formula:", ignoreCase = true)) {
                    formulaInStep = line.replace(Regex("(?i)Formula:"), "").trim()
                    false
                } else {
                    true
                }
            }.joinToString("\n").trim()

            steps.add(
                StepDetail(
                    stepNumber = stepNumber,
                    title = title,
                    body = cleanedBody.ifBlank { rawBody },
                    formulaOrKeyPoint = formulaInStep
                )
            )
        }

        // Extract Pro-Tip
        val proTipRegex = Regex("###\\s*(Pro-Tip|Common Pitfalls)[^\\n]*\\n(.*?)(?=(###|$))", RegexOption.DOT_MATCHES_ALL)
        proTipRegex.find(rawText)?.let {
            proTip = it.groupValues[2].trim()
        }

        // Extract Follow-up Questions
        val followUpRegex = Regex("###\\s*Follow-up Questions[^\\n]*\\n(.*?)(?=(###|$))", RegexOption.DOT_MATCHES_ALL)
        followUpRegex.find(rawText)?.let {
            val lines = it.groupValues[1].trim().lines()
            for (line in lines) {
                val clean = line.replace(Regex("^[\\s*\\-•0-9.)]+"), "").trim()
                if (clean.isNotBlank()) {
                    followUps.add(clean)
                }
            }
        }

        return ChatMessage(
            sender = MessageSender.GENIE,
            content = rawText,
            subject = subject,
            steps = steps,
            formulas = formulas,
            proTip = proTip,
            suggestedFollowUps = followUps.take(3)
        )
    }
}
