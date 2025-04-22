package com.iyr.ultrachango.ui.screens.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.Constants
import com.iyr.ultrachango.data.database.repositories.ProductsRepository
import com.iyr.ultrachango.data.database.repositories.ShoppingListRepository
import com.iyr.ultrachango.data.database.repositories.UserLocationsRepository
import com.iyr.ultrachango.data.models.PriceInBranch
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ProductOnSearch
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.data.models.ShoppingListComplete
import com.iyr.ultrachango.data.models.UserAddress
import com.iyr.ultrachango.data.models.Locations
import com.iyr.ultrachango.data.models.toProductOnSearch
import com.iyr.ultrachango.data.models.toReferenceLocation
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.ui.screens.navigation.AppRoutes
import com.iyr.ultrachango.utils.auth_by_cursor.models.AppUser
import com.iyr.ultrachango.utils.auth_by_cursor.repository.AuthRepository
import com.iyr.ultrachango.utils.coroutines.Resource
import com.iyr.ultrachango.utils.extensions.toLocalLocation
import com.iyr.ultrachango.utils.geo.getPlaceFromCoordinates
import com.iyr.ultrachango.utils.isGpsEnabled
import com.iyr.ultrachango.utils.isGpsPresent
import com.iyr.ultrachango.viewmodels.UserViewModel
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import com.ultrachango2.features.location.domain.model.LocationType
import com.ultrachango2.features.location.domain.model.ReferenceLocation
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import kotlin.collections.ArrayList
import kotlin.collections.plus

class HomeScreenViewModel(
    private val productsRepository: ProductsRepository,
    private val userLocationsRepository: UserLocationsRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val userViewModel: UserViewModel,
    private val authRepository: AuthRepository,
    private val scaffoldVM: ScaffoldViewModel,
    private val permissionsController: PermissionsController,
) : ViewModel(), KoinComponent {


    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()


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
        MutableStateFlow<String>(authRepository.getCurrentUser()?.displayName ?: "")
    val myName: StateFlow<String> = _myName.asStateFlow()


    private var shoppingLists = emptyList<ShoppingList>()

    init {

        //scaffoldVM.showLoader(true)

        viewModelScope.launch(Dispatchers.IO) {
            fetchLocation()
        }

    }


    fun fetchData(requestRealLocation: Boolean = false) {

        permissionsController.let { _controller ->
            viewModelScope.launch {
                //      try {
                //   val permissionsController = _controller.value!!
                val granted =
                    permissionsController.isPermissionGranted(Permission.LOCATION)

                println("HomeScreenViewModel - fetchData - granted = $granted")

                if (granted) {
                    executeMultipleRequests(true)
                } else {
                    try {

                        //                permissionsController.providePermission(Permission.LOCATION)
                        //  val result = permissionsController.getPermissionState(Permission.LOCATION)
                        val result =
                            permissionsController.getPermissionState(Permission.LOCATION)


                        when (result) {
                            PermissionState.NotDetermined -> {
                                executeMultipleRequests(false)
                            }

                            PermissionState.Denied -> {
                                permissionsController.getPermissionState(Permission.LOCATION)
                                //                 executeMultipleRequests(false)
                            }

                            PermissionState.Granted -> {
                                executeMultipleRequests(true)
                            }

                            PermissionState.DeniedAlways -> {
                                //                   permissionsController.providePermission(Permission.LOCATION)
                                executeMultipleRequests(false)
                            }


                            PermissionState.NotDetermined -> executeMultipleRequests(
                                false
                            )

                            else -> {}
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
            var UserAddresses: List<UserAddress>? = null

            val deferredResults = listOf(viewModelScope.async {
                shoppingLists = getShoppingLists()
            }, viewModelScope.async {
                fetchLocations(requestRealLocation)
            })
            deferredResults.awaitAll()
            val shoppingListSimple = shoppingLists?.map { it.toShoppingList() }

            _state.update {
                it.copy(
                    shoppingLists = shoppingListSimple,
                )
            }

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
            val userKey = userViewModel.user.value?.userKey.toString()
            val ean = product.ean

            productsRepository.favoritesTogle(
                ean = ean!!, favorite = favorite
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
        val userId = userViewModel.user.value?.userKey.toString()

        viewModelScope.launch {
            try {
                val response = productsRepository.searchByBarCodeCloud(
                    barcode
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


    fun onCreateRoutToShoppingListRequested(listId: Int, listName: String): String {
        val userKey = userViewModel.getUserKey()
        return AppRoutes.ShoppingListEditRoute.createRoute(userKey, listId, listName)
    }

    fun closeDropDown() {
        _state.value = _state.value.copy(
//            searchResultsExpanded = false
        )
        _productsDropdownExpanded.value = false
    }

    fun getUserFirstName(): String? {
        return authRepository.getCurrentUser()?.displayName
    }

    fun getUserKey(): String {
        return authRepository.getUserKey().toString()
    }

    fun getUser(): AppUser {
        return authRepository.getCurrentUser()!!
    }


    //------------ Locations -------------------------------------


    private val _knownLocations = MutableStateFlow(emptyList<ReferenceLocation>())
    val knownLocations: StateFlow<List<ReferenceLocation>> =
        _knownLocations.asStateFlow()

    private val _currentLocation = MutableStateFlow<dev.jordond.compass.Location?>(null)
    val currentLocation: StateFlow<dev.jordond.compass.Location?> =
        _currentLocation.asStateFlow()

    // Ubicacion obtenida del usuario
    private var myLocation: dev.jordond.compass.Location? = null

    // Ubicaciones del usuario
    private var UserAddresses: ArrayList<UserAddress>? = ArrayList<UserAddress>()

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
            getPlaceFromCoordinates(
                viewModelScope,
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

    suspend fun geoCode(location: dev.jordond.compass.Location?): UserAddress? {
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
        println("fetchLocations = ")
        val userKey = authRepository.getUserKey()
        var deferredResults: List<Deferred<Any>> = emptyList()
        if (requestRealLocation) {
            deferredResults = listOf(
                viewModelScope.async {
                    getUserLocations(userKey!!)
                },
                viewModelScope.async {
                    _state.update {
                        _state.value.copy(
                            fetchingDeviceLocation = true
                        )
                    }
                    val result = fetchLocation()
                    val location =
                        geoCode(result.getOrNull()).toReferenceLocation(LocationType.CURRENT_LOCATION)
                    _state.update {
                        _state.value.copy(
                            fetchingDeviceLocation = false
                        )
                    }
                    var toReturn: ArrayList<ReferenceLocation> = ArrayList<ReferenceLocation>()
                    /*
                                        if (location != null) {
                                            location.toReferenceLocation()

                                            toReturn.add(ReferenceLocation(title = location.province.toString(),locationType = Locations.CURRENT_LOCATION ))
                                        }
                                        else
                                        {
                                            toReturn.add(UserAddress(title = "Error de Ubicacion",locationType = Locations.LOCATION_ERROR ))
                                        }
                                        */
                    toReturn
                }
            )
        } else {
            deferredResults = listOf(
                viewModelScope.async {
                    getUserLocations(userKey!!)
                },
            )
        }

        val responseConbined = ArrayList<ReferenceLocation>()
        val results = deferredResults.awaitAll()
        //   val userUserAddresses = (results[0] as ArrayList<UserAddress>)


        val userUserAddresses: ArrayList<ReferenceLocation> = ArrayList<ReferenceLocation>()
        (results[0] as ArrayList<UserAddress>).forEach { it ->
            userUserAddresses.add(it.toReferenceLocation(LocationType.CUSTOM))
        }

        responseConbined.addAll(userUserAddresses)

        if (requestRealLocation && results.size > 1) {
        //    val currentLocation = results[1].get(0)
  // que tipo de dato es
        //          responseConbined.add(currentLocation)
        } else {
            if (permissionsController.isPermissionGranted(Permission.LOCATION) == true == false) {

                val pp =3
           /*
                val enableLocationsServiceOption = UserAddress(
                    title = "Habilitar Servicio de Ubicación",
                    locationType = Locations.ENABLE_LOCATION
                )
                responseConbined.add(enableLocationsServiceOption)
         */
            }
        }
        //   onLocationsListUpdate(responseConbined.toList())



        if (isGpsPresent()) {

            //------
            val realLocationData: ReferenceLocation = if (!isGpsEnabled()) {
                ReferenceLocation(
                    id = (-1).toString(), // ID especial para ubicación actual
                    locationType = LocationType.ENABLE_LOCATION,
                    name = "GPS Apagado",
                    address = "Enciende tu GPS para usar esta opción",
                )
            } else if (permissionsController.isPermissionGranted(Permission.LOCATION)== false) {
                ReferenceLocation(
                    id = (-1).toString(), // ID especial para ubicación actual
                    locationType = LocationType.PERMISSION_REQUIRED,
                    name = "No hay permisos",
                    address = "Otorga permisos para usar esta opción",
                )
            } else {
                ReferenceLocation(
                    id = (-1).toString(), // ID especial para ubicación actual
                    locationType = LocationType.CURRENT_LOCATION,
                    name = "Ubicación actual",
                    address = "Usar mi ubicación actual",
                )
            }

           responseConbined.add(realLocationData)

        }



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

        return getCurrentLocation(permissionsController)
        //      }
    }


    private suspend fun getUserLocation() {

    }

    private suspend fun getUserLocations(userKey: String): List<UserAddress> {
        return userLocationsRepository.list(userKey)
    }

    private fun onLocationsListUpdate(updated: List<UserAddress>) {
        val oldLocations = UserAddresses ?: emptyList()
        val newList =
            updated.filter { it.locationType != Locations.CURRENT_LOCATION }

      /*
        newList.let { it ->
            _knownLocations.value = it.toList()
        }
*/
    }

    private fun updateMyLocation(myUserAddress: UserAddress?) {
        var newResult = ArrayList<UserAddress>()
        if (myUserAddress == null) {
            // Eliminar la ubicación con locationType = CURRENT_LOCATION

            //   newResult = _state.value.locations?.filter { it.locationType != Locations.CURRENT_LOCATION }
            var pepe = 22
        } else {
            // Verificar si existe una ubicación con locationType = CURRENT_LOCATION
            val currentLocationIndex =
                UserAddresses?.indexOfFirst { it.locationType == Locations.CURRENT_LOCATION }
            UserAddresses


            if (currentLocationIndex != null && currentLocationIndex != -1) {
                // Modificar la ubicación existente
                //       _state.value.locations?.set(currentLocationIndex, myLocation)
            } else {
                // Agregar la nueva ubicación
                val location = myUserAddress.copy(locationType = Locations.CURRENT_LOCATION)


/*
                UserAddresses = ArrayList<UserAddress>(UserAddresses!!.toList())
                UserAddresses!!.add(location)
                _knownLocations.value = UserAddresses as ArrayList<UserAddress>
*/
            }
            _state.update {
                _state.value.copy(
                    fetchingDeviceLocation = false
                )
            }
        }


    }


    fun onLocationSelected(UserAddress: UserAddress) {
        val settings: Settings = Settings()
        if (UserAddress.locationType != Locations.ENABLE_LOCATION) {
            settings[Constants.CURRENT_LOCATION] = Json.encodeToString(UserAddress)
            _state.value = _state.value.copy(userAddressSelected = UserAddress)
        } else {
            viewModelScope.launch(Dispatchers.Main) {
                permissionsController.openAppSettings()
            }
        }
    }

    suspend fun getRefLocation(): Pair<Double, Double>? {
        var response: Pair<Double, Double>? = null
        val settings: Settings = Settings()
        val value = settings.getStringOrNull(Constants.CURRENT_LOCATION)
        if (value != null) {
            val location = Json.decodeFromString<UserAddress>(value)
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
        viewModelScope.launch {
            _state.update { it.copy(fetchingDeviceLocation = true) }
            try {
                val result = fetchLocation()
                when (result) {
                    is GeolocatorResult.Success -> {
                        val location = result.data.toLocalLocation()
                        val place = getPlaceFromCoordinates(
                            viewModelScope,
                            location.latitude,
                            location.longitude
                        ) { place ->
                            location.title = place?.locality ?: "Ubicación actual"
                            updateMyLocation(location)
                        }
                    }

                    is GeolocatorResult.Error -> {
                        when (result) {
                            is GeolocatorResult.NotSupported -> showError("Geolocalización no soportada")
                            is GeolocatorResult.NotFound -> showError("No se pudo obtener la ubicación")
                            is GeolocatorResult.PermissionError -> showError("Se requieren permisos de ubicación")
                            is GeolocatorResult.GeolocationFailed -> showError("Error al obtener la ubicación")
                            else -> showError("Error desconocido")
                        }
                    }
                }
            } catch (e: Exception) {
                showError(e.message ?: "Error al obtener la ubicación")
            } finally {
                _state.update { it.copy(fetchingDeviceLocation = false) }
            }
        }
    }

    private fun showError(message: String) {
        _state.update {
            it.copy(
                showErrorMessage = true,
                errorMessage = message
            )
        }
    }

    /*
        fun setPermissionsController(permissionsController: PermissionsController) {
            this.permissionsController.value = permissionsController


            println("HomeScreenViewModel - setPermissionsController - permissionsController = $permissionsController")
            //    fetchData(false)


        }
    */
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
            val response = productsRepository.searchByBarCodeCloud(
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

    fun setPermissionState(state: PermissionState) {
        _state.value = _state.value.copy(

        )
    }


    data class UiState(
        val showKeyboard: Boolean = false,
        val productToShow: ProductOnSearch? = null,
        val productShoppingLists: List<Long> = emptyList(), // Lista de IDs de listas de compras en las que está el producto
        val showPulldownIcon: Boolean = true,
        val shoppingLists: List<ShoppingList>? = null,
        val fetchingDeviceLocation: Boolean = false,
        val userAddressSelected: UserAddress? = null,
        val showErrorMessage: Boolean = false,
        val errorMessage: String? = null,
        val havePermissionState: PermissionState = PermissionState.NotDetermined,
        //   val waitForLocation: Boolean = false
    )

}


fun Place.toLocalLocation(): UserAddress {
    return UserAddress(
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