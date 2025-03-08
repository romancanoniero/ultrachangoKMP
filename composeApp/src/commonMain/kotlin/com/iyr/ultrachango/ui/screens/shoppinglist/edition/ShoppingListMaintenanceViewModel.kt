package com.iyr.ultrachango.ui.screens.shoppinglist.edition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.iyr.ultrachango.data.database.repositories.ProductsRepository
import com.iyr.ultrachango.data.database.repositories.ShoppingListRepository
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ProductOnSearch
import com.iyr.ultrachango.data.models.ShoppingListComplete
import com.iyr.ultrachango.data.models.ShoppingListMemberComplete
import com.iyr.ultrachango.data.models.ShoppingListProductComplete
import com.iyr.ultrachango.data.models.ShoppingListQuantities
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.utils.auth_by_cursor.repository.AuthRepository
import com.iyr.ultrachango.utils.coroutines.Resource
import com.iyr.ultrachango.utils.ui.elements.searchwithscanner.ALREADY_EXISTS
import com.iyr.ultrachango.utils.ui.elements.searchwithscanner.NON_EXISTING
import com.iyr.ultrachango.viewmodels.UserViewModel
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent


class ShoppingListAddEditViewModel(
    private val userKey: String,
    private val authRepository: AuthRepository,
    private val shoppingListId: Int,
    private val productsRepository: ProductsRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val userViewModel: UserViewModel,
    private val scaffoldVM: ScaffoldViewModel,
) : ViewModel(), KoinComponent {



    var permissionsController: PermissionsController? = null


    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()


    private var shoppingList: ShoppingListComplete? = null/*
     var scaffoldStateUI by mutableStateOf(ScaffoldViewModel.UiState())
         private set
*/

    private var searchJob: kotlinx.coroutines.Job? = null


    private val _showCameraPreview = MutableStateFlow(false)
    val showCameraPreview: StateFlow<Boolean> = _showCameraPreview.asStateFlow()

    init {
        fetchData()
    }

    fun assignPermissionsController(permissionsController: PermissionsController) {
        this.permissionsController = permissionsController
    }


    fun fetchData() {
        scaffoldVM.showLoader(true)
        //  scaffoldStateUI.showLoader= true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (shoppingListId.toString().isNotEmpty()) {
                    shoppingList = shoppingListRepository.getShoppingList(shoppingListId)
                    scaffoldVM.showLoader(false)

                    var userKey = authRepository.getUserKey()
                    _state.value = _state.value.copy(
                        listId = shoppingList?.listId ?: 0,
                        name = shoppingList?.listName.toString(),
                        itemsList = prepareItems(userKey!!, shoppingList!!),
                        membersList = shoppingList?.members ?: emptyList()
                    )
                }

            } catch (e: Exception) {

                scaffoldVM.showLoader(false)

                _state.value = _state.value.copy(
                    listId = shoppingList?.listId ?: 0,
                    name = shoppingList?.listName.toString(),
                    itemsList = shoppingList?.items ?: emptyList()
                )
            }
        }
    }


    val buttonSaveEnabled = MutableStateFlow<Boolean>(false)
    val data = MutableStateFlow<ShoppingListComplete?>(shoppingList)


    fun resetGoBack() {
        _state.value = _state.value.copy(goBack = false)
    }

    fun onProductAdded(product: Product, userKey: String) {
        val listId = _state.value.listId
        val ean = product.ean
        viewModelScope.launch(Dispatchers.IO) {
            closeDialogsRequested()
            shoppingListRepository.addProductToList(listId, ean, 1)
            fetchData()
        }
    }


    fun closeErrorDialogRequest() {

        _state.value = _state.value.copy(
            showErrorMessage = false
        )
    }

    fun updateProductCounter(listId: Int, ean: String, userId: String, value: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                shoppingListRepository.updateProductCounter(listId, ean, userId, value)
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
    fun onProductTextInput(text: String, lat: Double = -34.586050, lng: Double = -58.504600) {
        scaffoldVM.showLoader(true)
        _state.value = _state.value.copy(
            showKeyboard = false,
            loadingProducts = true,

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

                        val records = resource.data?.map { it -> it.toProductOnSearch() }


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
                        scaffoldVM.showLoader(false)

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
                val response = productsRepository.searchByBarCodeByLatLngCloud(
                    barcode, -34.586050, -58.504600
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
    //-------------
}


data class UiState(
    val showKeyboard: Boolean = false,
    val loading: Boolean = false,
    val buttonSaveEnabled: Boolean = false,
    val goBack: Boolean = false,
    val name: String = "",
    val userId: String? = null,
    val listId: Int = 0,
    val itemsList: List<ShoppingListProductComplete> = emptyList(),
    val membersList: List<ShoppingListMemberComplete> = emptyList(),
    val showErrorMessage: Boolean = false,
    val errorMessage: String? = null,
    val showAddConfirmationDialog: Boolean = false,
    val productReference: Product? = null,
    val showPulldownIcon: Boolean = false,
    val searchResultsExpanded: Boolean = false,
    val loadingProducts: Boolean = false,
    val searchResults: List<ProductOnSearch> = emptyList(),
)


private fun prepareItems(
    myUserId: String, shoppingList: ShoppingListComplete
): List<ShoppingListProductComplete> {
    var auxList = completeMembers(myUserId, shoppingList).sortedBy { it.product?.name }

    return auxList
}

fun completeMembers(
    myUserId: String, shoppingList: ShoppingListComplete
): List<ShoppingListProductComplete> {
    val members = shoppingList.members
    val creatorId = shoppingList.userId
    shoppingList.items?.forEach { item ->
        val quantities = item.quantities?.toMutableList()

        // Añadir registros faltantes en quantities para cada miembro
        members?.forEach { member ->
            if (quantities?.none { it.userId == member.userId } == true) {
                quantities.add(
                    ShoppingListQuantities(
                        listId = shoppingList.listId ?: 0,
                        ean = item.product?.ean ?: "",
                        userId = member.userId,
                        qty = 0.0
                    )
                )
            }
        }

        // Ordenar quantities: primero el creador de la lista, luego los demás
        item.quantities = quantities?.sortedWith(
            compareBy({ it.userId != creatorId },
                { it.userId != myUserId },
                { it.userId })
        )
    }


    return shoppingList.items ?: emptyList()
}

