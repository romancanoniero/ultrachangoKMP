package com.iyr.ultrachango.data.api.cloud.location

import com.iyr.ultrachango.config.Config.BASE_URL_CLOUD_SERVER
import com.iyr.ultrachango.data.api.cloud.Response
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.UserAddress
import com.iyr.ultrachango.utils.coroutines.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CloudLocationsService(
    private val client: HttpClient
) : ICloudLocationsService {

    val urlBase = "$BASE_URL_CLOUD_SERVER/users/locations"

    /**
     * Guarda una lista de compras en la nube
     * @param shoppingList : ShoppingListModel
     */
    override suspend fun save(UserAddress: UserAddress): UserAddress? {
        val call = client.post(urlBase) {
            contentType(ContentType.Application.Json)
            setBody(UserAddress)
        }
        val response = call.body<Response<UserAddress>>()
        when (call.status.value) {
            200 -> {
                return response.payload
            }
            else -> {
                throw Exception(response.message ?: "Error desconocido")
            }
        }
    }

    override suspend fun list(userKey: String): List<UserAddress> {
        var result: List<UserAddress>? = null
        Resource.Loading<List<Product>?>()
        try {
            var url = "$urlBase/list/$userKey"
            var call = client.get(url)
                .body<List<UserAddress>>()
            result = call
        } catch (exception: Exception) {
            throw exception
        }
        return result
    }

    override suspend fun delete(userKey: String, locationID: Int) {
        try {
            val url = "$urlBase/$userKey/$locationID"
            client.delete(url)
        } catch (e: Exception) {
            throw e
        }
    }
}