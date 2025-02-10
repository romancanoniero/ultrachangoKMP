package com.iyr.ultrachango.utils.ui.elements.searchwithscanner2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.iyr.ultrachango.ui.screens.home.MIN_THRESHOLD_SEARCH
import com.iyr.ultrachango.ui.theme.frameBorder
import com.iyr.ultrachango.utils.extensions.isDigitsOnly
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import org.jetbrains.compose.resources.vectorResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.barcode_scanner

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchTextFieldWithScanner2(
    text: String = "",
    onTextChanged: (String) -> Unit,
    onScannerButtonPressed: () -> Unit, // maneja cuando el boton de scaneo se presiona
    onScan: () -> Unit, // maneja cuando el boton de scaneo se presiona
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean, FocusRequester) -> Unit,
    onResultsUpdated: (List<Any>) -> Unit
) {

    val   vm: SearchWithScannerViewModel2 = SearchWithScannerViewModel2()
    var showScannerIcon by remember { mutableStateOf(true) }

    //--------------------
    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    //  var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    var searchText by remember { mutableStateOf("") }

    var searchResultsExpanded by remember { mutableStateOf(false) }
    var searchResultsSize by remember { mutableStateOf(0) }


    val searchTextFlow = remember { MutableStateFlow("") }
    LaunchedEffect(searchTextFlow) {
        searchTextFlow
            .debounce(1000) // Espera 2 segundos desde la última pulsación
            .collectLatest { text ->
                if (text.length >= MIN_THRESHOLD_SEARCH) {
                    if (text.length>=4 && text.isDigitsOnly()) {
                        val pepe = 3
                        //   vm.onBarcodeScanned(text)
                    }
                        else {
                        vm.onProductTextInput(text)
                    }
                }
            }
    }

    ExposedDropdownMenuBox(
        expanded = searchResultsExpanded && searchResultsSize > 1,
        onExpandedChange = {
            vm.onPulldownStatusInvert()
        }
    ) {

        TextField(
            value = text,
            placeholder = { Text("Nombre del producto") },
            onValueChange = {
//                onTextChanged(it)
                searchText =it
                searchTextFlow.value = it
                showScannerIcon = it.isEmpty()
            },

            modifier = Modifier.fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                .border(1.dp, frameBorder, shape = RoundedCornerShape(12.dp))
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    onFocusChanged(focusState.hasFocus, focusRequester)
                },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                vm.onProductTextInput(text)
            }),

            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Buscar",
                    tint = Color.LightGray
                )

            },
            trailingIcon = {
                if (searchResultsSize > 1) {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = searchResultsExpanded
                    )
                } else {
                    if (showScannerIcon) {
                        IconButton(
                            onClick = onScannerButtonPressed
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.barcode_scanner),
                                contentDescription = "Escáner",
                                tint = Color.LightGray
                            )
                        }
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,

                unfocusedContainerColor = Color.Transparent,
                unfocusedPlaceholderColor = Color.Blue,
                focusedPlaceholderColor = Color.Red,
                focusedContainerColor = Color.White

            )
        )
        ExposedDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = searchResultsExpanded && searchResultsSize > 1,
            onDismissRequest = {
                vm.onPulldownStatusInvert()
            }
        ) {
            var pp = 33
            // Mandar a mostrar los resultados
            /*

            vm.state.searchResults.forEach { product ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = product.name.toString()

                        vm.onShowProductRequest(product.toProductOnSearch())
                        //    vm.onPulldownCloseRequest()

                    }
                ) {
                    DropdownItemProductSearch(product = product)
                }
            }

             */
        }
    }

}