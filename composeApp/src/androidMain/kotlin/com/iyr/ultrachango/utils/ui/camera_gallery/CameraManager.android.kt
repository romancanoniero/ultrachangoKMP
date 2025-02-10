package com.iyr.ultrachango.utils.ui.camera_gallery

import AppContext
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.ashampoo.kim.Kim
import com.ashampoo.kim.android.readMetadata
import com.ashampoo.kim.format.jpeg.JpegRewriter
import com.ashampoo.kim.format.tiff.constant.TiffTag
import com.ashampoo.kim.format.tiff.write.TiffOutputSet
import com.mohamedrejeb.calf.picker.toAndroidBitmap
import com.mohamedrejeb.calf.picker.toImageBitmap
import io.ktor.http.ContentDisposition.Companion.File
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.coroutineContext


@Composable
actual fun rememberCameraManager(onResult: (SharedImage?) -> Unit): CameraManager {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver
    var tempPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }
    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture(),
            onResult = { success ->
                if (success) {
                    val localFilePath = AppContext.context.cacheDir.toString()
                        .replace("user/0", "data") + "/" + tempPhotoUri.lastPathSegment

                    val bitmapRotated =
                        rotateImageIfRequired(context, Uri.parse("file:" + localFilePath))


                    onResult.invoke(SharedImage(
                        uri = localFilePath,
                        bitmap = bitmapRotated))
                    /*
                    saveBitmapToFile(context,bitmapRotated, Uri.parse("file:" + localFilePath!!).lastPathSegment.toString())


                    val compressedImage = bitmapRotated.compress().toAndroidBitmap()


                    saveBitmapToFile(context,compressedImage, Uri.parse("file:" + localFilePath!!).lastPathSegment.toString())


                    saveBitmapToFile(
                        context,
                        compressedImage,
                        Uri.parse("file:" + localFilePath!!).lastPathSegment.toString()
                    )

                    onResult.invoke(
                        SharedImage( uri = localFilePath.toString()  )
                    )
*/


                }
            })
    return remember {
        CameraManager(onLaunch = {
            tempPhotoUri = ComposeFileProvider.getImageUri(context)
            cameraLauncher.launch(tempPhotoUri)
        })
    }
}

actual class CameraManager actual constructor(
    private val onLaunch: () -> Unit
) {
    actual fun launch() {
        onLaunch()
    }
}


fun rotateImageIfRequired(context: Context, uri: Uri): Bitmap {

    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IllegalArgumentException("No se pudo abrir la imagen")

    // Leer los metadatos EXIF
    val exif = ExifInterface(inputStream)
    val orientation =
        exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

    // Decodificar la imagen en un Bitmap
    val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
        ?: throw IllegalArgumentException("No se pudo decodificar la imagen")

    val rotation = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> 0f
    }

    return if (rotation != 0f) {
        val matrix = Matrix().apply { postRotate(rotation) }
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } else {
        bitmap
    }
}


fun saveBitmapToFile(context: Context, bitmap: Bitmap, fileName: String): File? {
    // Crear un archivo en el almacenamiento interno de la app
    val file = File(context.cacheDir, fileName)

    return try {
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(
                Bitmap.CompressFormat.JPEG, 90, outputStream
            ) // Guardar como JPEG con calidad 90%
            outputStream.flush()
        }
        file // Retornar el archivo guardado
    } catch (e: IOException) {
        e.printStackTrace()
        null // Retornar null en caso de error
    }
}

fun compressImage(context: Context, imageUri: Uri, quality: Int = 80): ByteArray {
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    return outputStream.toByteArray()
}


fun ByteArray.compressImage(quality: Int = 80): ByteArray {
    val outputStream = ByteArrayOutputStream()
    this.toAndroidBitmap().compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    return outputStream.toByteArray()
}

fun Bitmap.compress(quality: Int = 80): ByteArray {
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    return outputStream.toByteArray()
}