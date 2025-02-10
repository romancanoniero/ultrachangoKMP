package com.iyr.ultrachango.utils.ui

import AppContext
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Implementación de feedback háptico en Android.
 * Utiliza Vibrator para generar una vibración corta al pulsar el botón.
 */

@RequiresApi(Build.VERSION_CODES.O)
actual fun triggerHapticFeedback() {

    val context = AppContext.context
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vm.defaultVibrator
    } else {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
}

/**
 * Implementación de sonido de clic en Android.
 * Usa MediaPlayer para reproducir un sonido del sistema.
 */
@Composable
actual fun PlayClickSound() {
    val context = LocalContext.current
    val mediaPlayer = MediaPlayer.create(context, android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
    mediaPlayer.start()
}