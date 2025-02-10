@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.iyr.ultrachango.utils.sound

expect class AudioPlayer  {
    /** Plays a sound by id (see soundResList) */

    companion object{
        fun getInstance(): AudioPlayer
    }

    open fun playSound(id: Int)
    open fun release()
}

val soundResList = listOf(
    "files/scanner.mp3"
)


