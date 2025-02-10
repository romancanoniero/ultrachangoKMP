package com.iyr.ultrachango.utils.ui

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.mohamedrejeb.calf.picker.toImageBitmap
import io.ktor.util.encodeBase64
import java.io.ByteArrayOutputStream

actual fun ByteArray.toBase64(): String {
    val bitmap: Bitmap = this.toImageBitmap().asAndroidBitmap()
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return  byteArray.encodeBase64()
}