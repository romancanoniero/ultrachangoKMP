package com.iyr.ultrachango.data.api.cloud.familymembers


import com.iyr.ultrachango.config.Config.BASE_URL_CLOUD_SERVER
import com.iyr.ultrachango.data.api.cloud.Response
import com.iyr.ultrachango.data.models.FamilyMember
import com.iyr.ultrachango.data.models.ShoppingListProduct
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.data.models.ShoppingListComplete
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CloudFamilyMembersService(
    private val client: HttpClient
) : ICloudFamilyMembersService {


    val urlBase = "$BASE_URL_CLOUD_SERVER/families/"


    /**
     * Guarda una lista de compras en la nube
     * @param shoppingList : ShoppingListModel
     */
/*
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

    override suspend fun get(userKey: String): FamilyMember {
   //     val urlBase = "$BASE_URL_CLOUD_SERVER/shoppinglist/get/$userKey/$listId"
        val url = urlBase.plus("get/$userKey")
        val call = client.get(url)

        val response = call.body<Response<FamilyMember>>()
        when (call.status.value) {
            200 -> {
                return response.payload!!
            }

            else -> {
                throw Exception(response.message ?: "Error desconocido")
            }
        }
    }

    override suspend fun getList(userKey: String): List<FamilyMember> {
        val url = urlBase.plus("list/$userKey")
        val call = client.get(url)

        val response = call.body<Response<List<FamilyMember>>>()
        when (call.status.value) {
            200 -> {
                return response.payload!!
            }

            else -> {
                throw Exception(response.message ?: "Error desconocido")
            }
        }
    }

}