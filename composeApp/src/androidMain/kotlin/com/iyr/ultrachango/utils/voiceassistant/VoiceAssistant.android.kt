package com.iyr.ultrachango.utils.voiceassistant


actual class VoiceAssistant {
    actual fun startListening() {
    }

    actual fun stopListening() {
    }

    actual fun handleVoiceCommand(command: String) {
    }

    actual fun handleError(error: Throwable) {
    }
}


/*
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class VoiceAssistantActivity : ComponentActivity() {

    private val viewModel: VoiceAssistantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        }

        val speechRecognizerCallback = object : SpeechRecognizer.RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                viewModel.handleError(error)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.let {
                    viewModel.handleVoiceCommand(it[0])
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }

        speechRecognizer.setRecognitionListener(speechRecognizerCallback)
        speechRecognizer.startListening(recognizerIntent)
    }
}

class VoiceAssistantViewModel : ViewModel() {

    fun handleVoiceCommand(command: String) {
        viewModelScope.launch {
            try {
                // Handle the voice command to add items to the shopping list
                // For example, parse the command and add the item to the database
            } catch (e: Exception) {
                // Handle any errors that occur during the voice command processing
            }
        }
    }

    fun handleError(error: Int) {
        viewModelScope.launch {
            // Handle the error and provide user feedback
        }
    }
}

@Composable
fun VoiceAssistantUI() {
    val context = LocalContext.current
    val activity = context as? Activity
    val isListening = remember { mutableStateOf(false) }

    if (isListening.value) {
        Toast.makeText(context, "Listening...", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Not listening", Toast.LENGTH_SHORT).show()
    }

    activity?.let {
        val startVoiceAssistant = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                matches?.let {
                    // Handle the voice command results
                }
            }
        }

        startVoiceAssistant.launch(Intent(context, VoiceAssistantActivity::class.java))
    }
}
*/