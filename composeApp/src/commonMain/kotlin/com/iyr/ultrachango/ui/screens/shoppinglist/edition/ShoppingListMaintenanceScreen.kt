package com.iyr.ultrachango.ui.screens.shoppinglist.edition

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.iyr.ultrachango.Constants.PRODUCT_DOES_NOT_EXIST
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ProductOnSearch
import com.iyr.ultrachango.data.models.ShoppingListMemberComplete
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.ui.dialogs.AddProductConfirmationDialog
import com.iyr.ultrachango.ui.dialogs.ErrorDialog
import com.iyr.ultrachango.ui.rootnavigation.RootRoutes
import com.iyr.ultrachango.ui.screens.home.MIN_THRESHOLD_SEARCH
import com.iyr.ultrachango.utils.extensions.isDigitsOnly
import com.iyr.ultrachango.utils.helpers.getProductImageUrl
import com.iyr.ultrachango.utils.helpers.getProfileImageURL
import com.iyr.ultrachango.utils.sound.AudioPlayer
import com.iyr.ultrachango.utils.ui.ShowKeyboard
import com.iyr.ultrachango.utils.ui.UserImage
import com.iyr.ultrachango.utils.ui.elements.ReusableSearchTextField
import com.iyr.ultrachango.utils.ui.elements.StyleLight
import com.iyr.ultrachango.utils.ui.elements.searchwithscanner.ALREADY_EXISTS
import com.iyr.ultrachango.utils.ui.elements.searchwithscanner.NON_EXISTING
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import com.iyr.ultrachango.viewmodels.UserViewModel
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.ncgroup.kscan.BarcodeFormats
import org.ncgroup.kscan.BarcodeResult
import org.ncgroup.kscan.ScannerView
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.product_not_found
import ultrachango2.composeapp.generated.resources.sin_imagen


@Composable
fun ShoppingListAddEditScreen(
    userKey: String,
    listId: Int?,
    listName: String?,
    navController: NavHostController,
    permissionsController: PermissionsController,
    vm: ShoppingListAddEditViewModel = koinViewModel(),
    scaffoldVM: ScaffoldViewModel,
    onGroupButtonClicked: (listId: Long) -> Unit = {},
    userViewModel: UserViewModel = koinViewModel()
) {


    vm.assignPermissionsController(permissionsController)
    val state by vm.state.collectAsState()

    val showCameraPreview by vm.showCameraPreview.collectAsState()

    var searchText by remember { mutableStateOf("") }
    val searchTextFlow = remember { MutableStateFlow("") }
    val focusRequester = remember { FocusRequester() }


    fun onIncButtonPressed(

        listId: Int, ean: String, userId: String, value: Double
    ) {
        vm.updateProductCounter(listId, ean, userId, value)
    }

    fun onDecButtonPressed(listId: Int, ean: String, userId: String, value: Double) {
        vm.updateProductCounter(listId, ean, userId, value)
    }




    LaunchedEffect(Unit) {
        vm.fetchData()
    }


    if (state.showErrorMessage) {
        var errorMessage = state.errorMessage.toString()
        when (errorMessage) {
            PRODUCT_DOES_NOT_EXIST ->
                errorMessage = stringResource(Res.string.product_not_found)
        }
        ErrorDialog(title = "Error", message = errorMessage, onDismissRequest = {
            vm.closeErrorDialogRequest()
        })
    }



    if (state.showAddConfirmationDialog) {
        AddProductConfirmationDialog(
            userId = state.userId ?: "",
            product = state.productReference!!,
            onAddButtonClicked = { product, userId ->
                vm.onProductAdded(product, userId)
            },
            onDismissRequest = {
                vm.closeDialogsRequested()
            },
        )
    }

    scaffoldVM.setTitle(state.name.uppercase())
    scaffoldVM.setButtons {
        IconButton(onClick = {
            triggerHapticFeedback()
            val route = RootRoutes.ShoppingListMembersRoute.createRoute(listId ?: 0)
            navController.navigate(route)
        }) {
            Icon(Icons.Default.Group, contentDescription = "Members")
        }
    }


    if (state.goBack) {
        navController.popBackStack()
        vm.resetGoBack()
    }

    if (showCameraPreview) {
        ScannerView(
            codeTypes = listOf(
                BarcodeFormats.FORMAT_ALL_FORMATS
            )
        ) { result ->
            when (result) {
                is BarcodeResult.OnSuccess -> {
                    println("Barcode: ${result.barcode.data}, format: ${result.barcode.format}")
                    AudioPlayer.getInstance()
                        .playSound(0) // Assuming 0 is the id for "files/scanner.mp3"

                    vm.hideScanner()
                    vm.onBarcodeScanned(result.barcode.data)
                }

                is BarcodeResult.OnFailed -> {
                    println("Error: ${result.exception.message}")
                    vm.hideScanner()
                }

                BarcodeResult.OnCanceled -> {
                    vm.hideScanner()
                }
            }

        }

    } else {

        val searchTextFlow = remember { MutableStateFlow("") }

        if (state.loadingProducts) {
            focusRequester.freeFocus()
            ShowKeyboard(false)
        }

        LaunchedEffect(searchTextFlow) {
            searchTextFlow
                .debounce(1000) // Espera 2 segundos desde la última pulsación
                .collectLatest { text ->
                    if (text.length >= MIN_THRESHOLD_SEARCH) {
                        if (text.length >= 4 && text.isDigitsOnly())
                            vm.onBarcodeScanned(text)
                        else
                            vm.onProductTextInput(text)
                    }
                }
        }


        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {


            UsersSection(
                modifier = Modifier.fillMaxWidth(),
                usersList = state.membersList,
            )

            Spacer(modifier = Modifier.height(10.dp))

            SearchTextFieldWithScanner(
                userKey = userViewModel.user.value?.uid,
                text = searchText,
                loadingProducts = state.loadingProducts,
                onTextChange = {
                    searchText = it
                    if (!searchText.isEmpty() && searchText.length > MIN_THRESHOLD_SEARCH) {
                        searchTextFlow.value = it
                    } else {
                        vm.onEmptySeachText()
                    }
                },

                onScannerClick = {
                    vm.onScanPressed()
                },

                state = state,
                vm = vm,
                focusRequester = focusRequester,
                onFocusChanged = { hasFocus, focusRequester ->
                    vm.onFocusChanged(hasFocus)
                },
                onImageClicked = { product ->
                    //       vm.onShowProductRequest(product)
                },
                onAddButtonClicked = { product ->
                    triggerHapticFeedback()
                    vm.onAddProductAsk(product)

                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.itemsList) { item ->
                    ShoppingListProductRow(
                        product = item,
                        onProductClicked = {
                            //        vm.onProductClicked(it)
                        },
                        onProductDeleted = {
                            //        vm.onProductDeleted(it)
                        },
                        onToggle = {
                            val myUserItem = item.quantities?.first { it.userId == userKey }
                            myUserItem?.let {
                                val isSelected = it.qty > 0
                                if (isSelected)
                                    onDecButtonPressed(
                                        listId = myUserItem.listId,
                                        ean = myUserItem.ean,
                                        userId = myUserItem.userId,
                                        value = 0.0
                                    )
                                else
                                    onIncButtonPressed(
                                        listId = myUserItem.listId,
                                        ean = myUserItem.ean,
                                        userId = myUserItem.userId,
                                        value = 1.0
                                    )
                            }
                            // aca hacer la logica para que marque o desmarque el item
                        },
                        /*
                                               , onIncrement = { listId, ean, userId, value ->
                                               onIncButtonPressed(listId, ean, userId, value)
                                           }, onDecrement = { listId, ean, userId, value ->
                                               onDecButtonPressed(listId, ean, userId, value)
                                           }
                       */
                    )

                }
            }
        }


    }
}

@Composable
fun UsersSection(
    modifier: Modifier = Modifier, usersList: List<ShoppingListMemberComplete>
) {

    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.cardColors().copy(
            containerColor = Color.White,

            ),
        shape = MaterialTheme.shapes.medium,

        ) {
        Column(
            modifier = modifier
                .padding(horizontal = 4.dp)

        )
        {
            Text(
                text = "Miembros", style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            LazyRow(modifier.fillMaxWidth()) {
                items(usersList) { user ->
                    RoundMemberItem(member = user)
                }
            }
        }
    }
}

@Composable
fun RoundMemberItem(
    avatarSize: Dp? = 60.dp,
    member: ShoppingListMemberComplete,
    onClick: () -> Unit = {},
) {
    Column {

        val imageUrl = getProfileImageURL(member.userId, member.user?.fileName)

        UserImage(
            modifier = Modifier.size(avatarSize!!),
            urlImage = imageUrl,
            onClick = onClick
        )

        Text(
            text = member.user?.nick?.uppercase() ?: "XXXXXX", style = StyleLight()
        )
    }

}


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchTextFieldWithScanner(
    userKey: String? = null,
    text: String,
    onTextChange: (String) -> Unit,
    loadingProducts: Boolean,
    onScannerClick: () -> Unit,
    state: UiState,
    vm: ShoppingListAddEditViewModel,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean, FocusRequester) -> Unit,
    onImageClicked: (Product) -> Unit = {},
    onAddButtonClicked: (Product) -> Unit = {},
) {
    var showScannerIcon by remember { mutableStateOf(true) }

    //--------------------
    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    //  var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }


    ExposedDropdownMenuBox(
        expanded = state.searchResultsExpanded && state.searchResults.size > 1,
        onExpandedChange = {
            vm.onPulldownStatusInvert()
        }
    ) {

        ReusableSearchTextField(
            text = text,
            onTextChange = onTextChange,
            loadingProducts = loadingProducts,
            showScannerIcon = showScannerIcon,
            onScannerClick = onScannerClick,
            focusRequester = focusRequester,
            onFocusChanged = onFocusChanged,
            onScannerButtonVisibilityChange = {
                showScannerIcon = it
            },
            onTextSearchRequested = {
                var pp = 3
            },
        )


        ExposedDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = state.searchResultsExpanded && state.searchResults.size > 1,
            onDismissRequest = {
                vm.onPulldownStatusInvert()
            }
        ) {
            state.searchResults.forEach { product ->
                DropdownMenuItem(
                    onClick = {
                        triggerHapticFeedback()
                        selectedOptionText = product.name.toString()
                    }
                ) {

                    //         dropDownItemComposable.invoke(product.toProductOnSearch())

                    DropdownItemProductSearch(
                        product = product,
                        onImageClicked = {
                            onImageClicked(it)
                        },
                        onAddButtonClicked = {
                            //  vm.closeDropDown()
                            onAddButtonClicked(it)
                        },
                        onExistingIcon = Icons.Outlined.CheckCircle,
                        onNonExistingIcon = Icons.Outlined.AddCircle,
                    )
                }
            }
        }
    }

}

@Composable
fun DropdownItemProductSearch(
    product: ProductOnSearch,
    showAddButton: Boolean = false,
    onImageClicked: (Product) -> Unit,
    onAddButtonClicked: (Product) -> Unit,
    onExistingIcon: ImageVector? = null,
    onNonExistingIcon: ImageVector? = null,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        // Imagen del producto

        if (product.haveImage) {
            val urlProduct = getProductImageUrl(product.ean.toString())
            AsyncImage(
                model = urlProduct,
                placeholder = painterResource(Res.drawable.sin_imagen),
                contentDescription = product.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable { onImageClicked(product.toProduct()) }
            )
        }
        Spacer(Modifier.width(8.dp))
        // Nombre y marca del producto
        Column(
            Modifier.weight(1f)
        ) {
            Text(text = product.name.toString(), fontWeight = FontWeight.Bold)
            Text(text = product.brand.toString(), fontSize = 12.sp)
        }

        //if (showAddButton) {
        when (product.status) {
            ALREADY_EXISTS -> {
                onExistingIcon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = "Ya existe",
                        tint = Color.Green
                    )
                }
            }

            NON_EXISTING -> {
                onNonExistingIcon?.let {
                    /*
                                           Icon(
                                               imageVector = it,
                                               contentDescription = "No existe",
                                               tint = Color.Green
                                           )
                   */
                    IconButton(
                        modifier = Modifier,
                        onClick = {
                            triggerHapticFeedback()
                            onAddButtonClicked(product.toProduct())
                        }) {
                        Icon(it, contentDescription = "Add to shopping list")
                    }

                }
            }
        }


        //  }
    }
}
