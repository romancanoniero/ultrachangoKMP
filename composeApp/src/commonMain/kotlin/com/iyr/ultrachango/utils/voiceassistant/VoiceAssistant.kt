@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.iyr.ultrachango.utils.voiceassistant

expect class VoiceAssistant {
    fun startListening()
    fun stopListening()
    fun handleVoiceCommand(command: String)
    fun handleError(error: Throwable)
}
