package com.iyr.ultrachango.ui.screens.home


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.Constants
import com.iyr.ultrachango.auth.AuthRepository
import com.iyr.ultrachango.auth.AuthenticatedUser

import com.iyr.ultrachango.data.database.repositories.ProductsRepository
import com.iyr.ultrachango.data.database.repositories.ShoppingListRepository
import com.iyr.ultrachango.data.database.repositories.UserLocationsRepository
import com.iyr.ultrachango.data.models.Location
import com.iyr.ultrachango.data.models.Locations
import com.iyr.ultrachango.data.models.PriceInBranch
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ProductOnSearch
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.data.models.ShoppingListComplete
import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.ui.screens.navigation.AppRoutes
import com.iyr.ultrachango.utils.coroutines.Resource
import com.iyr.ultrachango.utils.extensions.toLocalLocation
import com.iyr.ultrachango.utils.geo.getPlaceFromCoordinates
import com.iyr.ultrachango.viewmodels.UserViewModel
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.jordond.compass.Place
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import io.ktor.client.call.NoTransformationFoundException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent

class HomeScreenViewModel(
    private val productsRepository: ProductsRepository,
    private val userLocationsRepository: UserLocationsRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val userViewModel: UserViewModel,
    private val authRepositoryImpl: AuthRepository,
    private val scaffoldVM: ScaffoldViewModel,
) : ViewModel(), KoinComponent {

    /*
        var state by mutableStateOf(UiState())
            private set

     */

    val permissionsController = mutableStateOf<PermissionsController?>(null)

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val _fetchingLocations = MutableStateFlow(false)
    val fetchingLocations: StateFlow<Boolean> = _fetchingLocations.asStateFlow()


    // - Busqueda de Productos
    private val _productsList = MutableStateFlow<List<Product>>(emptyList())
    val productsList: StateFlow<List<Product>> = _productsList.asStateFlow()

    private val _productsDropdownExpanded = MutableStateFlow<Boolean>(false)
    val productsDropdownExpanded: StateFlow<Boolean> = _productsDropdownExpanded.asStateFlow()
    // -------------------------


    private var searchJob: kotlinx.coroutines.Job? = null

    private val _showCameraPreview = MutableStateFlow(false)
    val showCameraPreview: StateFlow<Boolean> = _showCameraPreview.asStateFlow()


    private val _myName =
        MutableStateFlow<String>(authRepositoryImpl.getCurrentUser()?.firstName ?: "")
    val myName: StateFlow<String> = _myName.asStateFlow()


    private var shoppingLists = emptyList<ShoppingList>()

    init {


        scaffoldVM.showLoader(true)


        //     fetchLocation()
    }


    fun fetchData(requestRealLocation: Boolean = false) {

        permissionsController.let { _controller ->
            viewModelScope.launch {

                //      try {
                val permissionsController = _controller.value!!
                val granted =
                    permissionsController.isPermissionGranted(Permission.LOCATION)

                println("HomeScreenViewModel - fetchData - granted = $granted")

                if (granted) {
                    executeMultipleRequests(true)
                } else {

                    try {


                        permissionsController.providePermission(Permission.LOCATION)
                        val result =
                            permissionsController.getPermissionState(Permission.LOCATION)
                        when (result) {
                            PermissionState.NotDetermined -> {
                                executeMultipleRequests(false)
                            }

                            PermissionState.NotGranted -> {
                                permissionsController.providePermission(Permission.LOCATION)
                                //                 executeMultipleRequests(false)
                            }

                            PermissionState.Granted -> {
                                executeMultipleRequests(true)
                            }

                            PermissionState.Denied -> {
                                //                   permissionsController.providePermission(Permission.LOCATION)
                                executeMultipleRequests(false)
                            }

                            PermissionState.DeniedAlways -> {
                                executeMultipleRequests(false)
                            }
                        }

                    } catch (e: Exception) {
                        executeMultipleRequests(false)
                    }
                }
                /*
                     } catch (e: Exception) {
                         scaffoldVM.showLoader(false)
                         _state.value = _state.value.copy(
                             errorMessage = e.message, showErrorMessage = true
                         )
                     }
                     */
            }

        }
    }

    private suspend fun executeMultipleRequests(requestRealLocation: Boolean) {
        try {


            var shoppingLists: List<ShoppingListComplete>? = null
            var locations: List<Location>? = null

            val deferredResults = listOf(viewModelScope.async {
                shoppingLists = getShoppingLists()
            }, viewModelScope.async {
                fetchLocations(requestRealLocation)
            })
            deferredResults.awaitAll()
            val shoppingListSimple = shoppingLists?.map { it.toShoppingList() }

            scaffoldVM.showLoader(false)
            //       _knownLocations.value = locations ?: emptyList()

        } catch (exception: NoTransformationFoundException) {
            val errorMessage = exception.message
            if (errorMessage.contains("404")) {
                //     throw Exception("NO_INTERNET")
                scaffoldVM.showLoader(false)

                _state.value = _state.value.copy(
                    errorMessage = "No hay Conexion a Internet", showErrorMessage = true
                )
            } else
                throw exception
        }
    }


    private suspend fun getShoppingLists(): List<ShoppingListComplete> {
        return shoppingListRepository.list()
    }


    fun onSearchByCodebar() {

    }


    /***
     * Busca articulos por texto en las proximidades a una Lat, Lng
     * @param text
     *
     */
    fun onProductTextInput(
        text: String,
        lat: Double = -34.586050,
        lng: Double = -58.504600
    ) {
        scaffoldVM.showLoader(true)
        _state.value = _state.value.copy(
            showKeyboard = false,
        )
        searchJob?.let {
            it.cancel()
        }
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            productsRepository.searchByText(text, lat, lng).onStart {
                // Emitir estado de carga
                scaffoldVM.showLoader(true)
            }.catch { exception ->
                // Manejar errores
                val error = exception
                scaffoldVM.showLoader(false)
            }.collect { resource ->
                // Actualizar el estado con los resultados de búsqueda

                when (resource) {
                    is Resource.Success -> {

                        scaffoldVM.showLoader(false)

                        _state.value = _state.value.copy(
                            showPulldownIcon = true,
                            //    searchResultsExpanded = true,
                            //    searchResults = resource.data ?: emptyList()
                        )
                        _productsDropdownExpanded.value = true
                        _productsList.value = resource.data ?: emptyList()
                    }

                    is Resource.Error -> {
                        scaffoldVM.showLoader(false)

                        _state.value = _state.value.copy(
                            showErrorMessage = true,
                            errorMessage = resource.message
                        )


                    }

                    else -> {
                        null
                    }
                }

            }
        }

    }

    fun onFocusChanged(hasFocus: Boolean) {
        _state.value = _state.value.copy(showKeyboard = hasFocus)
    }

    fun onEmptySeachText() {
        _state.value = _state.value.copy(
            //           searchResults = arrayListOf(),
            showPulldownIcon = false,
            //      searchResultsExpanded = false
        )
        _productsDropdownExpanded.value = false
        _productsList.value = emptyList()
    }

    fun onPulldownStatusInvert() {
        val currentUiState = state.value

        _state.value = _state.value.copy(
            //        searchResults = currentUiState.searchResults,
            showPulldownIcon = !currentUiState.showPulldownIcon,
            // searchResultsExpanded = !currentUiState.searchResultsExpanded
        )
        _productsDropdownExpanded.value = !_productsDropdownExpanded.value
    }

    fun onPulldownCloseRequest() {
        val currentUiState = state.value
        _state.value = _state.value.copy(
            //searchResults = currentUiState.searchResults,
            showPulldownIcon = !currentUiState.showPulldownIcon,
            //       searchResultsExpanded = false
        )
        _productsDropdownExpanded.value = false
    }

    fun onShowProductRequest(product: ProductOnSearch) {
        _state.value = _state.value.copy(
            productToShow = product, shoppingLists = shoppingLists
        )
    }

    fun onProductAlreadyShown() {
        _state.value = _state.value.copy(productToShow = null)
    }

    fun onListUnselected(list: ShoppingList) {
        viewModelScope.launch {
            shoppingListRepository.removeProductFromList(
                list.listId!!,
                _state.value.productToShow?.ean!!
            )
        }
    }

    fun onListSelected(list: ShoppingList) {
        viewModelScope.launch {
            val ean = _state.value.productToShow?.ean!!
            shoppingListRepository.addProductToList(
                id = list.listId!!, ean = ean, 1
            )
        }
    }

    fun onFavButtonPressed(product: Product, favorite: Boolean) {
        viewModelScope.launch {
            val userKey = userViewModel.user.value?.uid.toString()
            val ean = product.ean

            productsRepository.favoritesTogle(
                ean = ean, favorite = favorite
            )
        }
    }


    fun closeErrorDialogRequest() {

        _state.value = _state.value.copy(
            showErrorMessage = false
        )
    }

    // Funciones de Scanneo
    fun onScanPressed() {
        viewModelScope.launch {
            try {
                _showCameraPreview.value = true
            } catch (deniedAlways: DeniedAlwaysException) {
                // Permission is always denied.
                var pp = deniedAlways
            } catch (denied: DeniedException) {
                // Permission was denied.
                var pp = denied
            }
        }
    }

    fun resetCameraPreview() {
        _showCameraPreview.value = false
    }

    fun hideScanner() {
        _showCameraPreview.value = false
    }

    fun onBarcodeScanned(barcode: String) {
        val userId = userViewModel.user.value?.uid.toString()

        viewModelScope.launch {
            try {
                val response = productsRepository.searchByBarCodeByLatLngCloud(
                    barcode, -34.586050, -58.504600
                )
                val productToShow = response.get("product") as Product
                val listWhereProductIs = response.get("shoppingLists") as List<Long>
                _state.value = _state.value.copy(
                    productToShow = productToShow.toProductOnSearch(),
                    productShoppingLists = listWhereProductIs
                )


            } catch (e: Exception) {
                var message = e.message

                when (message) {
                    "PRODUCT_DOES_NOT_EXIST" -> message = "Producto no encontrado"
                    "NO_INTERNET" -> message = "No hay conexión a internet"
                    else -> message = "Error desconocido - ${e.message}"
                }
                _state.value = _state.value.copy(
                    showErrorMessage = true, errorMessage = message
                )
            }

        }

    }

    //------------------------------------


    fun onCreateRoutToShoppingListRequested(listId: Int): String {
        val userKey = userViewModel.getUserKey()
        return AppRoutes.ShoppingListEditRoute.createRoute(userKey, listId)
    }

    fun closeDropDown() {
        _state.value = _state.value.copy(
//            searchResultsExpanded = false
        )
        _productsDropdownExpanded.value = false
    }

    fun getUserFirstName(): String? {
        return authRepositoryImpl.getCurrentUser()?.firstName
    }

    fun getUserKey(): String {
        return authRepositoryImpl.getUserKey().toString()
    }

    fun getUser(): AuthenticatedUser {
        return authRepositoryImpl.getCurrentUser()!!
    }


    //------------ Locations -------------------------------------


    private val _knownLocations = MutableStateFlow(emptyList<Location>())
    val knownLocations: StateFlow<List<Location>> =
        _knownLocations.asStateFlow()

    private val _currentLocation = MutableStateFlow<dev.jordond.compass.Location?>(null)
    val currentLocation: StateFlow<dev.jordond.compass.Location?> =
        _currentLocation.asStateFlow()

    // Ubicacion obtenida del usuario
    private var myLocation: dev.jordond.compass.Location? = null

    // Ubicaciones del usuario
    private var locations: ArrayList<Location>? = ArrayList<Location>()

    /**
     * Método que se llama cuando se obtiene la ubicación del usuario
     */
    suspend fun onLocationObtained(location: dev.jordond.compass.Location?) {
        myLocation = location
        myLocation?.let {
            val location = it.toLocalLocation()
            val lat = it.coordinates.latitude
            val lng = it.coordinates.longitude


            //  return geoCode(it)
            getPlaceFromCoordinates(viewModelScope,
                lat,
                lng,
                onResult = { place ->

                    location.title = place?.locality.toString()
                    updateMyLocation(location)

                })

        } ?: run {
            //     updateMyLocation(null)
            //  return null
        }
    }

    suspend fun geoCode(location: dev.jordond.compass.Location?): Location? {
        myLocation = location
        myLocation?.let {

            val lat = it.coordinates.latitude
            val lng = it.coordinates.longitude
            return getPlaceFromCoordinates(lat, lng)!!.toLocalLocation()
        } ?: run {
            return null
        }
    }


    private suspend fun fetchLocations(requestRealLocation: Boolean) {

        println("fetchLocations = " )
        val userKey = authRepositoryImpl.getUserKey()
        var deferredResults: List<Deferred<Any>> = emptyList()


        if (requestRealLocation) {
            deferredResults = listOf(
                viewModelScope.async {
                    getUserLocations(userKey)
                },
                viewModelScope.async {
                    _fetchingLocations.value = true
                    val result = fetchLocation()
                    val location = geoCode(result.getOrNull())
                    _fetchingLocations.value = false

                    var toReturn = List<Location>(1) {
                        location!!.apply {
                            title = location.province.toString()
                            locationType = Locations.CURRENT_LOCATION
                        }
                    }
                    toReturn
                }
            )
        } else {
            deferredResults = listOf(
                viewModelScope.async {
                    getUserLocations(userKey)
                },
            )
        }

        val responseConbined = ArrayList<Location>()
        val results = deferredResults.awaitAll()
        val userLocations = (results[0] as ArrayList<Location>)

        responseConbined.addAll(userLocations)

        if (requestRealLocation && results.size > 1) {
            val currentLocation = results[1].get(0)
            responseConbined.add(currentLocation)
        } else {
            if (permissionsController.value?.isPermissionGranted(Permission.LOCATION) == true == false) {
                val enableLocationsServiceOption = Location(
                    title = "Habilitar Servicio de Ubicación",
                    locationType = Locations.ENABLE_LOCATION
                )
                responseConbined.add(enableLocationsServiceOption)
            }
        }
        //   onLocationsListUpdate(responseConbined.toList())
        println("ViewModel - ubicaciones = " + Json.encodeToString(responseConbined.toList()))
        _knownLocations.emit(responseConbined.toList())
    }


    private suspend fun getCurrentLocation(
        permissionsController: PermissionsController,
    ): GeolocatorResult {
        val geolocator: Geolocator = Geolocator.mobile()
        val result: GeolocatorResult = geolocator.current()
        return result
    }

    /*
        private suspend fun getCurrentLocation(
            permissionsController: PermissionsController,
            onLocationObtained: (Location?) -> Unit,
            onError: (String) -> Unit
        ) {
            val geolocator: Geolocator = Geolocator.mobile()

            val result: GeolocatorResult = geolocator.current()
            when (result) {
                is GeolocatorResult.Success -> {
                    onLocationObtained(result.data)
                }

                is GeolocatorResult.Error -> when (result) {
                    is GeolocatorResult.NotSupported -> onError("Geolocation not supported")
                    is GeolocatorResult.NotFound -> onError("Location not found")
                    is GeolocatorResult.PermissionError -> onError("Permission error")
                    is GeolocatorResult.GeolocationFailed -> onError("Geolocation failed")
                    is GeolocatorResult.Error -> onError("Unknown error")
                    is GeolocatorResult.Success -> onError("Unexpected success")
                }
            }
        }
    */

    private suspend fun fetchLocation(): GeolocatorResult {
        //     viewModelScope.launch {
        //   val currentLocation = getCurrentLocation(permissionsController?.value!!)

        return getCurrentLocation(permissionsController.value!!)
        //      }
    }


    private suspend fun getUserLocation() {

    }

    private suspend fun getUserLocations(userKey: String): List<Location> {
        return userLocationsRepository.list(userKey)
    }

    private fun onLocationsListUpdate(updated: List<Location>) {
        val oldLocations = locations ?: emptyList()
        val newList =
            updated.filter { it.locationType != Locations.CURRENT_LOCATION }
        newList.let { it ->
            _knownLocations.value = it.toList()
        }

    }

    private fun updateMyLocation(myLocation: Location?) {
        var newResult = ArrayList<Location>()
        if (myLocation == null) {
            // Eliminar la ubicación con locationType = CURRENT_LOCATION

            //   newResult = _state.value.locations?.filter { it.locationType != Locations.CURRENT_LOCATION }
            var pepe = 22
        } else {
            // Verificar si existe una ubicación con locationType = CURRENT_LOCATION
            val currentLocationIndex =
                locations?.indexOfFirst { it.locationType == Locations.CURRENT_LOCATION }
            locations


            if (currentLocationIndex != null && currentLocationIndex != -1) {
                // Modificar la ubicación existente
                //       _state.value.locations?.set(currentLocationIndex, myLocation)
            } else {
                // Agregar la nueva ubicación
                val location = myLocation.copy(locationType = Locations.CURRENT_LOCATION)



                locations = ArrayList<Location>(locations!!.toList())
                locations!!.add(location)
                _knownLocations.value = locations as ArrayList<Location>

            }

            _fetchingLocations.value = false
        }
    }


    fun onLocationSelected(location: Location) {
        val settings: Settings = Settings()
        if (location.locationType != Locations.ENABLE_LOCATION) {
            settings[Constants.CURRENT_LOCATION] = Json.encodeToString(location)
            _state.value = _state.value.copy(locationSelected = location)
        } else {


            //viewModelScope.launch(Dispatchers.Main) {
            //sss
            //   fetchLocations(true)

            permissionsController.value?.openAppSettings()
//               permissionsController.value?.providePermission(Permission.LOCATION)
            //  }
        }
    }

    suspend fun getRefLocation(): Pair<Double, Double>? {
        var response: Pair<Double, Double>? = null
        val settings: Settings = Settings()
        val value = settings.getStringOrNull(Constants.CURRENT_LOCATION)
        if (value != null) {
            val location = Json.decodeFromString<Location>(value)
            when (location.locationType) {
                Locations.CUSTOM -> {
                    response = Pair(location.latitude, location.longitude)
                }

                Locations.CURRENT_LOCATION -> {
                    val locationResult = fetchLocation()

                    locationResult.getOrNull()?.let { it ->
                        val location = it.toLocalLocation()
                        val lat = it.coordinates.latitude
                        val lng = it.coordinates.longitude
                        response = Pair(location.latitude, location.longitude)
                    }


                }

                else -> {

                }
            }
        }
        return response
    }

    fun requestCurrentLocation() {

        var pp = 3
        // TODO("Not yet implemented")
    }

    fun setPermissionsController(permissionsController: PermissionsController) {
        this.permissionsController.value = permissionsController


        println("HomeScreenViewModel - setPermissionsController - permissionsController = $permissionsController")
        fetchData(false)


    }

    suspend fun getPrices(
        ean: String,
        userId: String,
        latitude: Double,
        longitude: Double,
        onResults: (Resource<List<PriceInBranch>?>) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        try {
            val userKey = userViewModel.getUserKey()
            val response = productsRepository.searchByBarCodeByLatLngCloud(
                ean = ean,
                latitude = -34.586050,
                longitude = -58.504600,
                onResults = { results ->
                    onResults(results)
                }
            )

        } catch (exception: Exception) {
            onError(exception.message.toString())
        }
    }


    data class UiState(
        val showKeyboard: Boolean = false,
        //      val searchResults: List<Product> = emptyList(),
        val productToShow: ProductOnSearch? = null,
        val productShoppingLists: List<Long> = emptyList(), // Lista de IDs de listas de compras en las que está el producto
        val showPulldownIcon: Boolean = true,
        //       val searchResultsExpanded: Boolean = true,
        val shoppingLists: List<ShoppingList>? = null,
        // val locations: List<Location>? = null,
        val locationSelected: Location? = null,
        val showErrorMessage: Boolean = false,
        val errorMessage: String? = null,
        //   val waitForLocation: Boolean = false
    )

}


fun Place.toLocalLocation(): Location {
    return Location(
        title = locality.toString(),
        street = thoroughfare.toString(),
        number = subThoroughfare.toString(),
        city = locality.toString(),
        province = subLocality.toString(),
        country = country.toString(),
        postalCode = postalCode.toString(),
        latitude = coordinates.latitude,
        longitude = coordinates.longitude
    )
}