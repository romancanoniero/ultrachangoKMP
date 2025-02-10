package com.iyr.ultrachango.utils.ui

import androidx.compose.runtime.Composable

/**
 * Declaración `expect` para definir una función de feedback háptico en Android e iOS.
 */
expect fun triggerHapticFeedback()

/**
 * Declaración `expect` para reproducir un sonido de clic en Android e iOS.
 */
@Composable
expect fun PlayClickSound()