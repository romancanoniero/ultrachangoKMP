package com.iyr.ultrachango.data.database.repositories



import com.iyr.ultrachango.data.api.cloud.products.CloudProductsService
import com.iyr.ultrachango.data.api.preciosclaros.PreciosClarosService
import com.iyr.ultrachango.data.models.PriceInBranch
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ProductWithPricesAround
import com.iyr.ultrachango.domain.auth.AuthRepository
import com.iyr.ultrachango.utils.coroutines.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow



class ProductsRepository(
    private val authRepository: AuthRepository,
    private val preciosClarosService: PreciosClarosService,
    private val productsCloudService: CloudProductsService,
    ) {



    suspend fun searchByBarCodeByLatLngRemote(
        ean: String,
        latitude: Double,
        longitude: Double
    ) = preciosClarosService.searchByBarCodeByLatLngRemote(ean, latitude, longitude)


    suspend fun searchByBarCodeByLatLngRadius(
        ean: String, latitude: Double, longitude: Double, radius: Int
    ): HashMap<String, Any> {
        val userKey = authRepository.getUserKey().toString()
        return productsCloudService.getProductByEANLatLng(ean, latitude, longitude, radius)
    }


    /**
     * Buscar un producto por código de barras en la nube con indicacion de las listas a las
     * que pertenece
     * @param ean : String
     * @param userId : String
     * @param latitude : Double
     * @param longitude : Double
     * @return Flow<Resource<List<Product>?>>
     */
    suspend fun searchByBarCodeCloud(
        ean: String
    ): HashMap<String, Any> {
        val userKey = authRepository.getUserKey().toString()
        return productsCloudService.getProductByEANWithShoppingList(ean, userKey)
    }

/*

*/


    /**
     * Busca sugerencias de productos con información detallada, incluyendo precios en sucursales cercanas.
     *
     * Esta función recupera sugerencias de productos basadas en el texto de búsqueda y la ubicación proporcionados.
     * Obtiene información detallada sobre los productos sugeridos, incluyendo sus precios en las sucursales
     * dentro de una distancia especificada.
     *
     * @param text El texto de la consulta de búsqueda para encontrar sugerencias de productos.
     * @param latitude La latitud de la ubicación del usuario para la búsqueda por proximidad.
     * @param longitude La longitud de la ubicación del usuario para la búsqueda por proximidad.
     * @param distance La distancia máxima (en alguna unidad, probablemente metros o kilómetros dependiendo del backend)
     *                 a considerar las sucursales desde la ubicación del usuario.
     * @param onResults Una función de callback que se invocará con el resultado de la búsqueda.
     *                  El resultado está envuelto en un objeto [Resource] para indicar el estado de éxito, error o carga.
     *                  En caso de éxito, el recurso contendrá una lista de objetos [ProductWithPricesAround],
     *                  o null si no se encuentran sugerencias.
     */
    suspend fun searchProductsSuggestionsDetailed(
        text: String,
        latitude: Double,
        longitude: Double,
        distance : Double,

    ) : Resource<List<ProductWithPricesAround>?> {
        val userKey = authRepository.getUserKey().toString()

        val apiCall = productsCloudService.getSuggestedProductsWithDetailed(text, latitude, longitude, distance)
      //  val precios = apiCall.sucursales.map { it -> it.toPriceInBranch() }
      //  val toReturn = precios
        //   val call =  productsCloudService.getProductByEANWithShoppingList(ean, userId )
// TODO : Implementar una llamada al mi servidor que haga la busqueda combinada en el servidor

      //  onResults(Resource.Success(null))
        return Resource.Success(apiCall)
    }


    suspend fun searchByBarCodeCloud(
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
        emit(searchByTextInCloud(text, latitude, longitude) )
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
            callCloudService.map { it as Product }

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