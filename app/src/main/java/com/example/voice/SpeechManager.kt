package com.example.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * Robust Android Speech Recognition Manager.
 * Operates on Main Looper for device compatibility, handles English & Hindi speech models,
 * audio level metering, partial transcription, and clear error diagnostics.
 */
class SpeechManager(private val context: Context) {

    sealed class SpeechState {
        object Idle : SpeechState()
        object Listening : SpeechState()
        data class PartialResult(val text: String) : SpeechState()
        data class FinalResult(val text: String) : SpeechState()
        data class Error(val message: String) : SpeechState()
    }

    private val mainHandler = Handler(Looper.getMainLooper())

    private val _speechState = MutableStateFlow<SpeechState>(SpeechState.Idle)
    val speechState: StateFlow<SpeechState> = _speechState.asStateFlow()

    private val _soundLevel = MutableStateFlow(0f)
    val soundLevel: StateFlow<Float> = _soundLevel.asStateFlow()

    private var speechRecognizer: SpeechRecognizer? = null

    val isRecognitionAvailable: Boolean
        get() = SpeechRecognizer.isRecognitionAvailable(context)

    fun startListening(languagePreference: String = "Bilingual (Auto)") {
        if (!isRecognitionAvailable) {
            _speechState.value = SpeechState.Error("Speech recognition is not available on this device. Install or enable Google Speech Services.")
            return
        }

        mainHandler.post {
            stopListeningInternal()

            try {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                    setRecognitionListener(object : RecognitionListener {
                        override fun onReadyForSpeech(params: Bundle?) {
                            _speechState.value = SpeechState.Listening
                        }

                        override fun onBeginningOfSpeech() {
                            _speechState.value = SpeechState.Listening
                        }

                        override fun onRmsChanged(rmsdB: Float) {
                            // Normalize audio RMS from approx -2dB..10dB to 0f..1f
                            val normalized = ((rmsdB + 2f) / 12f).coerceIn(0f, 1f)
                            _soundLevel.value = normalized
                        }

                        override fun onBufferReceived(buffer: ByteArray?) {}

                        override fun onEndOfSpeech() {
                            _soundLevel.value = 0f
                        }

                        override fun onError(error: Int) {
                            val message = when (error) {
                                SpeechRecognizer.ERROR_AUDIO -> "Audio recording error. Check microphone."
                                SpeechRecognizer.ERROR_CLIENT -> "Speech recognizer client error."
                                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission is required."
                                SpeechRecognizer.ERROR_NETWORK -> "Network error during speech recognition."
                                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Speech recognition network timed out."
                                SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized. Please speak closer to the mic."
                                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Speech recognizer is busy. Please try again."
                                SpeechRecognizer.ERROR_SERVER -> "Speech server error occurred."
                                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No sound detected. Microphone timed out."
                                else -> "Recognition error (Code #$error)"
                            }
                            _soundLevel.value = 0f
                            _speechState.value = SpeechState.Error(message)
                        }

                        override fun onResults(results: Bundle?) {
                            _soundLevel.value = 0f
                            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            val text = matches?.firstOrNull()?.trim().orEmpty()
                            if (text.isNotBlank()) {
                                _speechState.value = SpeechState.FinalResult(text)
                            } else {
                                _speechState.value = SpeechState.Error("No sound recognized. Please try again.")
                            }
                        }

                        override fun onPartialResults(partialResults: Bundle?) {
                            val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            val text = matches?.firstOrNull()?.trim().orEmpty()
                            if (text.isNotBlank()) {
                                _speechState.value = SpeechState.PartialResult(text)
                            }
                        }

                        override fun onEvent(eventType: Int, params: Bundle?) {}
                    })
                }

                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
                    putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)

                    val langTag = when {
                        languagePreference.contains("Hindi", ignoreCase = true) || languagePreference.contains("हिंदी") -> "hi-IN"
                        languagePreference.contains("English", ignoreCase = true) -> "en-US"
                        else -> Locale.getDefault().toLanguageTag()
                    }
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, langTag)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, langTag)
                    putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1500L)
                    putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 1500L)
                    putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 1000L)
                }

                speechRecognizer?.startListening(intent)
                _speechState.value = SpeechState.Listening
            } catch (e: Exception) {
                _speechState.value = SpeechState.Error("Failed to initialize voice recognition: ${e.message}")
            }
        }
    }

    fun stopListening() {
        mainHandler.post {
            stopListeningInternal()
            if (_speechState.value is SpeechState.Listening) {
                _speechState.value = SpeechState.Idle
            }
        }
    }

    private fun stopListeningInternal() {
        try {
            speechRecognizer?.stopListening()
            speechRecognizer?.cancel()
            speechRecognizer?.destroy()
        } catch (_: Exception) {}
        speechRecognizer = null
        _soundLevel.value = 0f
    }

    fun resetState() {
        _speechState.value = SpeechState.Idle
        _soundLevel.value = 0f
    }
}
