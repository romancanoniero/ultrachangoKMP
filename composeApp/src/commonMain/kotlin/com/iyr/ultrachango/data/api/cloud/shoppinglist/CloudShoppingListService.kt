package com.iyr.ultrachango.data.api.cloud.shoppinglist


import com.iyr.ultrachango.config.Config.BASE_URL_CLOUD_SERVER
import com.iyr.ultrachango.data.api.cloud.Response
import com.iyr.ultrachango.data.models.ShoppingListProduct
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.data.models.ShoppingListComplete
import com.iyr.ultrachango.data.models.ShoppingListMember
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CloudShoppingListService(
    private val client: HttpClient
) : ICloudShoppingListService {


    override fun count(): Int {
        return 0
    }


    /**
     * Guarda una lista de compras en la nube
     * @param shoppingList : ShoppingListModel
     */

    override suspend fun save(shoppingList: ShoppingList): ShoppingList? {
        val url = "$BASE_URL_CLOUD_SERVER/shoppinglist"
        val call = client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(shoppingList)
        }
        val response = call.body<Response<ShoppingList>>()
        when (call.status.value) {
            200 -> {
                return response.payload
            }

            else -> {
                throw Exception(response.message ?: "Error desconocido")
            }
        }
    }


    override suspend fun get(userKey: String, listId: Int): ShoppingListComplete {
        val url = "$BASE_URL_CLOUD_SERVER/shoppinglist/get/$userKey/$listId"
        val call = client.get(url)

        val response = call.body<Response<ShoppingListComplete>>()
        when (call.status.value) {
            200 -> {
                return response.payload!!
            }

            else -> {
                throw Exception(response.message ?: "Error desconocido")
            }
        }
    }

    override suspend fun getAll(userKey: String): List<ShoppingListComplete> {
        val url = "$BASE_URL_CLOUD_SERVER/shoppinglist/list/$userKey"
        try {
            val call = client.get(url)

            val response = call.body<Response<List<ShoppingListComplete>>>()
            when (call.status.value) {
                200 -> {
                    return response.payload!!
                }

                else -> {
                    throw Exception(response.message ?: "Error desconocido")
                }
            }

        } catch (e: Exception) {
           // throw e
            return emptyList()
        }
    }

    override suspend fun delete(userKey: String, listId: Int): Boolean? {
        val url = "$BASE_URL_CLOUD_SERVER/shoppinglist/delete/$listId"
        val call = client.get(url)
        val response = call.body<Response<Boolean>>()
        when (call.status.value) {
            200 -> {
                return response.payload!!
            }

            else -> {
                throw Exception(response.message ?: "Error desconocido")
            }
        }

    }


    override fun getListProducts(listId: Int): List<ShoppingListProduct> {
        return emptyList()
    }

    override suspend fun addProductToList(
        listId: Int, ean: String, userKey: String, quantity: Int
    ) {
        val url = "$BASE_URL_CLOUD_SERVER/shoppinglist/product/add/$listId/$ean/$userKey/$quantity"
        val call = client.post(url)
        val response = call.body<Response<Boolean>>()
        when (call.status.value) {
            200 -> {
                return
            }

            else -> {
                throw Exception(response.message ?: "Error desconocido")
            }
        }
    }


    override suspend fun removeProductFromShoppingList(id: Int, ean: String) {

        try {
            val url = "$BASE_URL_CLOUD_SERVER/shoppinglist/product/remove/$id/$ean"
            val call = client.delete(url)
            val response = call.body<Response<Boolean>>()
            when (call.status.value) {
                200 -> {
                    return
                }

                else -> {
                    throw Exception(response.message ?: "Error desconocido")
                }
            }
        } catch (exception: Exception) {
            throw exception
        }
    }


    /***
     * Actualiza la cantidad de un producto en una lista de compras
     * @param listId : Int
     * @param ean : String
     * @param userKey : String
     * @param quantity : Double
     */
    override suspend fun updateProductQuantityOnList(
        listId: Int, ean: String, userKey: String, quantity: Double
    ) {
        try {
            val url =
                "$BASE_URL_CLOUD_SERVER/shoppinglist/product/qty/update/$listId/$ean/$userKey/$quantity"
            val call = client.post(url)
            val response = call.body<Response<Boolean>>()
            when (call.status.value) {
                200 -> {
                    return
                }

                else -> {
                    throw Exception(response.message ?: "Error desconocido")
                }
            }

        } catch (e: Exception) {
            throw e
        }
    }


    // ------- MEMBERS
    override suspend fun getMembers(listId: Long): List<ShoppingListMember> {
        val url = "$BASE_URL_CLOUD_SERVER/shoppinglist/members/$listId"
        val call = client.get(url)
        val response = call.body<Response<List<ShoppingListMember>>>()
        when (call.status.value) {
            200 -> {
                return response.payload!!
            }

            else -> {
                throw Exception(response.message ?: "Error desconocido")
            }
        }
    }


    override suspend fun getMembers(listId: Long, userId: String): List<ShoppingListMember> {
        try {
            val url = "$BASE_URL_CLOUD_SERVER/shoppinglist/members/get/$listId/$userId"
            val call = client.get(url)
            val response = call.body<Response<List<ShoppingListMember>>>()
            when (call.status.value) {
                200 -> {
                    return response.payload!!
                }

                else -> {
                    throw Exception(response.message ?: "Error desconocido")
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }


    override suspend fun addMember(listId: Long, userId: String) {
        try {
            val url = "$BASE_URL_CLOUD_SERVER/shoppinglist/members/add/$listId/$userId/false"
            val call = client.post(url)
            val response = call.body<Response<Boolean>>()
            when (call.status.value) {
                200 -> {
                    return
                }

                else -> {
                    throw Exception(response.message ?: "Error desconocido")
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun removeMember(listId: Long, userId: String) {
        try {
            val url = "$BASE_URL_CLOUD_SERVER/shoppinglist/members/remove/$listId/$userId"
            val call = client.delete(url)
            val response = call.body<Response<Boolean>>()
            when (call.status.value) {
                200 -> {
                    return
                }

                else -> {
                    throw Exception(response.message ?: "Error desconocido")
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }
}