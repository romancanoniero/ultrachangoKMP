package com.iyr.ultrachango.utils.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.SystemTheme
import platform.AudioToolbox.AudioServicesPlaySystemSound
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle

/**
 * Declaración `expect` para definir una función de feedback háptico en Android e iOS.
 */
actual fun triggerHapticFeedback() {
    val feedbackGenerator = UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleLight)
    feedbackGenerator.prepare()
    feedbackGenerator.impactOccurred()
}

/**
 * Declaración `expect` para reproducir un sonido de clic en Android e iOS.
 */
@Composable
actual fun PlayClickSound() {
    val systemSoundID: UInt = 1104u // Sonido de clic del teclado en iOS
    AudioServicesPlaySystemSound(systemSoundID)
}