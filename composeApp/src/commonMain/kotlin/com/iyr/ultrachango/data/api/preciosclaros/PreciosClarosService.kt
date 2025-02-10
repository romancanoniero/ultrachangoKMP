package com.iyr.ultrachango.data.api.preciosclaros

import com.iyr.ultrachango.data.Api.preciosclaros.model.producto.RemoteProductResult
import com.iyr.ultrachango.data.Api.preciosclaros.model.productos.RemoteProductsResult
import com.iyr.ultrachango.data.api.preciosclaros.models.sucursales.Sucursale
import com.iyr.ultrachango.data.api.preciosclaros.models.sucursales.SucursalesAPI
import com.iyr.ultrachango.utils.coroutines.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PreciosClarosService(
    private val client: HttpClient
) {

    suspend fun searchByBarCodeByLatLngRemote(
        barCode: String, latitude: Double, longitude: Double
    ): RemoteProductResult {

        try {
            val url = "https://d735s5r2zljbo.cloudfront.net/prod/producto?id_producto=${
                barCode
            }&lat=${latitude}&lng=${longitude}&limit=50"

            println(url)

            return client.get(url).body<RemoteProductResult>()

        } catch (ex: Exception) {
            println(ex.message)
            throw ex
        }
    }


    /***
     * Busca articulos por texto en las proximidades a una Lat, Lng
     */
    suspend fun searchByTextRemote(
        text: String, latitude: Double, longitude: Double
    ): Resource<RemoteProductsResult?> {

        var result: Resource<RemoteProductsResult?>? = null

        Resource.Loading<RemoteProductsResult?>()
        try {

            val url =
                "https://d735s5r2zljbo.cloudfront.net/prod/productos?string=${text}&lat=${latitude}&lng=${longitude}"

            var limit = 100
            var offset = 0

            while (result == null || ((result.data?.productos?.size ?: 0) < (result.data?.total
                    ?: 0)

                        )
            ) {
                //Log.d("EXTERNAL_API", "paginatedUrl: $paginatedUrl")
                val urlCall = url.plus("&offset=${offset}&limit=${limit}")

                var call = client.get(urlCall).body<RemoteProductsResult>()

                when (call.status) {
                    200 -> {
                        if (result == null) {
                            result = Resource.Success<RemoteProductsResult?>(call)
                        } else {
                            call.productos?.let {
                                result?.data?.productos?.addAll(it)

                            }
                        }
                        offset += limit
                    }

                    500 -> {
                        throw Exception(call.errorDescription ?: "Error desconocido")
                    }

                    else -> {
                        throw Exception("Error desconocido")
                    }
                }
            }
        } catch (exception: Exception) {
            result = Resource.Error(exception.message ?: "An unknown Error Occurred")
        }
        return result!!
    }


    suspend fun getStoresList(
        lat: Double, lng: Double, radius: Double, limit: Int?
    ): List<Sucursale>? {
        var result: SucursalesAPI? = null

        val url =
            "https://d735s5r2zljbo.cloudfront.net/prod/sucursales?lat=${lat}&lng=${lng}&limit=${limit}&distancia_max=${radius}"

        var limit = 100
        var offset = 0
        try {

            var lastResultsCount: Int? = null
            while (lastResultsCount == null || lastResultsCount != 0) {
                //Log.d("EXTERNAL_API", "paginatedUrl: $paginatedUrl")
                val urlCall = url.plus("&offset=${offset}&limit=${limit}")

                var call = client.get(urlCall).body<SucursalesAPI>()

                lastResultsCount = call.sucursales.size

                if (result == null) {
                    result = call
                } else {
                    result.sucursales.addAll(call.sucursales)
                }
                offset += limit


            }
        } catch (exception: Exception) {
            val pp = exception
        }
        return result?.sucursales
    }


}