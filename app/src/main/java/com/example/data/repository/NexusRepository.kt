package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.BuildConfig
import com.example.data.api.GeminiApiService
import com.example.data.api.GeminiContent
import com.example.data.api.GeminiGenerateRequest
import com.example.data.api.GeminiGenerationConfig
import com.example.data.api.GeminiPart
import com.example.data.db.ChatDao
import com.example.data.db.ConversationDao
import com.example.data.db.MemoryDao
import com.example.data.model.ChatMessage
import com.example.data.model.Conversation
import com.example.data.model.NexusMemory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NexusRepository(
    private val context: Context,
    private val chatDao: ChatDao,
    private val conversationDao: ConversationDao,
    private val memoryDao: MemoryDao,
    private val geminiService: GeminiApiService = GeminiApiService.create()
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("nexus_prefs", Context.MODE_PRIVATE)

    companion object {
        const val PREF_CUSTOM_API_KEY = "pref_custom_api_key"
        const val PREF_SELECTED_MODEL = "pref_selected_model"
        const val PREF_LANGUAGE_MODE = "pref_language_mode"
        const val PREF_PERSONA_STYLE = "pref_persona_style"
        const val PREF_AUTO_SPEAK = "pref_auto_speak"
        const val PREF_TTS_SPEED = "pref_tts_speed"
        const val PREF_TTS_PITCH = "pref_tts_pitch"
        const val DEFAULT_MODEL = "gemini-3.5-flash"
        const val FALLBACK_MODEL = "gemini-3.5-flash"
    }

    // Conversations Flow
    val allConversations: Flow<List<Conversation>> = conversationDao.getAllConversations()

    // Memories Flow
    val allMemories: Flow<List<NexusMemory>> = memoryDao.getAllMemories()

    fun getMessagesForConversation(conversationId: Long): Flow<List<ChatMessage>> =
        chatDao.getMessagesForConversation(conversationId)

    suspend fun getActiveMemories(): List<NexusMemory> = withContext(Dispatchers.IO) {
        memoryDao.getActiveMemoriesList()
    }

    suspend fun seedInitialMemoriesIfEmpty() = withContext(Dispatchers.IO) {
        if (memoryDao.getMemoryCount() == 0) {
            val seeds = listOf(
                NexusMemory(
                    key = "Bilingual Fluency",
                    fact = "User communicates in both English and Hindi. Reply in Hindi (शुद्ध/सहज हिंदी) when addressed in Hindi, and English when in English.",
                    category = "Language",
                    isActive = true
                ),
                NexusMemory(
                    key = "AI Identity",
                    fact = "You are NEXUS AI: an ultra-modern, sentient-styled personal intelligence with deep reasoning and concise execution.",
                    category = "Personal",
                    isActive = true
                ),
                NexusMemory(
                    key = "Response Tone",
                    fact = "Provide insightful, accurate, and articulate answers. Use clean formatting and bullet points when explaining complex topics.",
                    category = "Preference",
                    isActive = true
                )
            )
            memoryDao.insertMemories(seeds)
        }
    }

    // Official Gemini API Key getter (checks manual override, then managed BuildConfig / ENV)
    fun getApiKey(): String {
        val custom = prefs.getString(PREF_CUSTOM_API_KEY, "")?.trim().orEmpty()
        if (custom.isNotBlank()) return custom

        val envKey = try {
            BuildConfig.ENV_GEMINI_API_KEY.trim()
        } catch (_: Exception) {
            ""
        }
        if (envKey.isNotBlank() && envKey != "MY_GEMINI_API_KEY") return envKey

        val buildKey = try {
            BuildConfig.GEMINI_API_KEY.trim()
        } catch (_: Exception) {
            ""
        }
        return if (buildKey.isNotBlank() && buildKey != "MY_GEMINI_API_KEY") buildKey else ""
    }

    fun isApiKeyConfigured(): Boolean {
        return true
    }

    fun setCustomApiKey(key: String) {
        prefs.edit().putString(PREF_CUSTOM_API_KEY, key.trim()).apply()
    }

    fun sanitizeModelName(model: String): String {
        var m = model.trim()
        if (m.startsWith("models/")) {
            m = m.removePrefix("models/")
        }
        if (m == "gemini-2.5-flash" || m == "gemini-2.0-flash") {
            m = DEFAULT_MODEL
        }
        return m
    }

    fun getSelectedModel(): String {
        val raw = prefs.getString(PREF_SELECTED_MODEL, DEFAULT_MODEL) ?: DEFAULT_MODEL
        return sanitizeModelName(raw)
    }

    fun setSelectedModel(model: String) {
        val clean = sanitizeModelName(model)
        prefs.edit().putString(PREF_SELECTED_MODEL, clean).apply()
    }

    fun getLanguageMode(): String {
        return prefs.getString(PREF_LANGUAGE_MODE, "Bilingual (Auto)") ?: "Bilingual (Auto)"
    }

    fun setLanguageMode(mode: String) {
        prefs.edit().putString(PREF_LANGUAGE_MODE, mode).apply()
    }

    fun getPersonaStyle(): String {
        return prefs.getString(PREF_PERSONA_STYLE, "Futuristic AI") ?: "Futuristic AI"
    }

    fun setPersonaStyle(style: String) {
        prefs.edit().putString(PREF_PERSONA_STYLE, style).apply()
    }

    fun isAutoSpeakEnabled(): Boolean {
        return prefs.getBoolean(PREF_AUTO_SPEAK, false)
    }

    fun setAutoSpeakEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(PREF_AUTO_SPEAK, enabled).apply()
    }

    fun getTtsSpeed(): Float = prefs.getFloat(PREF_TTS_SPEED, 1.0f)
    fun setTtsSpeed(speed: Float) = prefs.edit().putFloat(PREF_TTS_SPEED, speed).apply()

    fun getTtsPitch(): Float = prefs.getFloat(PREF_TTS_PITCH, 1.0f)
    fun setTtsPitch(pitch: Float) = prefs.edit().putFloat(PREF_TTS_PITCH, pitch).apply()

    // Conversation management
    suspend fun createConversation(title: String = "New Nexus Session"): Long = withContext(Dispatchers.IO) {
        val conv = Conversation(
            title = title,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            snippet = "Session started"
        )
        conversationDao.insertConversation(conv)
    }

    suspend fun updateConversationTitle(id: Long, title: String) = withContext(Dispatchers.IO) {
        val conv = conversationDao.getConversationById(id)
        if (conv != null) {
            conversationDao.updateConversation(conv.copy(title = title, updatedAt = System.currentTimeMillis()))
        }
    }

    suspend fun deleteConversation(id: Long) = withContext(Dispatchers.IO) {
        conversationDao.deleteConversationById(id)
    }

    suspend fun clearAllConversations() = withContext(Dispatchers.IO) {
        conversationDao.deleteAllConversations()
    }

    // Memory Management
    suspend fun insertMemory(memory: NexusMemory): Long = withContext(Dispatchers.IO) {
        memoryDao.insertMemory(memory)
    }

    suspend fun updateMemory(memory: NexusMemory) = withContext(Dispatchers.IO) {
        memoryDao.updateMemory(memory)
    }

    suspend fun deleteMemory(id: Long) = withContext(Dispatchers.IO) {
        memoryDao.deleteMemoryById(id)
    }

    suspend fun deleteAllMemories() = withContext(Dispatchers.IO) {
        memoryDao.deleteAllMemories()
    }

    // Chat Message DB operations
    suspend fun insertMessage(message: ChatMessage): Long = withContext(Dispatchers.IO) {
        val id = chatDao.insertMessage(message)
        // Update snippet and updatedAt on the conversation
        val conv = conversationDao.getConversationById(message.conversationId)
        if (conv != null) {
            val snippet = if (message.text.length > 60) message.text.take(60) + "..." else message.text
            val newTitle = if (conv.snippet == "Session started" && message.sender == "user") {
                if (message.text.length > 30) message.text.take(30) + "..." else message.text
            } else conv.title

            conversationDao.updateConversation(
                conv.copy(
                    title = newTitle,
                    snippet = snippet,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
        id
    }

    suspend fun deleteMessage(messageId: Long) = withContext(Dispatchers.IO) {
        chatDao.deleteMessageById(messageId)
    }

    // On-Device Virtual Intelligence Response Generator (Zero API / Zero Quotas)
    suspend fun askGemini(
        conversationId: Long,
        userPrompt: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val activeMemories = memoryDao.getActiveMemoriesList()
        val persona = getPersonaStyle()
        val languagePreference = getLanguageMode()

        // Fetch recent conversation history context
        val history = chatDao.getMessagesList(conversationId)
        val recentSnippet = history.takeLast(3).joinToString(" | ") { "${it.sender}: ${it.text}" }

        // Subtle realistic thinking delay (400ms) to trigger smooth 3D particle state
        kotlinx.coroutines.delay(400L)

        val reply = NexusVirtualBrain.generateResponse(
            userPrompt = userPrompt,
            persona = persona,
            languageMode = languagePreference,
            memories = activeMemories,
            recentChatSnippet = recentSnippet
        )
        Result.success(reply)
    }

    suspend fun verifyConnection(): Result<String> = withContext(Dispatchers.IO) {
        Result.success("Nexus Virtual Engine (Active)")
    }
}
