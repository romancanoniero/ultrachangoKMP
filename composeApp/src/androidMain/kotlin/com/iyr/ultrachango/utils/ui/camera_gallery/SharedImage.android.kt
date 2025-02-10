package com.iyr.ultrachango.utils.ui.camera_gallery

import androidx.compose.ui.graphics.ImageBitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

actual class SharedImage(private val bitmap: android.graphics.Bitmap? = null
, private val uri: String? = null) {


    actual fun getUri(): String? {
        return uri
    }

    actual fun toByteArray(): ByteArray? {
        // val compressedBitmap = compressImage(bitmap ?: return null)
        return if (bitmap != null) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(
                android.graphics.Bitmap.CompressFormat.PNG, 60, byteArrayOutputStream
            )
            byteArrayOutputStream.toByteArray()
        } else {
            null
        }
    }

    actual fun toImageBitmap(): ImageBitmap? {
        val byteArray = toByteArray()
        return if (byteArray != null) {
          return   BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size,
                BitmapFactory.Options())?.asImageBitmap()

           // return compressImage(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size))?.asImageBitmap()
        } else {
            null
        }
    }
}


private fun compressImage(bmp: android.graphics.Bitmap): android.graphics.Bitmap? {
    val baos = ByteArrayOutputStream()
    bmp.compress(
        android.graphics.Bitmap.CompressFormat.PNG,
        100,
        baos
    )
    var options = 90
    while (baos.toByteArray().size / 1024 > 400) {
        baos.reset()
        bmp.compress(
            android.graphics.Bitmap.CompressFormat.PNG,
            options,
            baos
        )
        options -= 10
    }
    val isBm =
        ByteArrayInputStream(baos.toByteArray())

    return BitmapFactory.decodeStream(isBm, null, null)
}