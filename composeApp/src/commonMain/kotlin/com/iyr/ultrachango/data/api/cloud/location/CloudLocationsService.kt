package com.iyr.ultrachango.data.api.cloud.location


import com.iyr.ultrachango.config.Config.BASE_URL_CLOUD_SERVER
import com.iyr.ultrachango.data.api.cloud.Response
import com.iyr.ultrachango.data.models.FamilyMember
import com.iyr.ultrachango.data.models.Location
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.utils.coroutines.Resource
import com.iyr.ultrachango.utils.ui.places.models.CustomPlace
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
    override suspend fun save(location: Location): Location? {
        val call = client.post(urlBase) {
            contentType(ContentType.Application.Json)
            setBody(location)
        }
        val response = call.body<Response<Location>>()
        when (call.status.value) {
            200 -> {
                return response.payload
            }

            else -> {
                throw Exception(response.message ?: "Error desconocido")
            }
        }
    }

    override suspend fun list(userKey: String): List<Location> {
        var result: List<Location>? = null

        Resource.Loading<List<Product>?>()
        try {
            var url = "$urlBase/list/$userKey"
            var call = client.get(url)
                .body<List<Location>>()

            result = call

        } catch (exception: Exception) {
            throw exception
        }
        return result
    }


    override suspend fun delete(userKey: String,locationID: Int) {
        try {
            var url = "$urlBase/$userKey/$locationID"
            var call = client.delete(url)
                .body<Response<Boolean>>()



        } catch (exception: Exception) {
            throw exception
        }

    }
}