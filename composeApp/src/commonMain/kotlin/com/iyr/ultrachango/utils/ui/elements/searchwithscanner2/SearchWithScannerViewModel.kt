package com.iyr.ultrachango.utils.ui.elements.searchwithscanner2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.data.database.repositories.ProductsRepository
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ProductOnSearch
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.utils.coroutines.Resource
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class SearchWithScannerViewModel2 : ViewModel(), KoinComponent {


    private val _state = MutableStateFlow(UiState())
    var state: StateFlow<UiState> = _state.asStateFlow()


    private var searchJob: kotlinx.coroutines.Job? = null

    private val _showCameraPreview = MutableStateFlow(false)
    val showCameraPreview: StateFlow<Boolean> = _showCameraPreview.asStateFlow()

    /***
     * Busca articulos por texto en las proximidades a una Lat, Lng
     * @param text
     *
     */
    fun onProductTextInput(text: String, lat: Double = -34.586050, lng: Double = -58.504600) {


        val scope = viewModelScope

        _state.value = _state.value.copy(
            loading = true,
            showKeyboard = false
        )

        searchJob?.let {
            it.cancel()
        }


        searchJob = scope.launch(Dispatchers.IO) {
           /*
            productsRepository.searchByText(text, lat, lng)
                .onStart {
                    // Emitir estado de carga
                    _state.value = _state.value.copy(
                        loading = true,
                        showKeyboard = false
                    )
                }
                .catch { exception ->
                    // Manejar errores
                    val error = exception
                    _state.value = _state.value.copy(
                        loading = false,
                        showKeyboard = false
                    )
                }
                .collect { resource ->
                    // Actualizar el estado con los resultados de búsqueda

                    when (resource) {
                        is Resource.Success -> {
                            _state.value = _state.value.copy(
                                loading = false,
                                showPulldownIcon = true,
                                searchResultsExpanded = true,

                                )
                            println("Emito searchResults")

                           _state.value = _state.value.copy(
                               searchResults = resource.data ?: emptyList(),
                               searchResultsExpanded = true
                           )
                         //   _searchResults.value = resource.data ?: emptyList()
                            //_state.emit(newUiState)
                        }

                        is Resource.Error -> {
                            _state.value = _state.value.copy(
                                loading = false,
                                showErrorMessage = false,
                                errorMessage = resource.message
                            )

                        }

                        else -> {
                            null
                        }
                    }

                }

            */
        }
    }


    fun onFocusChanged(hasFocus: Boolean) {

        _state.value = _state.value.copy(
            showKeyboard = hasFocus
        )
    }

    fun onEmptySeachText() {
        _state.value = _state.value.copy(
            searchResults = emptyList<Product>(),
            showPulldownIcon = false,
            searchResultsExpanded = false
        )

    }

    fun onScanPressed() {
        val scope = viewModelScope

        scope.launch {
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
    }

    fun onPulldownStatusInvert() {
        val currentUiState = state.value
//        searchResults = currentUiState.searchResults,

        _state.value = _state.value.copy(

            showPulldownIcon = !currentUiState.showPulldownIcon,
            searchResultsExpanded = !currentUiState.searchResultsExpanded
        )
    }

    fun onShowProductRequest(product: ProductOnSearch) {
        _state.value = _state.value.copy(productToShow = product.toProduct())
    }

    fun onProductAlreadyShown() {
        _state.value = _state.value.copy(productToShow = null)


    }

    fun onProductAddRequest(product: Product) {
        _state.value = _state.value.copy(
            showAddProductDialog = true,
            product = product
        )
    }
}


data class UiState(
    val loading: Boolean = false,
    val showKeyboard: Boolean = false,
    val showPulldownIcon: Boolean = false,
    val searchResults: List<Product> = emptyList(),
    val searchResultsExpanded: Boolean = false,
    val productToShow: Product? = null,
    val shoppingLists: List<ShoppingList>? = null,
    val showErrorMessage: Boolean = false,
    val errorMessage: String? = null,
    val showAddProductDialog: Boolean = false,
    val product: Product? = null
)