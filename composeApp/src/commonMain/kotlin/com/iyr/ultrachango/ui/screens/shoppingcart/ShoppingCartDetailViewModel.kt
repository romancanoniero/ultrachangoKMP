package com.iyr.ultrachango.ui.screens.shoppingcart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.data.database.repositories.ShoppingCartRepository


import com.iyr.ultrachango.data.database.repositories.ProductsRepository
import com.iyr.ultrachango.data.database.repositories.ShoppingListRepository
import com.iyr.ultrachango.data.models.ShoppingCartProduct
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ProductOnSearch
import com.iyr.ultrachango.data.models.ShoppingCart
import com.iyr.ultrachango.data.models.ShoppingListComplete
import com.iyr.ultrachango.data.models.ShoppingListProductComplete
import com.iyr.ultrachango.data.models.ShoppingListQuantities
import com.iyr.ultrachango.data.models.toProductOnSearch
import com.iyr.ultrachango.domain.auth.AuthRepository
import com.iyr.ultrachango.ui.ScaffoldViewModel

import com.iyr.ultrachango.utils.coroutines.Resource
import com.iyr.ultrachango.utils.geo.getCurrentLocation
import com.iyr.ultrachango.utils.ui.elements.searchwithscanner.ALREADY_EXISTS
import com.iyr.ultrachango.utils.ui.elements.searchwithscanner.GENERIC_PRODUCT
import com.iyr.ultrachango.utils.ui.elements.searchwithscanner.NON_EXISTING
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
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
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent


class ShoppingCartMaintenanceViewModel(
    private val authRepository: AuthRepository,
    private val productsRepository: ProductsRepository,
    private val shoppingCartRepository: ShoppingCartRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val permissionsController: PermissionsController,
    private val scaffoldVM: ScaffoldViewModel,
) : ViewModel(), KoinComponent {


    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private var shoppingList: ShoppingListComplete? = null

    private var searchJob: kotlinx.coroutines.Job? = null

    private val _showCameraPreview = MutableStateFlow(false)
    val showCameraPreview: StateFlow<Boolean> = _showCameraPreview.asStateFlow()

    init {
        fetchData()
    }

    fun assignPermissionsController(permissionsController: PermissionsController) {
        //    this.permissionsController = permissionsController
    }

    fun fetchData(
    ) {
        _state.update {
            _state.value.copy(
                loading = true
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            var shoppingLists: List<ShoppingListComplete>? = null
            var shoppingCart: ShoppingCart? = null
            try {
                //              if (preparationListId.toString().isNotEmpty()) {
                val deferredResults = listOf(

                    viewModelScope.async
                    {
                        shoppingCart = shoppingCartRepository.getOrCreateShoppingCart()
                        shoppingCart
                    },
                    viewModelScope.async
                    {
                        shoppingLists = shoppingListRepository.list()
                    },
                )
                //    deferredResults.awaitAll()
                val results = deferredResults.awaitAll()

                _state.update {
                    _state.value.copy(
                        loading = false,
                        shoppingCart = shoppingCart,
                        shoppingList = shoppingLists ?: emptyList()
                    )
                }


//                }

            } catch (e: Exception) {
                val pp = e
                scaffoldVM.showLoader(false)
                /*
                                _state.value = _state.value.copy(
                                    listId = shoppingList?.listId ?: 0,
                                    name = shoppingList?.listName.toString(),
                                    itemsList = sampleShoppingCart.get(0).items
                                        ?: emptyList()//shoppingList?.items ?: emptyList()
                                )
                                */
            }
        }
    }


    val buttonSaveEnabled = MutableStateFlow<Boolean>(false)
    val data = MutableStateFlow<ShoppingCart?>(ShoppingCart())

    fun getUserKey(): String {
        return authRepository.getUserKey().toString()
    }


    fun resetGoBack() {
        _state.value = _state.value.copy(goBack = false)
    }

    fun onProductAdded(product: Product, userKey: String) {
        val listId = _state.value.shoppingCart?.shoppingCartId ?: -1
        val ean = product.ean
        viewModelScope.launch(Dispatchers.IO) {
            closeDialogsRequested()
            shoppingCartRepository.addProductToList(listId, ean!!, 1.0)
            fetchData()
        }
    }


    fun closeErrorDialogRequest() {

        _state.value = _state.value.copy(
            showErrorMessage = false
        )
    }

    fun updateProductCounter(ean: String, value: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var listId = _state.value.shoppingCart?.shoppingCartId!!
                var userId = _state.value.shoppingCart?.userKey.toString()
                shoppingCartRepository.updateProductCounter(listId, ean, userId, value)
                fetchData()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _state.value = _state.value.copy(
                        showErrorMessage = true, errorMessage = e.message
                    )

                }
            }

        }
    }

    fun onAddProductAsk(product: Product) {
        val userKey = authRepository.getUserKey().toString()
        _state.value = _state.value.copy(
            showAddConfirmationDialog = true, productReference = product, userId = userKey
        )
    }


    fun closeDialogsRequested() {
        _state.value = _state.value.copy(
            showAddConfirmationDialog = false,
            showErrorMessage = false,
            productReference = null,
            userId = null
        )
    }


    //------------------

    /***
     * Busca articulos por texto en las proximidades a una Lat, Lng
     * @param text
     *
     */
    //TODO: Cambiar el nombre de la funcion a onSearchByText y simplificar
    fun onProductTextInput(
        text: String,
        includeFreeText: Boolean = false
    ) {
        _state.value = _state.value.copy(
            showKeyboard = false,
            loadingProducts = true,
        )
        searchJob?.let {
            it.cancel()
        }
        searchJob = viewModelScope.launch(Dispatchers.IO) {

            val location = getCurrentLocation(permissionsController)
            location.getOrNull()?.let { location ->
                val coordinates = location.coordinates

                productsRepository.searchByText(text, coordinates.latitude, coordinates.longitude)
                    .onStart {
                        // Emitir estado de carga
                    }.catch { exception ->
                        // Manejar errores
                        val error = exception
                        _state.value = _state.value.copy(
                            loadingProducts = false,
                            errorMessage = error.message.toString(),
                            showErrorMessage = true
                        )

                    }.collect { resource ->
                        // Actualizar el estado con los resultados de búsqueda
                        when (resource) {
                            is Resource.Success -> {
                                var records = resource.data?.map { it -> it.toProductOnSearch() }
                                if (includeFreeText) {
                                    records = listOf(
                                        ProductOnSearch(
                                            ean = text,
                                            name = text,
                                            brand = "",
                                            status = GENERIC_PRODUCT
                                        )
                                    ) + (records ?: emptyList<ProductOnSearch>())
                                }

                                val mappedList = records?.map { product ->
                                    product.status =
                                        if (shoppingList?.items?.any { it.product?.ean == product.ean } == true) ALREADY_EXISTS else NON_EXISTING
                                    product
                                } ?: emptyList()

                                _state.value = _state.value.copy(
                                    showPulldownIcon = true,
                                    searchResultsExpanded = true,
                                    searchResults = mappedList,
                                    loadingProducts = false
                                )
                            }

                            is Resource.Error -> {
                                _state.value = _state.value.copy(
                                    showErrorMessage = true, errorMessage = resource.message
                                )
                            }
                            else -> {
                                null
                            }
                        }

                    }


            }

        }

    }


    fun hideScanner() {
        _showCameraPreview.value = false
    }


    fun onPulldownStatusInvert() {
        val currentUiState = state
        _state.value = _state.value.copy(
            searchResults = currentUiState.value.searchResults,
            showPulldownIcon = !currentUiState.value.showPulldownIcon,
            searchResultsExpanded = !currentUiState.value.searchResultsExpanded
        )
    }


    fun onScanPressed() {
        viewModelScope.launch(Dispatchers.IO) {

            permissionsController?.providePermission(Permission.CAMERA)

            if (permissionsController?.isPermissionGranted(Permission.CAMERA) == true) {
                try {
                    _showCameraPreview.value = true
                } catch (deniedAlways: DeniedAlwaysException) {
                    // Permission is always denied.
                    var pp = deniedAlways
                } catch (denied: DeniedException) {
                    // Permission was denied.
                    var pp = denied
                }
            } else {
                permissionsController?.getPermissionState(Permission.CAMERA)

            }
        }
    }

    fun onBarcodeScanned(barcode: String) {
        val userId = authRepository.getUserKey().toString()

        //       val userId = userViewModel.user.value?.id.toString()
// TODO
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = productsRepository.searchByBarCodeCloud(
                    barcode
                )
                val productToShow = response.get("product") as Product
                val listWhereProductIs = response.get("shoppingLists") as List<Long>
                onAddProductAsk(productToShow)

            } catch (e: Exception) {


                _state.value = _state.value.copy(
                    showErrorMessage = true, errorMessage = e.message
                )
            }

        }

    }

    fun onFocusChanged(hasFocus: Boolean) {
        _state.value = _state.value.copy(showKeyboard = hasFocus)
    }

    fun onEmptySeachText() {
        _state.value = _state.value.copy(
            searchResults = arrayListOf(), showPulldownIcon = false, searchResultsExpanded = false
        )
    }

    fun closeDropDown() {
        _state.value = _state.value.copy(
            showPulldownIcon = false, searchResultsExpanded = false
        )
    }

    fun onIncludeList(list: ShoppingListComplete) {

        var shoppingCart = _state.value.shoppingCart

        var updatedShoppingCart = shoppingCart?.includeFromList(list)
        viewModelScope.launch(Dispatchers.IO) {
            shoppingCartRepository.update(shoppingCart?.prepateToSave()!!)
        }
        _state.update {
            _state.value.copy(
                forcedRefreshTime = Clock.System.now().toEpochMilliseconds(),
                loading = false,
                shoppingCart = updatedShoppingCart
            )
        }
    }

    fun onExcludeList(listToRemove: ShoppingListComplete) {


        var shoppingCart = _state.value.shoppingCart // referencia al shoppingCart

        var shoppingLists = _state.value.shoppingList

        _state.update {
            _state.value.copy(
                forcedRefreshTime = Clock.System.now().toEpochMilliseconds(),
                loading = false,
                shoppingCart = shoppingCart?.removeFromList(listToRemove, shoppingLists)
            )
        }


    }

    fun deleteAllItems() {
        _state.update {
            _state.value.copy(
                forcedRefreshTime = Clock.System.now().toEpochMilliseconds(),
                loading = true,
                showEmptyConfirmationDialog = true,
                showErrorMessage = false
            )
        }
    }

    fun onCloseDialogsRequested() {
        _state.value = _state.value.copy(
            showEmptyConfirmationDialog = false,
            showErrorMessage = false,
        )
    }

    fun onEmptyCartConfirmed() {
        _state.value = _state.value.copy(
            showEmptyConfirmationDialog = false,
            loading = false,
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                shoppingCartRepository.clear()
                fetchData()
                _state.value = _state.value.copy(
                    loading = false,
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    showErrorMessage = true, errorMessage = e.message
                )
            }
        }
    }

    fun onDeleteProductClicked(product: ShoppingCartProduct) {

        val listId = _state.value.shoppingCart?.shoppingCartId ?: -1
        val ean = product.ean


        val itemsUpdated = ArrayList<ShoppingCartProduct>()
        _state.value.shoppingCart?.items?.filter { it.ean != ean }?.forEach {
            itemsUpdated.add(it)
        }


        val shoppingCartUpdated = _state.value.shoppingCart?.copy(
            items = itemsUpdated,
        )
        _state.value = _state.value.copy(
            shoppingCart = shoppingCartUpdated,
        )
        /*
          viewModelScope.launch(Dispatchers.IO) {
              closeDialogsRequested()
              shoppingCartRepository.addProductToList(listId, ean!!, 1.0)
              fetchData()
          }
          */


    }
    //-------------
}


data class UiState(
    val forcedRefreshTime: Long = 0,
    val showKeyboard: Boolean = false,
    val loading: Boolean = false,
    val buttonSaveEnabled: Boolean = false,
    val goBack: Boolean = false,
    val name: String = "",
    val userId: String? = null,
    val itemsList: List<ShoppingCartProduct> = emptyList(),
    val shoppingList: List<ShoppingListComplete> = emptyList(),
    val shoppingCart: ShoppingCart? = ShoppingCart(),
    val selectedListsIds: List<Int> = emptyList(),
    val showErrorMessage: Boolean = false,
    val errorMessage: String? = null,
    val showAddConfirmationDialog: Boolean = false,
    val productReference: Product? = null,
    val showPulldownIcon: Boolean = false,
    val searchResultsExpanded: Boolean = false,
    val loadingProducts: Boolean = false,
    val searchResults: List<ProductOnSearch> = emptyList(),
    val showEmptyConfirmationDialog: Boolean = false,
)


private fun prepareItems(
    myUserId: String, preparationList: ShoppingListComplete
): List<ShoppingListProductComplete> {
    // var auxList = completeMembers(myUserId, shoppingList).sortedBy { it.product?.name }

    return emptyList() // auxList
}

fun completeMembers(
    myUserId: String, shoppingList: ShoppingListComplete
): List<ShoppingListProductComplete> {
    val members = shoppingList.members
    val creatorId = shoppingList.userKey
    shoppingList.items?.forEach { item ->
        val quantities = item.quantities?.toMutableList()

        // Añadir registros faltantes en quantities para cada miembro
        members?.forEach { member ->
            if (quantities?.none { it.userKey == member.userKey } == true) {
                quantities.add(
                    ShoppingListQuantities(
                        listId = shoppingList.listId ?: 0,
                        ean = item.product?.ean ?: "",
                        userKey = member.userKey,
                        qty = 0.0
                    )
                )
            }
        }

        // Ordenar quantities: primero el creador de la lista, luego los demás
        item.quantities = quantities?.sortedWith(
            compareBy(
                { it.userKey != creatorId },
                { it.userKey != myUserId },
                { it.userKey })
        )
    }


    return shoppingList.items ?: emptyList()
}

