package com.iyr.ultrachango.data.api.cloud.images


import com.iyr.ultrachango.auth.AuthRepository
import com.iyr.ultrachango.config.Config.BASE_URL_CLOUD_SERVER
import com.iyr.ultrachango.data.api.cloud.Response
import com.iyr.ultrachango.getAuthToken
import com.iyr.ultrachango.utils.ui.toBase64
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headers
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.io.encoding.ExperimentalEncodingApi

class CloudImagesService(
    private val client: HttpClient,
    private val settings: Settings,

) : ICloudImagesService {

    val urlBase = "$BASE_URL_CLOUD_SERVER/api"

    override suspend fun getProfileImage(userKey: String): ByteArray? {
        val authToken = settings.getAuthToken()
        try {
            val call = client.get("$urlBase/client/$userKey/image") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $authToken")
                }
                contentType(ContentType.Application.Json)
            }
            val response = call.body<Response<ByteArray>>()
            when (call.status.value) {
                200 -> {
                    return response.payload
                }
                else -> {
                    throw Exception(response.message ?: "Error desconocido")
                }
            }

        } catch (e: Exception) {
            throw e
        }
        return null
    }


    @OptIn(ExperimentalEncodingApi::class, ExperimentalResourceApi::class)
    override suspend fun postProfileImage(userKey: String, fileName: String, bytes: ByteArray) {
        val authToken = settings.getAuthToken()
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




            return call1.body()
        } catch (e: Exception) {
            throw e
        }
    }
}


fun bytesToMegabytes(bytes: Long): Double {
    return bytes / 1_048_576.0
}