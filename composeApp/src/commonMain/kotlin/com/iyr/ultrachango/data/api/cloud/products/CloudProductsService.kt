package com.iyr.ultrachango.data.api.cloud.products


import coil3.Bitmap
import coil3.ImageLoader
import coil3.request.ImageRequest
import com.iyr.ultrachango.Constants.PRODUCT_DOES_NOT_EXIST
import com.iyr.ultrachango.config.Config.BASE_URL_CLOUD_SERVER
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.utils.coroutines.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlinx.serialization.json.longOrNull

class CloudProductsService(
    private val client: HttpClient
) : ICloudProductsService {

    override suspend fun getProducts(): List<Product> {

        var result: List<Product>? = null

        Resource.Loading<List<Product>?>()
        try {
            var url = "$BASE_URL_CLOUD_SERVER/products/listProducts"

            //   url.plus("listProducts")

            var call = client.get(url)
                .body<List<Product>>()

            result = call

        } catch (exception: Exception) {
            throw exception
        }
        return result
    }

    override suspend fun getProductById(id: String): Product {
        return Product()
    }

    override suspend fun getProductByMarca(marca: String): List<Product> {

        return emptyList()
    }

    override suspend fun getProductByText(text: String,
                                          latitude: Double,
                                          longitude: Double): List<Product> {

        var result: List<Product>? = null

        Resource.Loading<List<Product>?>()
        try {

            var url = "$BASE_URL_CLOUD_SERVER/products/text/${text}/${latitude}/${longitude}"

            var call = client.get(url)
                .body<List<Product>>()

            result = call

        } catch (exception: Exception) {
            throw exception
        }
        return result

    }

    override suspend fun getProductByEAN(ean: String): Product {
        var result: Product? = null

        try {

            var url = "$BASE_URL_CLOUD_SERVER/products/ean/${ean}"

            var call = client.get(url)
                .body<Product>()

            result = call

        } catch (exception: Exception) {
            throw exception
        }
        return result
    }

    override suspend fun getProductByEANWithShoppingList(
        ean: String,
        userId: String
    ): HashMap<String, Any> {
        var result: HashMap<String, Any> = HashMap<String, Any>()

        try {

            var url = "$BASE_URL_CLOUD_SERVER/products/ean_user/${ean}/${userId}"

            var call = client.get(url)
                .bodyAsText()

            val jsonElement = Json.parseToJsonElement(call)
            var product = Product()
            jsonElement.jsonObject["product"]?.let { it ->

                if (it != JsonNull) {
                    val productJson = it.toString()
                    product = Json.decodeFromString<Product>(productJson)
                    result["product"] = product
                } else {
                    throw Exception(PRODUCT_DOES_NOT_EXIST)
                }
            }

            val shoppingLists = ArrayList<Long>()
            jsonElement.jsonObject["shoppingLists"]?.jsonArray?.mapNotNull { it.jsonPrimitive.longOrNull }
                ?.let {
                    shoppingLists.addAll(it)
                }
            result["product"] = product
            result["shoppingLists"] = shoppingLists.toList()

        } catch (exception: Exception) {
            throw exception
        }
        return result
    }


    override suspend fun createProduct(product: Product): Product {
        return Product()
    }

    override suspend fun updateProduct(product: Product): Product {
        return Product()
    }

    override suspend fun deleteProduct(id: String): Product {
        return Product()
    }

    override fun togleProductFavorite(userKey: String, ean: String, favorite: Boolean): Product {
        // TODO("Not yet implemented")
        return Product()
    }


  /*
    override suspend fun getProductImage(ean: String): Bitmap {
        try {
            val url = "$BASE_URL_CLOUD_SERVER/images/products/image/${ean}"
            val response = client.get(url)
            val byteArray = response.body<ByteArray>()
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } catch (exception: Exception) {
            throw exception
        }
    }
*/

}