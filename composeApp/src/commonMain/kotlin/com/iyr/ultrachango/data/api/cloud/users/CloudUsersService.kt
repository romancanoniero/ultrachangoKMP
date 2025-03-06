package com.iyr.ultrachango.data.api.cloud.users


import com.iyr.ultrachango.auth.AuthRepository
import com.iyr.ultrachango.auth.AuthenticatedUser
import com.iyr.ultrachango.config.Config.BASE_URL_CLOUD_SERVER
import com.iyr.ultrachango.data.api.cloud.Response

import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.getAuthToken
import com.russhwolf.settings.Settings

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

import io.ktor.http.contentType
import io.ktor.utils.io.InternalAPI
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CloudUsersService(
    private val client: HttpClient,
    private val settings: Settings,



    //  private val localStorage: AuthRepositoryImpl
) : ICloudUsersService {


    val urlBase = "$BASE_URL_CLOUD_SERVER/users"


    override suspend fun saveUser(user: AuthenticatedUser) {
        val token = settings.getAuthToken()
        val entityAsJson = Json.encodeToString(user)
        val call = client.post(urlBase) {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "json" to entityAsJson, "token" to token
                )
            )
        }

        val response = call.body<Response<String>>()

        when (call.status.value) {
            200 -> {
                //  return response.payload;
            }

            else -> {
                throw Exception(response.message ?: "Error desconocido")
            }
        }


        //     apiClient.saveUser(user) // Envía al servidor
        //    localStorage.saveUser(user) // Guarda localmente
    }

    override suspend fun getUser(userId: String): User? {
        val url = "$urlBase/get"
        val token = settings.getAuthToken()
        val call = client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "id" to userId,
                    "token" to token
                )
            )
        }

        val response = call.body<Response<User>>()

        when (call.status.value) {
            200 -> {
                return response.payload
            }

            else -> {
                throw Exception(response.message ?: "Error desconocido")
            }
        }
        return null
    }

    override suspend fun getAuthenticatedUser(userId: String): AuthenticatedUser? {
        val url = "$urlBase/get"
        val token = settings.getAuthToken()
        val call = client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "id" to userId,
                    "token" to token
                )
            )
        }

        val response = call.body<Response<AuthenticatedUser>>()

        when (call.status.value) {
            200 -> {
                return response.payload
            }

            else -> {
           //     throw Exception(response.message ?: "Error desconocido")
            }
        }
        return null
    }
    /*
        override suspend fun updateUser(user: User) {
    asdasdasd
        }
    */
    @OptIn(InternalAPI::class)
    override suspend fun updateUser(user: AuthenticatedUser, image: ByteArray?) {
        val token = settings.getAuthToken()

        val entityAsJson = Json.encodeToString(user)
        val call = client.post(urlBase) {
            contentType(ContentType.MultiPart.FormData)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("data", entityAsJson, Headers.build {
                            append(HttpHeaders.ContentType, "application/json")
                        })
                        append("token", token.toString())
                        image?.let {
                            append("image", image, Headers.build {
                                append(HttpHeaders.ContentType, "image/*")
                                append(HttpHeaders.ContentDisposition, "filename=\"image.jpg\"")
                            })
                        }
                    }
                )
            )
        }

        val response = call.body<Response<String>>()

        when (call.status.value) {
            200 -> {
                //  return response.payload;
            }

            else -> {
                throw Exception(response.message ?: "Error desconocido")
            }
        }

    }


    override suspend fun syncUser(userId: String): User {
        TODO("Not yet implemented")
    }

    /**
     * Guarda una lista de compras en la nube
     * @param shoppingList : ShoppingListModel
     *//*
        override suspend fun save(shoppingList: ShoppingList): ShoppingList? {
            val call = client.post(urlBase) {
                contentType(ContentType.Application.Json)
                setBody(shoppingList)
            }
            val response = call.body<Response<ShoppingList>>()
            when (call.status.value) {
                200 -> {
                    return response.payload;
                }

                else -> {
                    throw Exception(response.message ?: "Error desconocido")
                }
            }
        }
    */


}



