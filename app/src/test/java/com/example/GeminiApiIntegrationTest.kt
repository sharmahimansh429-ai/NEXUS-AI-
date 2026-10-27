package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.db.ChatDao
import com.example.data.db.ConversationDao
import com.example.data.db.MemoryDao
import com.example.data.db.NexusDatabase
import com.example.data.repository.NexusRepository
import kotlinx.coroutines.runBlocking
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
    fun testVirtualEngineAlwaysConfigured() {
        val repo = NexusRepository(context, chatDao, convDao, memoryDao)
        assertTrue(repo.isApiKeyConfigured())
    }

    @Test
    fun testVirtualBrainConversationAndMemory() = runBlocking {
        val repo = NexusRepository(
            context = context,
            chatDao = chatDao,
            conversationDao = convDao,
            memoryDao = memoryDao
        )

        val convId = repo.createConversation("Test Session")
        val result = repo.askGemini(convId, "Hello Nexus")

        assertTrue(result.isSuccess)
        val response = result.getOrNull()
        assertTrue(!response.isNullOrBlank())
    }

    @Test
    fun testVirtualBrainHindiSupport() = runBlocking {
        val repo = NexusRepository(
            context = context,
            chatDao = chatDao,
            conversationDao = convDao,
            memoryDao = memoryDao
        )

        val convId = repo.createConversation("Hindi Session")
        val result = repo.askGemini(convId, "नमस्ते, आप कैसे हैं?")

        assertTrue(result.isSuccess)
        val response = result.getOrNull()
        assertTrue(!response.isNullOrBlank())
    }
}
