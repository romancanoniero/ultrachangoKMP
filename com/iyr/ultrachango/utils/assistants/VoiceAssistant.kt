package com.iyr.ultrachango.utils.assistants

import android.content.Context
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import platform.Foundation.NSUserActivity
import platform.Speech.SFSpeechRecognizer
import platform.Speech.SFSpeechRecognizerDelegate

interface VoiceAssistant {
    fun startListening()
    fun stopListening()
    val recognizedText: StateFlow<String>
}

class AndroidVoiceAssistant(private val context: Context) : VoiceAssistant {
    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private val _recognizedText = MutableStateFlow("")
    override val recognizedText: StateFlow<String> = _recognizedText

    init {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Toast.makeText(context, "Listening...", Toast.LENGTH_SHORT).show()
            }

            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    _recognizedText.value = matches[0]
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    override fun startListening() {
        val intent = RecognizerIntent().apply {
            action = RecognizerIntent.ACTION_RECOGNIZE_SPEECH
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now")
        }
        speechRecognizer.startListening(intent)
    }

    override fun stopListening() {
        speechRecognizer.stopListening()
    }
}

class IOSVoiceAssistant : VoiceAssistant, SFSpeechRecognizerDelegate {
    private val speechRecognizer: SFSpeechRecognizer = SFSpeechRecognizer()
    private val _recognizedText = MutableStateFlow("")
    override val recognizedText: StateFlow<String> = _recognizedText

    init {
        speechRecognizer.delegate = this
    }

    override fun startListening() {
        // Implement iOS-specific speech recognition logic
    }

    override fun stopListening() {
        // Implement iOS-specific speech recognition logic
    }

    override fun speechRecognizer(speechRecognizer: SFSpeechRecognizer, availabilityDidChange: Boolean) {
        if (!availabilityDidChange) {
            // Handle speech recognition availability change
        }
    }
}

fun createVoiceAssistant(context: Any): VoiceAssistant {
    return when (context) {
        is Context -> AndroidVoiceAssistant(context)
        else -> IOSVoiceAssistant()
    }
}
