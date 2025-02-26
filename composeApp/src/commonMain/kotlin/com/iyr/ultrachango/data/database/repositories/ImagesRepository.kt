package com.iyr.ultrachango.data.database.repositories


import com.iyr.ultrachango.auth.AuthRepository
import com.iyr.ultrachango.data.api.cloud.images.CloudImagesService
import kotlinx.datetime.Clock


class ImagesRepository(
    private val authRepository: AuthRepository,
    private val imagesCloudService: CloudImagesService,
) {

    suspend fun getProfileImageURL(userKey: String): ByteArray? {
       return imagesCloudService.getProfileImage(userKey)
    }

    suspend fun postProfileImage(bytes: ByteArray) {

        val fileName = randomFileName("jpg")
        val userKey = authRepository.currentUserId
        return imagesCloudService.postProfileImage( userKey, fileName, bytes)
    }



}

private fun randomFileName(extension: String): String {
    val timestamp =  Clock.System.now().epochSeconds
    return "IMG_$timestamp.$extension"
}