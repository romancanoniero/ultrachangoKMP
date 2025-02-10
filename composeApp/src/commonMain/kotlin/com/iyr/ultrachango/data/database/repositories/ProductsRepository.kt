package com.iyr.ultrachango.data.database.repositories

import com.iyr.ultrachango.auth.AuthRepositoryImpl
import com.iyr.ultrachango.data.api.cloud.products.CloudProductsService
import com.iyr.ultrachango.data.api.preciosclaros.PreciosClarosService

import com.iyr.ultrachango.data.models.PriceInBranch
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.utils.coroutines.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow



class ProductsRepository(
    private val authRepository: AuthRepositoryImpl,
    private val preciosClarosService: PreciosClarosService,
    private val productsCloudService: CloudProductsService,
//    private val productsDao: ProductsDao,

    ) {



    suspend fun searchByBarCodeByLatLngRemote(
        barCode: String, latitude: Double, longitude: Double
    ) = preciosClarosService.searchByBarCodeByLatLngRemote(barCode, latitude, longitude)


    /**
     * Buscar un producto por código de barras en la nube con indicacion de las listas a las
     * que pertenece
     * @param ean : String
     * @param userId : String
     * @param latitude : Double
     * @param longitude : Double
     * @return Flow<Resource<List<Product>?>>
     */
    suspend fun searchByBarCodeByLatLngCloud(
        ean: String, latitude: Double, longitude: Double
    ): HashMap<String, Any> {
        val userKey = authRepository.getUserKey().toString()
        return productsCloudService.getProductByEANWithShoppingList(ean, userKey)
    }


    suspend fun searchByBarCodeByLatLngCloud(
        ean: String,

        latitude: Double,
        longitude: Double,
        onResults: (Resource<List<PriceInBranch>?>) -> Unit
    ) {
        val userKey = authRepository.getUserKey().toString()
        val apiCall = preciosClarosService.searchByBarCodeByLatLngRemote(ean, latitude, longitude)
        val precios = apiCall.sucursales.map { it -> it.toPriceInBranch() }
        val toReturn = precios
        //   val call =  productsCloudService.getProductByEANWithShoppingList(ean, userId )
// TODO : Implementar una llamada al mi servidor que haga la busqueda combinada en el servidor

        onResults(Resource.Success(toReturn))
    }


    suspend fun searchByText(
        text: String, latitude: Double, longitude: Double
    ): Flow<Resource<List<Product>?>> = flow {
        // Emitir productos locales primero
        /*
        val localCall = productsDao.searchByText(text)
        emit(Resource.Success<List<Product>?>(localCall))
*/
        emit(searchByTextInCloud(text, latitude, longitude))
        /*
                // Filtrar productos nuevos que no están en la base de datos local
                val newProducts = cloudProducts.filter { cloudProduct ->
                    productsDao.searchByText(text)
                        .none { localProduct -> localProduct.ean == cloudProduct.ean }
                }
                // Agregar los productos nuevos a la base de datos de Room
                if (newProducts.isNotEmpty()) {
                    productsDao.insertProducts(newProducts.toList())
                }

         */
        // Emitir la lista combinada de productos locales y remotos
        //    emit(Resource.Success<List<Product>?>(productsDao.searchByText(text)))

        // Hacer una consulta al servicio remoto en paralelo
        //  emit(searchByTextInRemote(text, latitude, longitude))
    }

    private suspend fun FlowCollector<Resource<List<Product>?>>.searchByTextInRemote(
        text: String,
        latitude: Double,
        longitude: Double
    ): Resource<List<Product>?>? {

        var call = preciosClarosService.searchByTextRemote(
            text = text,
            latitude = latitude,
            longitude = longitude

        )

        when (call) {
            is Resource.Success -> {
                val remoteProducts =
                    call.data?.productos?.map { it.toDomainProduct() } ?: emptyList()
                // Filtrar productos nuevos que no están en la base de datos local
/*
                val newProducts = remoteProducts.filter { remoteProduct ->
                    productsDao.searchByText(text)
                        .none { localProduct -> localProduct.ean == remoteProduct.ean }

                }
                // Agregar los productos nuevos a la base de datos de Room
                if (newProducts.isNotEmpty()) {
                    productsDao.insertProducts(newProducts.toList())
                }

 */
                // Emitir la lista combinada de productos locales y remotos
                return Resource.Success<List<Product>?>(remoteProducts)
            }

            is Resource.Error -> {
                return Resource.Error<List<Product>?>(call.message.toString())

            }

            else -> {
                return null
            }
        }
    }

    private suspend fun FlowCollector<Resource<List<Product>?>>.searchByTextInCloud(
        text: String,
        latitude: Double,
        longitude: Double
    ): Resource.Success<List<Product>?> {
        val callCloudService =
            productsCloudService.getProductByText(text, latitude, longitude)
        val cloudProducts =
            callCloudService.map { it }

        return Resource.Success<List<Product>?>(cloudProducts)

    }

    fun favoritesTogle(
    //    userKey: String,
        ean: String,
        favorite: Boolean
    ): Resource.Success<Product?> {
        val userKey = authRepository.getUserKey().toString()
        val callCloudService =
            productsCloudService.togleProductFavorite(userKey, ean, favorite)
        val result = Product()

        return Resource.Success<Product?>(result)

    }
}