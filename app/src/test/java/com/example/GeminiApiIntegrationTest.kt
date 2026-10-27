package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.api.GeminiApiService
import com.example.data.api.GeminiCandidate
import com.example.data.api.GeminiContent
import com.example.data.api.GeminiGenerateRequest
import com.example.data.api.GeminiGenerateResponse
import com.example.data.api.GeminiPart
import com.example.data.db.ChatDao
import com.example.data.db.ConversationDao
import com.example.data.db.MemoryDao
import com.example.data.db.NexusDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.NexusMemory
import com.example.data.repository.NexusRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class GeminiApiIntegrationTest {

    private lateinit var context: Context
    private lateinit var db: NexusDatabase
    private lateinit var chatDao: ChatDao
    private lateinit var convDao: ConversationDao
    private lateinit var memoryDao: MemoryDao

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = NexusDatabase.getInstance(context)
        chatDao = db.chatDao()
        convDao = db.conversationDao()
        memoryDao = db.memoryDao()
    }

    @Test
    fun testApiKeyConfigurationFlow() {
        val repo = NexusRepository(context, chatDao, convDao, memoryDao)

        // Set custom API key
        repo.setCustomApiKey("AIzaSyMockKeyForTesting12345")
        assertTrue(repo.isApiKeyConfigured())
        assertEquals("AIzaSyMockKeyForTesting12345", repo.getCustomApiKey())

        // Clear custom API key
        repo.setCustomApiKey("")
        assertEquals("", repo.getCustomApiKey())
    }

    @Test
    fun testAskGeminiFailsHonestlyWhenNoKeyConfigured() = runBlocking {
        val repo = NexusRepository(context, chatDao, convDao, memoryDao)
        repo.setCustomApiKey("")

        // If no managed key is present in environment, askGemini must fail honestly without faking success
        if (!repo.isApiKeyConfigured()) {
            val convId = repo.createConversation("Test Session")
            val result = repo.askGemini(convId, "Hello")

            assertTrue(result.isFailure)
            val errorMsg = result.exceptionOrNull()?.message.orEmpty()
            assertTrue(
                "Error message should mention API key configuration",
                errorMsg.contains("API key", ignoreCase = true)
            )
        }
    }

    @Test
    fun testRealGeminiServiceWithMockApiImplementation() = runBlocking {
        // Create mock GeminiApiService returning valid candidate
        val mockService = object : GeminiApiService {
            override suspend fun generateContent(
                model: String,
                apiKey: String,
                request: GeminiGenerateRequest
            ): GeminiGenerateResponse {
                assertEquals("AIzaSyMockKey789", apiKey)
                return GeminiGenerateResponse(
                    candidates = listOf(
                        GeminiCandidate(
                            content = GeminiContent(
                                role = "model",
                                parts = listOf(GeminiPart(text = "NEXUS AI online and responding from Gemini."))
                            ),
                            finishReason = "STOP"
                        )
                    )
                )
            }
        }

        val repo = NexusRepository(
            context = context,
            chatDao = chatDao,
            conversationDao = convDao,
            memoryDao = memoryDao,
            geminiService = mockService
        )
        repo.setCustomApiKey("AIzaSyMockKey789")

        val convId = repo.createConversation("Live Session")
        val result = repo.askGemini(convId, "What is your status?")

        assertTrue(result.isSuccess)
        val responseText = result.getOrNull().orEmpty()
        assertTrue(responseText.contains("NEXUS AI online"))
    }

    @Test
    fun testDatabaseConversationAndMemoryPersistence() = runBlocking {
        val repo = NexusRepository(context, chatDao, convDao, memoryDao)
        val convId = repo.createConversation("Persistence Test")
        assertTrue(convId > 0L)

        val msgId = repo.insertMessage(
            ChatMessage(
                conversationId = convId,
                sender = "user",
                text = "Remember this fact"
            )
        )
        assertTrue(msgId > 0L)

        repo.insertMemory(
            NexusMemory(
                key = "Coding Language",
                fact = "Prefers Kotlin with Jetpack Compose",
                category = "Preference",
                isActive = true
            )
        )

        val active = repo.getActiveMemories()
        assertTrue(active.any { it.key == "Coding Language" })
    }
}
