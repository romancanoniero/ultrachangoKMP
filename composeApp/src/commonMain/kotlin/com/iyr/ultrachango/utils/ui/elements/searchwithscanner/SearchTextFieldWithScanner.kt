@file:OptIn(FlowPreview::class)

package com.iyr.ultrachango.utils.ui.elements.searchwithscanner

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ProductOnSearch
import com.iyr.ultrachango.ui.screens.home.MIN_THRESHOLD_SEARCH
import com.iyr.ultrachango.utils.ui.elements.MySearchTexField
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.barcode_scanner

@Composable
fun SearchWithScanner(
    modifier: Modifier = Modifier,
    vm: SearchWithScannerViewModel = koinViewModel(),
    onImageClicked: (Product) -> Unit = {},
    onAddButtonClicked: (Product) -> Unit = {},
    existingEANs: List<String>? = emptyList(),
    onExistingIcon: ImageVector? = null,
    onNonExistingIcon: ImageVector? = null,
    onScannerClick: () -> Unit,
    lifeCycleScope: CoroutineScope,
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val _showCameraPreview = MutableStateFlow(false)
    val showCameraPreview: StateFlow<Boolean> = _showCameraPreview.asStateFlow()
    val searchTextFlow = remember { MutableStateFlow("") }
    val state by vm.state.collectAsState()

    LaunchedEffect(searchTextFlow) {
        searchTextFlow
            .debounce(1000) // Espera 2 segundos desde la última pulsación
            .collectLatest { text ->
                if (text.length >= MIN_THRESHOLD_SEARCH) {
                    vm.onProductTextInput(text)
                }
            }

    }

    SearchTextFieldWithScanner(
        modifier = modifier,
        text = searchText,
        onTextChange = { it ->
            val newText = it
            if (!searchText.text.equals(it.toString())) {
                searchText = it
                if (!searchText.text.isEmpty() && searchText.text.length > MIN_THRESHOLD_SEARCH) {
                    searchTextFlow.value = searchText.text
                } else {
                    vm.onEmptySeachText()
                }
            }
        },
        onImageClicked = {
            onImageClicked(it)
        },
        onAddButtonClicked = {
            vm.onProductAddRequest(it)
            onAddButtonClicked(it)
        },
        onItemSelected = {
            it.let { item ->
                searchText = TextFieldValue(item.name ?: "")
                vm.onShowProductRequest(it)
            }
        },
        onScannerClick = {
            vm.onScanPressed()
        },
        existingEANs = existingEANs,
        onExistingIcon = onExistingIcon,
        onNonExistingIcon = onNonExistingIcon,
        focusRequester = focusRequester,
        onFocusChanged = { hasFocus, focusRequester ->
            //     vm.onFocusChanged(hasFocus)
        },
        lifeCycleScope = lifeCycleScope

    )
}

const val ALREADY_EXISTS = "ALREADY_EXISTS"
const val NON_EXISTING = "NON_EXISTING"

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchTextFieldWithScanner(
    modifier: Modifier = Modifier,
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    existingEANs: List<String>? = emptyList(),
    onExistingIcon: ImageVector? = null,
    onNonExistingIcon: ImageVector? = null,
    vm: SearchWithScannerViewModel = koinViewModel(),
    focusRequester: FocusRequester?,
    onFocusChanged: (Boolean, FocusRequester) -> Unit,
    onImageClicked: (Product) -> Unit = {},
    onAddButtonClicked: (Product) -> Unit = {},
    onScannerClick: () -> Unit,
    onItemSelected: (ProductOnSearch) -> Unit,
    lifeCycleScope: CoroutineScope,
    // state: HomeScreenViewModel.UiState,

) {


    var showScannerIcon by remember { mutableStateOf(true) }

//    var state by remember { mutableStateOf(UiState()) }

    val state by vm.state.collectAsState()



  //  val searchResults by  remember {  mutableStateOf(arrayListOf<Product>()) }
/*
    lifeCycleScope.launch {
        vm.searchResults.collect{ results ->

            if (results.isNotEmpty())
                searchResults.addAll(results)
        }
    }
*/




    ExposedDropdownMenuBox(
        expanded = state.searchResultsExpanded && state.searchResults.size > 1,
        onExpandedChange = {
            vm.onPulldownStatusInvert()
        }
    ) {

        MySearchTexField(

            value = text,
            placeHolderText = "Nombre del producto",
            onValueChange = {
                val text = it.text
                onTextChange(it)
                showScannerIcon = text.isEmpty()
            },
            modifier = modifier.fillMaxWidth()
                .onFocusChanged { focusState ->
                    //          onFocusChanged(focusState.hasFocus, focusRequester)
                },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                vm.onProductTextInput(text.text)
            }),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Buscar",
                    tint = Color.LightGray
                )
            },
            trailingIcon = {
                Row()
                {
                    if (text.text.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                triggerHapticFeedback()
                                onTextChange(TextFieldValue(""))
                            }
                        ) {
                            Icon(
                                Icons.Filled.Cancel,
                                contentDescription = "Limpiar",
                                tint = Color.LightGray
                            )
                        }
                    }


                    if (state.searchResults.size > 1) {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = state.searchResultsExpanded
                        )
                    } else {
                        if (showScannerIcon) {
                            IconButton(
                                onClick = onScannerClick
                            ) {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.barcode_scanner),
                                    contentDescription = "Escáner",
                                    tint = Color.LightGray
                                )
                            }
                        }
                    }


                }
            },
        )

        ExposedDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = state.searchResultsExpanded && state.searchResults.size > 1,
            onDismissRequest = {
                vm.onPulldownStatusInvert()
            }
        ) {

            if (state.searchResults.size > 0) {
                val pepe = 33
            }

            val mappedList = state.searchResults.map { product -> product.toProductOnSearch() }
            mappedList.forEach { product ->
                product.status =
                    if (existingEANs?.contains(product.ean) == true) ALREADY_EXISTS else NON_EXISTING
            }

            mappedList.forEach { product ->
                DropdownMenuItem(
                    onClick = {
                        triggerHapticFeedback()
                        onItemSelected(product)
                    }
                ) {
                    DropdownItemProductSearch(
                        product = product,
                        showAddButton = true,
                        onImageClicked = {
                            onImageClicked(it)
                        },
                        onAddButtonClicked = {
                            onAddButtonClicked(it)
                        },
                        onExistingIcon = onExistingIcon,
                        onNonExistingIcon = onNonExistingIcon,
                    )
                }
            }


        }


    }


}


