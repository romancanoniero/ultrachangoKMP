package com.iyr.ultrachango.utils.sound

import kotlinx.cinterop.ExperimentalForeignApi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.NSURL
import ultrachango2.composeapp.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
actual open class AudioPlayer {
    private val mediaItems = soundResList.map { path ->
        val uri = Res.getUri(path)
        NSURL.URLWithString(URLString = uri)
    }

    @OptIn(ExperimentalForeignApi::class)
    actual open fun playSound(id: Int) {
        val avAudioPlayer = AVAudioPlayer(mediaItems[id]!!, error = null)
        avAudioPlayer.prepareToPlay()
        avAudioPlayer.play()
    }

    actual open fun release() {}

    /** Plays a sound by id (see soundResList) */



    actual companion object {
        var instance : AudioPlayer? = null

        actual fun getInstance(): AudioPlayer {
           if (this.instance == null)
               instance = AudioPlayer()

            return AudioPlayer()
        }
    }
}