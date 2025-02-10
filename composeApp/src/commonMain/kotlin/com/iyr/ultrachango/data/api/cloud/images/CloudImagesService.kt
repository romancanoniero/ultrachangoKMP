package com.iyr.ultrachango.data.api.cloud.images


import androidx.compose.ui.graphics.ImageBitmap
import com.iyr.ultrachango.config.Config.BASE_URL_CLOUD_SERVER
import com.iyr.ultrachango.data.api.cloud.Response
import com.iyr.ultrachango.data.database.repositories.getAuthToken
import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.utils.ui.toBase64
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.utils.io.InternalAPI
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ultrachango2.composeapp.generated.resources.Res
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class CloudImagesService(
    private val client: HttpClient,
    private val settings: Settings
) : ICloudImagesService {

    val urlBase = "$BASE_URL_CLOUD_SERVER/api"

    override suspend fun getProfileImage(userKey: String): ByteArray? {
        val authToken = settings.getAuthToken();
        try {
            val call = client.get("$urlBase/client/$userKey/image") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $authToken")
                }
                contentType(ContentType.Application.Json)
            }
            return call.body();
        } catch (e: Exception) {
            throw e
        }
        return null
    }


    @OptIn(ExperimentalEncodingApi::class, ExperimentalResourceApi::class)
    override suspend fun postProfileImage(userKey: String, fileName: String, bytes: ByteArray) {
        val authToken = settings.getAuthToken();
        try {

            val base64 = bytes.toBase64()

            val call1: HttpResponse = client.post(
                "$BASE_URL_CLOUD_SERVER/users/image_profile"
            ) {
                val culo = bytesToMegabytes(bytes.size.toLong())
                contentType(ContentType.MultiPart.FormData)
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("token", authToken.toString())
                            append("user_key", userKey)
                            append("file_name", fileName)
                            append("imageBase64", base64)
                        }
                    )
                )


            }




            return call1.body();
        } catch (e: Exception) {
            throw e
        }
    }
}


fun bytesToMegabytes(bytes: Long): Double {
    return bytes / 1_048_576.0
}