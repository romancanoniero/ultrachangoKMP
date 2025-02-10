package com.iyr.ultrachango.utils.ui.camera_gallery

import androidx.compose.ui.graphics.ImageBitmap

expect class SharedImage {
    fun toByteArray(): ByteArray?
    fun toImageBitmap(): ImageBitmap?
    fun getUri(): String?
}