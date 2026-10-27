package com.example.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * Text-to-Speech manager providing natural bilingual (English & Hindi) speech synthesis
 * with dynamic rate/pitch adjustments, utterance tracking, and clean markdown preprocessing.
 */
class TextToSpeechManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isAvailable = MutableStateFlow(true)
    val isAvailable: StateFlow<Boolean> = _isAvailable.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _currentlySpeakingId = MutableStateFlow<Long?>(null)
    val currentlySpeakingId: StateFlow<Long?> = _currentlySpeakingId.asStateFlow()

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (_: Exception) {
            _isAvailable.value = false
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            _isAvailable.value = true
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentlySpeakingId.value = null
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentlySpeakingId.value = null
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    _isSpeaking.value = false
                    _currentlySpeakingId.value = null
                }
            })
        } else {
            isInitialized = false
            _isAvailable.value = false
        }
    }

    fun speak(text: String, messageId: Long? = null, speed: Float = 1.0f, pitch: Float = 1.0f) {
        if (!isInitialized || tts == null) return

        stop()

        val cleanText = cleanMarkdownForSpeech(text)
        if (cleanText.isBlank()) return

        // Detect Hindi Devanagari Unicode characters (0x0900..0x097F)
        val hasHindi = cleanText.any { it.code in 0x0900..0x097F }
        val targetLocale = if (hasHindi) Locale("hi", "IN") else Locale.US

        try {
            val availability = tts?.isLanguageAvailable(targetLocale)
            if (availability == TextToSpeech.LANG_AVAILABLE ||
                availability == TextToSpeech.LANG_COUNTRY_AVAILABLE ||
                availability == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE
            ) {
                tts?.language = targetLocale
            } else {
                // Fall back to system default or US English if Hindi language pack is absent
                tts?.language = Locale.US
            }

            tts?.setSpeechRate(speed.coerceIn(0.5f, 2.0f))
            tts?.setPitch(pitch.coerceIn(0.5f, 2.0f))

            _currentlySpeakingId.value = messageId
            val utteranceId = "nexus_utt_${messageId ?: System.currentTimeMillis()}"
            tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
            _isSpeaking.value = true
        } catch (_: Exception) {
            _isSpeaking.value = false
            _currentlySpeakingId.value = null
        }
    }

    fun stop() {
        try {
            tts?.stop()
        } catch (_: Exception) {}
        _isSpeaking.value = false
        _currentlySpeakingId.value = null
    }

    fun shutdown() {
        stop()
        try {
            tts?.shutdown()
        } catch (_: Exception) {}
        tts = null
        isInitialized = false
    }

    private fun cleanMarkdownForSpeech(raw: String): String {
        return raw
            // Remove code blocks
            .replace(Regex("```[\\s\\S]*?```"), "Code block omitted.")
            // Remove inline code
            .replace(Regex("`([^`]+)`"), "$1")
            // Remove markdown links [title](url) -> title
            .replace(Regex("\\[([^\\]]+)\\]\\([^)]+\\)"), "$1")
            // Remove headers #, ##, etc.
            .replace(Regex("#{1,6}\\s*"), "")
            // Remove bold/italics
            .replace(Regex("[*_~]{1,3}"), "")
            // Remove blockquotes
            .replace(Regex("^>\\s*", RegexOption.MULTILINE), "")
            // Replace multiple newlines with a pause period
            .replace(Regex("\n+"), ". ")
            .trim()
    }
}
