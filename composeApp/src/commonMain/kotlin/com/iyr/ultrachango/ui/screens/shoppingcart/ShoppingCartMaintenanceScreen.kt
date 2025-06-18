package com.iyr.ultrachango.ui.screens.shoppingcart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.iyr.ultrachango.data.models.ShoppingCartProduct
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ProductOnSearch
import com.iyr.ultrachango.data.models.ShoppingListComplete
import com.iyr.ultrachango.data.models.ShoppingListMemberComplete
import com.iyr.ultrachango.data.models.UserMinimum
import com.iyr.ultrachango.data.models.toProduct
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.ui.dialogs.AddProductConfirmationDialog
import com.iyr.ultrachango.ui.dialogs.ConfirmationDialog
import com.iyr.ultrachango.ui.dialogs.ErrorDialog
import com.iyr.ultrachango.ui.screens.home.MIN_THRESHOLD_SEARCH
import com.iyr.ultrachango.ui.screens.shoppinglist.edition.UserWithHand
import com.iyr.ultrachango.utils.extensions.isDigitsOnly
import com.iyr.ultrachango.utils.helpers.getProductImageUrl
import com.iyr.ultrachango.utils.helpers.getProfileImageURL
import com.iyr.ultrachango.utils.sound.AudioPlayer
import com.iyr.ultrachango.utils.ui.ShowKeyboard
import com.iyr.ultrachango.utils.ui.UserImage
import com.iyr.ultrachango.utils.ui.capitalizeFirstLetter
import com.iyr.ultrachango.utils.ui.elements.CircleSize
import com.iyr.ultrachango.utils.ui.elements.StyleLight
import com.iyr.ultrachango.utils.ui.elements.mySearchTextFieldWithScanner.MySearchTextFieldWithScanner
import com.iyr.ultrachango.utils.ui.elements.searchwithscanner.ALREADY_EXISTS
import com.iyr.ultrachango.utils.ui.elements.searchwithscanner.GENERIC_PRODUCT
import com.iyr.ultrachango.utils.ui.elements.searchwithscanner.NON_EXISTING
import com.iyr.ultrachango.utils.ui.showLoader
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
import ultrachango2.composeapp.generated.resources.empty_cart
import ultrachango2.composeapp.generated.resources.empty_cart_message
import ultrachango2.composeapp.generated.resources.product_not_found
import ultrachango2.composeapp.generated.resources.shopping_lists
import ultrachango2.composeapp.generated.resources.sin_imagen


@Composable
fun ShoppingCartMaintenanceScreen(
    userKey: String,
//    preparationListId: Int?,
//    preparationListName: String?,
    navController: NavHostController,
    permissionsController: PermissionsController,
    vm: ShoppingCartMaintenanceViewModel = koinViewModel(),
    scaffoldVM: ScaffoldViewModel,
    onGroupButtonClicked: (listId: Long) -> Unit = {},
    userViewModel: UserViewModel = koinViewModel()
) {


    vm.assignPermissionsController(permissionsController)

    val state by vm.state.collectAsState()

    print(state.showEmptyConfirmationDialog)

    val showCameraPreview by vm.showCameraPreview.collectAsState()

    var searchText by remember { mutableStateOf("") }
    val searchTextFlow = remember { MutableStateFlow("") }
    val focusRequester = remember { FocusRequester() }


    fun onIncButtonPressed(

        ean: String, value: Double
    ) {
        vm.updateProductCounter(ean, value)
    }

    fun onDecButtonPressed(ean: String, value: Double) {
        vm.updateProductCounter(ean, value)
    }




    LaunchedEffect(Unit) {
        vm.fetchData()
    }


    if (state.showEmptyConfirmationDialog) {
        requestEmptyCart(vm)
    }

    if (state.loading) {
        showLoader()
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
            // val route = RootRoutes.ShoppingListMembersRoute.createRoute(listId ?: 0)
            // navController.navigate(route)
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
                            vm.onProductTextInput(
                                text = text,
                                includeFreeText = true
                            )
                    }
                }
        }


        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            ShoppingListSection(
                modifier = Modifier.fillMaxWidth(),
                shoppingLists = state.shoppingList,
                onIncludeList = { list ->
                    vm.onIncludeList(list)
                },
                onExcludeList = { list ->
                    vm.onExcludeList(list)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            MySearchTextFieldWithScanner<ProductOnSearch>(
                items = vm.state.collectAsState().value.searchResults,
                loadingProducts = vm.state.value.loadingProducts,
                text = searchText,
                onTextChange = {
                    searchText = it
                    if (!searchText.isEmpty() && searchText.length > MIN_THRESHOLD_SEARCH) {
                        searchTextFlow.value = it
                    } else {
                        vm.onEmptySeachText()
                    }
                },
                onScannerClick = { vm.onScanPressed() },
                focusRequester = focusRequester,
                onFocusChanged = { hasFocus, focusRequester ->
                    vm.onFocusChanged(hasFocus)
                },
                dropdownItemContent = { product ->



                    var newProducto = ShoppingCartProduct().apply {
                        ean = product.ean
                        name = product.name
                        brand = product.brand
                        presentation = product.presentation
                        haveImage = product.haveImage
                        status = product.status
                        qty = 0.0
                        requirers = arrayListOf()
                    }


                    ShoppingListProductItem(
                        product = newProducto,
                        onImageClick = {
                            //vm.onShowProductRequest(it)
                                       },
                        onAddClick = { vm.onAddProductAsk(it.toProduct()) }
                    )

                },

            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize())
            {

                items(state.shoppingCart?.items!!, key = { it.ean.toString() })
                { product ->
                    PreparationProductRow(
                        product = product,
                        onProductClicked = {
                            //        vm.onProductClicked(it)
                        },
                        onProductDeleted = {
                            //        vm.onProductDeleted(it)
                        },
                        onToggle = {
                            /*
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
                          */


                            // aca hacer la logica para que marque o desmarque el item
                        },
                        onIncrement = { ean, value ->
                            onIncButtonPressed(
                                ean = ean,
                                value = value
                            )
                        },
                        onDecrement = { ean, value ->
                            onDecButtonPressed(
                                ean = ean,
                                value = value
                            )
                        },
                        onDeleteClicked = {
                            vm.onDeleteProductClicked(product)
                        }


                    )

                }


            }


        }
    }


}

private fun ShoppingCartProduct.toProduct(): Product {
    return Product(
        ean = ean,
        name = name,
        brand = brand,
        description = description,
        presentationUnit = presentationUnit,
        presentationQty = presentationQty,
        marca_lower = marca_lower,
        message = message,
        nombre_lower = nombre_lower,
        presentation = presentation,
        haveImage = haveImage
    )
}


@Composable
fun SearchItem(
    product: ProductOnSearch,
    onImageClick: (ProductOnSearch) -> Unit,
    onAddClick: (ProductOnSearch) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Imagen del producto
        if (product.haveImage == true) {
            AsyncImage(
                model = getProductImageUrl(product.ean ?: ""),
                placeholder = painterResource(Res.drawable.sin_imagen),
                contentDescription = product.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable { onImageClick(product) }
            )
        }

        Spacer(Modifier.width(8.dp))

        // Información del producto
        Column(Modifier.weight(1f)) {
            Text(
                text = product.name ?: "",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = product.brand ?: "",
                fontSize = 12.sp
            )

        }

        // Icono de estado

       if (product.status.toString().equals(NON_EXISTING)) {
           IconButton(
               onClick = {
                   triggerHapticFeedback()
                   onAddClick(product)
               }
           ) {
               Icon(
                   Icons.Outlined.AddCircle,
                   contentDescription = "Agregar"
               )
           }
       }
    }
}



@Composable
fun ShoppingListProductItem(
    product: ShoppingCartProduct,
    onImageClick: (ShoppingCartProduct) -> Unit,
    onAddClick: (ShoppingCartProduct) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Imagen del producto
        if (product.haveImage == true) {
            AsyncImage(
                model = getProductImageUrl(product.ean ?: ""),
                placeholder = painterResource(Res.drawable.sin_imagen),
                contentDescription = product.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable { onImageClick(product) }
            )
        }

        Spacer(Modifier.width(8.dp))

        // Información del producto
        Column(Modifier.weight(1f)) {
            Text(
                text = product.name ?: "",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = product.brand ?: "",
                fontSize = 12.sp
            )
            // Información específica de ShoppingList
            Text(
                text = "Cantidad: ${product.qty ?: 0}",
                fontSize = 12.sp
            )
        }

        // Icono de estado
        if (product.qty != null && product.qty!! > 0) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Ya existe",
                tint = Color.Green
            )
        } else {
            IconButton(
                onClick = {
                    triggerHapticFeedback()
                    onAddClick(product)
                }
            ) {
                Icon(
                    Icons.Outlined.AddCircle,
                    contentDescription = "Agregar"
                )
            }
        }
    }
}


@Composable
fun requestEmptyCart(vm: ShoppingCartMaintenanceViewModel) {
    ConfirmationDialog(
        title = stringResource(Res.string.empty_cart),
        message = stringResource(Res.string.empty_cart_message),
        onDismiss = {
            // vm.onResetDialogsRequested()
            vm.onCloseDialogsRequested()
        },
        onAccept = {
            vm.onEmptyCartConfirmed()
        },

        )
}


@Composable
fun PreparationProductRow(
    product: ShoppingCartProduct,
    onProductClicked: () -> Unit = {},
    onProductDeleted: () -> Unit = {},
    onToggle: () -> Unit = {},
    onIncrement: (String, Double) -> Unit = { _, _ -> },
    onDecrement: (String, Double) -> Unit = { _, _ -> },
    onDeleteClicked: () -> Unit,
) {

    val expanded = rememberSaveable { mutableStateOf(false) }


    ShoppingCartItem(
        product,
        isExpanded = expanded.value,
        onToggle = onToggle,

        onToggleVisibility = {
//            expanded.value = !expanded.value

        },
        onIncrement = onIncrement,
        onDecrement = onDecrement,
        onDeleteClicked = onDeleteClicked,
    )


    /*
        if (!expanded.value) {
            ProductQuantitiesCompressed(
                product =product,
                onClick = {
                    expanded.value = !expanded.value
                })
        }
    */
    if (expanded.value) {
        //ProductQuantitiesExtended(product, onIncrement, onDecrement)
    }
    /*
     Divider(
         modifier = Modifier.fillMaxWidth().height(2.dp)
     )
 */

}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShoppingListSection(
    modifier: Modifier = Modifier,
    shoppingLists: List<ShoppingListComplete>,
    onIncludeList: (ShoppingListComplete) -> Unit,
    onExcludeList: (ShoppingListComplete) -> Unit,

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
                text = stringResource(Res.string.shopping_lists),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp),
                modifier = Modifier
                    .padding(4.dp)
            ) {


                val chipColors =
                    FilterChipDefaults.filterChipColors().copy(
                        containerColor = Color.White,
                        labelColor = Color.DarkGray,
                        selectedLabelColor = Color.White,
                        selectedContainerColor = Color.Black
                    )



                shoppingLists.forEach { list ->
                    var isSelected by remember { mutableStateOf(false) }
                    FilterChip(
                        modifier = Modifier.padding(10.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = chipColors,
                        onClick = {
                            isSelected = !isSelected

                            if (isSelected) {
                                onIncludeList(list)
                            } else {
                                onExcludeList(list)
                            }
                        },
                        label = { Text(text = list.listName.toString()) },
                        selected = isSelected,
                    )
                }
            }


            /*
            LazyRow(modifier.fillMaxWidth()) {
                items(usersList) { user ->
                    RoundMemberItem(member = user)
                }
            }
            */
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
        val imageUrl =
            getProfileImageURL(member.userKey, member.user?.profilePicturePath.toString())

        UserImage(
            modifier = Modifier.size(avatarSize!!),
            urlImage = imageUrl,
            onClick = onClick
        )

        Text(
            text = member.user?.displayName?.capitalizeFirstLetter() ?: member.user?.firstName
            ?: "".capitalizeFirstLetter(), style = StyleLight()
        )
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

        if (product.haveImage == true) {
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
            GENERIC_PRODUCT -> {
                onExistingIcon?.let {

                }
            }

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


@Composable
fun RequirersSection(items: ArrayList<UserMinimum>?) {
    items?.forEach { qtyRecord ->
        UserWithHand(
            modifier = Modifier
                .size(CircleSize.MEDIUM.value),
            qtyRecord.userKey

        )

    }

}

@Composable
fun UsersWhoWantIt(
    modifier: Modifier = Modifier,
    product: ShoppingCartProduct,
    onClick: () -> Unit,
) {
    val quantities = product.qty
    BoxWithConstraints(
        modifier = Modifier
            .wrapContentWidth()
            //     .background(Color.LightGray)
            .clickable(
                onClick =

                    {
                        triggerHapticFeedback()
                        onClick.invoke()
                    }, enabled = true
            ),
        contentAlignment = Alignment.CenterEnd
    )
    {

        Row(
            modifier = Modifier
                .wrapContentWidth()
            //              .background(Color.Red).alpha(.90f)
            ,
            horizontalArrangement = Arrangement.spacedBy((-(0.34f * 60)).dp)
        ) {


            var index = 0
            /*
            quantities?.forEach { qtyRecord ->
                UserWithHand(
                    modifier = Modifier
                        .size(50.dp)
                        //   .offset(x = ((-index * 0.34f * 60).dp))
                        .clickable(enabled = true, onClick = {
                            triggerHapticFeedback()
                            onClick()
                        }),
                    qtyRecord = qtyRecord,
                    onClick =
                        {
                            triggerHapticFeedback()
                            onClick.invoke()

                        }
                )

                index++
            }
      */
        }

    }
}

