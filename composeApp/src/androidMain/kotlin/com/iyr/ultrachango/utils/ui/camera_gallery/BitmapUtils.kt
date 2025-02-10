package com.iyr.ultrachango.utils.ui.camera_gallery

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import java.io.IOException
import java.io.InputStream


object BitmapUtils {
    fun getBitmapFromUri(uri: Uri, contentResolver: ContentResolver): android.graphics.Bitmap? {
        var inputStream: InputStream? = null
        return try {
            inputStream = contentResolver.openInputStream(uri)
            val s = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            s
        } catch (e: Exception) {
            e.printStackTrace()
            println("getBitmapFromUri Exception: ${e.message}")
            println("getBitmapFromUri Exception: ${e.localizedMessage}")
            null
        }
    }
}



fun modifyOrientation(bitmap: Bitmap, image_absolute_path: String?): Bitmap {
    val ei = ExifInterface(image_absolute_path!!)
    val orientation =
        ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90f)

        ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180f)

        ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270f)

        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(bitmap, true, false)

        ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(bitmap, false, true)

        else -> bitmap
    }
}

fun rotate(bitmap: Bitmap, degrees: Float): Bitmap {
    val matrix: Matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap {
    val matrix: Matrix = Matrix()
    matrix.preScale(if (horizontal) -1.0f else 1.0f, if (vertical) -1.0f else 1.0f)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}