package com.iyr.ultrachango.utils.ui


import androidx.compose.ui.graphics.ImageBitmap
import coil3.Uri
import com.ashampoo.kim.Kim
import com.ashampoo.kim.model.MetadataUpdate
import com.ashampoo.kim.model.TiffOrientation
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.max


/*
   In commonMain folder
*/
expect fun ByteArray.toBase64():String

/*
fun getOrientation(context: Context, photoUri: android.net.Uri?): Int {
    val cursor: android.database.Cursor = context.getContentResolver().query(
        photoUri,
        arrayOf<String>(MediaStore.Images.ImageColumns.ORIENTATION), null, null, null
    )

    if (cursor == null || cursor.getCount() != 1) {
        return 90 //Assuming it was taken portrait
    }

    cursor.moveToFirst()
    return cursor.getInt(0)
}
*/
/**
 * Rotates and shrinks as needed
 */
/*
@Throws(java.io.IOException::class)
fun getCorrectlyOrientedImage(
    context: android.content.Context,
    photoUri: android.net.Uri?,
    maxWidth: Int
): android.graphics.Bitmap {
    var `is`: java.io.InputStream = context.getContentResolver().openInputStream(photoUri)
    val dbo: android.graphics.BitmapFactory.Options = android.graphics.BitmapFactory.Options()
    dbo.inJustDecodeBounds = true
    BitmapFactory.decodeStream(`is`, null, dbo)
    `is`.close()


    val rotatedWidth: Int
    val rotatedHeight: Int
    val orientation = getOrientation(context, photoUri)

    if (orientation == 90 || orientation == 270) {
        android.util.Log.d("ImageUtil", "Will be rotated")
        rotatedWidth = dbo.outHeight
        rotatedHeight = dbo.outWidth
    } else {
        rotatedWidth = dbo.outWidth
        rotatedHeight = dbo.outHeight
    }

    var srcBitmap: android.graphics.Bitmap
    `is` = context.getContentResolver().openInputStream(photoUri)
    android.util.Log.d(
        "ImageUtil", String.format(
            "rotatedWidth=%s, rotatedHeight=%s, maxWidth=%s",
            rotatedWidth, rotatedHeight, maxWidth
        )
    )
    if (rotatedWidth > maxWidth || rotatedHeight > maxWidth) {
        val widthRatio = (rotatedWidth.toFloat()) / (maxWidth.toFloat())
        val heightRatio = (rotatedHeight.toFloat()) / (maxWidth.toFloat())
        val maxRatio = max(widthRatio, heightRatio)
        android.util.Log.d(
            "ImageUtil", String.format(
                "Shrinking. maxRatio=%s",
                maxRatio
            )
        )

        // Create the bitmap from file
        val options: android.graphics.BitmapFactory.Options =
            android.graphics.BitmapFactory.Options()
        options.inSampleSize = maxRatio.toInt()
        srcBitmap = BitmapFactory.decodeStream(`is`, null, options)
    } else {
        android.util.Log.d(
            "ImageUtil", String.format(
                "No need for Shrinking. maxRatio=%s",
                1
            )
        )

        srcBitmap = BitmapFactory.decodeStream(`is`)
        android.util.Log.d("ImageUtil", String.format("Decoded bitmap successful"))
    }
    `is`.close()

    /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */
    if (orientation > 0) {
        val matrix: Matrix = Matrix()
        matrix.postRotate(orientation)

        srcBitmap = android.graphics.Bitmap.createBitmap(
            srcBitmap, 0, 0, srcBitmap.getWidth(),
            srcBitmap.getHeight(), matrix, true
        )
    }

    return srcBitmap
}
*/