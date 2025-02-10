package com.iyr.ultrachango.utils.sound

import android.content.Context
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.iyr.ultrachango.UltraChangoApp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ultrachango2.composeapp.generated.resources.Res


@ExperimentalResourceApi
actual class AudioPlayer(private val context: Context) {

    private val mediaPlayer = ExoPlayer.Builder(context).build()
    private val mediaItems = soundResList.map {
        MediaItem.fromUri(Res.getUri(it))

    }

    init {
        mediaPlayer.prepare()
    }

    @OptIn(ExperimentalResourceApi::class)
    actual open fun playSound(id: Int) {
        mediaPlayer.setMediaItem(mediaItems[id])
        mediaPlayer.play()
    }

    actual open fun release() {
        mediaPlayer.release()
    }

    /** Plays a sound by id (see soundResList) */

    actual companion object {

        @Volatile
        private var instance: AudioPlayer? = null

        actual fun getInstance(): AudioPlayer {
            return instance ?: synchronized(this) {
                instance ?: AudioPlayer(UltraChangoApp.contextProvider).also { instance = it }
            }
        }
    }
}