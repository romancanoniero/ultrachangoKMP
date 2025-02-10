package com.iyr.ultrachango.utils.voiceassistant

expect class VoiceAssistant {
    fun startListening()
    fun stopListening()
    fun handleVoiceCommand(command: String)
    fun handleError(error: Throwable)
}
