package com.iyr.ultrachango.data.api.cloud.images

import androidx.compose.ui.graphics.ImageBitmap
import com.iyr.ultrachango.data.models.User

interface ICloudImagesService  {

    suspend fun getProfileImage(userKey : String): ByteArray?
    suspend fun postProfileImage(userKey: String, fileName: String, bytes: ByteArray)


}