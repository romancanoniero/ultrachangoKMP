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
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryRecord
import platform.AVFAudio.AVAudioSessionInterruptionNotification
import platform.AVFAudio.AVAudioSessionModeMeasurement
import platform.AVFAudio.setActive
import platform.AVFoundation.AVAudioSession
import platform.Foundation.NSBundle
import platform.Foundation.NSError
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSSelectorFromString
import platform.Speech.SFSpeechAudioBufferRecognitionRequest
import platform.Speech.SFSpeechRecognizer
import platform.Speech.SFSpeechRecognitionTask
import platform.Speech.SFSpeechRecognitionTaskDelegateProtocol
import platform.darwin.NSObject

@ExperimentalForeignApi
class VoiceAssistant : NSObject(), SFSpeechRecognitionTaskDelegateProtocol {

    private val speechRecognizer = SFSpeechRecognizer()
    private val audioEngine = AVAudioSession.sharedInstance()
    private val recognitionRequest = SFSpeechAudioBufferRecognitionRequest()
    private var recognitionTask: SFSpeechRecognitionTask? = null


    init {

        NSNotificationCenter.defaultCenter.addObserver(
            this,
            NSSelectorFromString("handleInterruption:"),
            AVAudioSessionInterruptionNotification,
            null
        )
    }

    fun startListening() {
        try {
            audioEngine.setCategory(AVAudioSessionCategoryRecord)
            audioEngine.setMode(AVAudioSessionModeMeasurement)
            audioEngine.setActive(true, null)

            recognitionTask = speechRecognizer.recognitionTaskWithRequest(recognitionRequest, this)
        } catch (e: NSError) {
            handleError(e)
        }
    }

    fun stopListening() {

        audioEngine.stop()
        recognitionRequest.endAudio()
        recognitionTask?.cancel()
    }

    override fun speechRecognitionDidDetectSpeech(task: SFSpeechRecognitionTask) {
        // Handle speech detected
    }

    override fun speechRecognitionTask(task: SFSpeechRecognitionTask, didFinishSuccessfully: Boolean) {
        if (didFinishSuccessfully) {
            val bestTranscription = task.bestTranscription.formattedString
            handleVoiceCommand(bestTranscription)
        } else {
            handleError(null)
        }
    }

    private fun handleVoiceCommand(command: String) {
        // Handle the voice command to add items to the shopping list
        // For example, parse the command and add the item to the database
    }

    private fun handleError(error: NSError?) {
        // Handle any errors that occur during the voice command processing
    }

    @ObjCAction
    private fun handleInterruption(notification: NSNotification) {
        stopListening()
    }
}
*/