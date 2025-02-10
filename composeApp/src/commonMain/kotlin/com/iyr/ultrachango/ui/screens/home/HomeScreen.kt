package com.iyr.ultrachango.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil3.compose.AsyncImage
import com.iyr.ultrachango.Constants
import com.iyr.ultrachango.data.models.Location
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.data.models.app.Section
import com.iyr.ultrachango.data.models.app.sections
import com.iyr.ultrachango.getCurrentLocation


import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.ui.dialogs.ErrorDialog
import com.iyr.ultrachango.ui.dialogs.ProductInfoDialog
import com.iyr.ultrachango.ui.rootnavigation.RootRoutes
import com.iyr.ultrachango.ui.screens.home.components.LocationIndicator
import com.iyr.ultrachango.ui.screens.navigation.AppRoutes
import com.iyr.ultrachango.ui.theme.textColor
import com.iyr.ultrachango.utils.extensions.isDigitsOnly
import com.iyr.ultrachango.utils.helpers.getProductImageUrl
import com.iyr.ultrachango.utils.sound.AudioPlayer
import com.iyr.ultrachango.utils.ui.LoadingIndicator
import com.iyr.ultrachango.utils.ui.MyText
import com.iyr.ultrachango.utils.ui.ShowKeyboard
import com.iyr.ultrachango.utils.ui.device.getScreenWidth
import com.iyr.ultrachango.utils.ui.elements.PicturesBoard
import com.iyr.ultrachango.utils.ui.elements.ReusableSearchTextField
import com.iyr.ultrachango.utils.ui.elements.StyleTextBig
import com.iyr.ultrachango.utils.ui.elements.screenOuterPadding
import com.iyr.ultrachango.utils.ui.elements.textSize12
import com.iyr.ultrachango.utils.ui.elements.textSize16
import com.iyr.ultrachango.utils.ui.elements.textSize20
import com.iyr.ultrachango.utils.ui.elements.textSize26
import com.iyr.ultrachango.utils.ui.pagerindicator.PagerIndicator
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.ncgroup.kscan.BarcodeFormats
import org.ncgroup.kscan.BarcodeResult
import org.ncgroup.kscan.ScannerView
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.hello_there
import ultrachango2.composeapp.generated.resources.sin_imagen


val MIN_THRESHOLD_SEARCH: Int = 3

@Composable
fun HomeScreen(
    navController: NavHostController,
    permissionsController: PermissionsController,
    vm: HomeScreenViewModel = koinViewModel(),
    scaffoldVM: ScaffoldViewModel,
) {



    navController.clearBackStack(RootRoutes.HomeRoute.route)

    LaunchedEffect(Unit)
    {
        vm.setPermissionsController(permissionsController)
    }

    // val state by remember { mutableStateOf(vm.state) }
    val state by vm.state.collectAsState()


    val fetchingLocations by vm.fetchingLocations.collectAsState()
    val locations by vm.knownLocations.collectAsState()

    val productsList by vm.productsList.collectAsState()
    val productsDropdownExpanded by vm.productsDropdownExpanded.collectAsState()


    scaffoldVM.setTitle("UltraChango")

    val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val controller: PermissionsController =
        remember(factory) { factory.createPermissionsController() }

    val focusRequester = remember { FocusRequester() }

    var searchText by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val previousRoute = remember { mutableStateOf<String?>(null) }

    if (currentBackStackEntry?.destination?.route != previousRoute.value) {
        val savedStateHandle = currentBackStackEntry?.savedStateHandle
        val onBarcodeScanned = savedStateHandle?.get<String>("onBarcodeScanned")
        if (onBarcodeScanned != null) {
            searchText = onBarcodeScanned.toString()
        }
        previousRoute.value = currentBackStackEntry?.destination?.route
    }


    val backStackEntry by navController.currentBackStackEntryAsState()
    var result by remember { mutableStateOf<String?>(null) }


    val coroutineScope: CoroutineScope = rememberCoroutineScope()


    val hello = stringResource(Res.string.hello_there) + ", " + vm.getUserFirstName()
    var salutation by remember { mutableStateOf<String>(hello.toString()) }


    val showCameraPreview by vm.showCameraPreview.collectAsState()
    // Binds the permissions controller to the LocalLifecycleOwner lifecycle.
     BindEffect(permissionsController)

    ShowKeyboard(showKeyboard = state.showKeyboard)
    //  LoadingIndicator(enabled = state.loading)

    var hideVirtualKeyboard by remember { mutableStateOf(false) }
    if (hideVirtualKeyboard) {
        ShowKeyboard(false)
        hideVirtualKeyboard = false
    }

    LaunchedEffect(Unit) {
      //  vm.fetchData()
    }


    println("Base - ubicaciones = "+ Json.encodeToString(locations))


    if (fetchingLocations) {

        getCurrentLocation(onLocationObtained = {
            println("Location obtained")
         //   vm.onLocationObtained(it)

        },

            onError = {
                println("Error: $it")
            })

    }

    if (state.showErrorMessage) {
        ErrorDialog(title = "Error", message = state.errorMessage.toString(), onDismissRequest = {
            vm.closeErrorDialogRequest()
        })
    }

    if (showCameraPreview) {
        //  Scanner
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

        if (scaffoldVM.state.value.showLoader) {
            LoadingIndicator(enabled = true)
        } else {
            LoadingIndicator(enabled = false)
        }

        Screen(
            hideVirtualKeyboard,
            state,
            vm,
            salutation,
            focusRequester,
            fetchingLocations,
            locations,
            searchText,
            productsDropdownExpanded,
            productsList,
            navController
        )

    }


}


@Composable
private fun Screen(
    hideVirtualKeyboard: Boolean,
    state: HomeScreenViewModel.UiState,
    vm: HomeScreenViewModel,
    salutation: String,
    focusRequester: FocusRequester,
    fetchingLocations: Boolean,
    locations: List<Location>,
    searchText: String,
    productsDropdownExpanded: Boolean,
    productsList: List<Product>,
    navController: NavHostController
) {

    var hideVirtualKeyboard1 = hideVirtualKeyboard
    var searchText1 = searchText

    Column(Modifier.fillMaxSize().padding(screenOuterPadding)
        .verticalScroll(rememberScrollState()).clickable {
            hideVirtualKeyboard1 = true
        })
    {

        if (state.productToShow != null) {
            ProductInfoDialog(
                userKey = vm.getUserKey(),
                vm = vm,
                data = state.productToShow!!.toProduct(),
                availableList = state.shoppingLists,
                selectedShoppingLists = state.productShoppingLists,
                onDismissRequest = { vm.onProductAlreadyShown() },
                onListUnselected = { list -> vm.onListUnselected(list) },
                onListSelected = { list ->
                    vm.onListSelected(list)
                },
                onFavPressed = { product, isFavorite ->
                    vm.onFavButtonPressed(product, isFavorite)
                },
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        println("Screen - ubicaciones = "+ Json.encodeToString(locations))

        UpperSection(salutation, focusRequester, vm, fetchingLocations, locations, state)

        Spacer(modifier = Modifier.height(20.dp))

        ProductsSearch(
            vm,
            searchText1,
            focusRequester,
            productsDropdownExpanded,
            productsList,
            hideVirtualKeyboard1
        )

        Spacer(modifier = Modifier.height(10.dp))

        Body(vm, navController, state)

    }
}

@Composable
private fun UpperSection(
    salutation: String,
    focusRequester: FocusRequester,
    vm: HomeScreenViewModel,
    fetchingLocations: Boolean,
    locations: List<Location>,
    state: HomeScreenViewModel.UiState
) {
    Column(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)

    ) {
        Salutation(salutation)

        Spacer(modifier = Modifier.height(10.dp))


        println("UpperSections - ubicaciones = "+ Json.encodeToString(locations))


        Header(
            focusRequester = focusRequester,
            vm = vm,
            fetchingLocations = fetchingLocations,
            locations = locations,
            uiState = state
        )

    }
}




@Composable
private fun Body(
    vm: HomeScreenViewModel,
    navController: NavHostController,
    state: HomeScreenViewModel.UiState
) {
    HorizontalBanner()

    val byMarket = sections.filter { it.sectionKey == "by_market" }
    val byShoppingList =
        sections.filter { it.sectionKey == Constants.SECTION_SHOPPING_LIST }
    val byRewardsBranchList = sections.filter { it.sectionKey == "rewards_list" }

    FastActions(vm)

    Categories(vm, navController, "Descuentos y Promociones", byRewardsBranchList)
    Categories(vm, navController, "Por Supermercado", byMarket)

    val shoppingLists = state.shoppingLists?.map { shoppingList ->
        Section(
            sectionKey = Constants.SECTION_SHOPPING_LIST,
            title = shoppingList.listName ?: "",
            photo = "shoppingList.photoUrl",
            route = AppRoutes.ProvidersList.route,
            params = arrayListOf(shoppingList.id),
            data = shoppingList
        )
    }

    shoppingLists?.let { lists ->
        Categories(vm, navController, "Tus Listas", lists)
    }
}

@Composable
fun FastActions( vm: HomeScreenViewModel,) {
   Row()
   {
   Button(
         onClick = {
             triggerHapticFeedback()
             vm.onScanPressed()
         },
         modifier = Modifier
              .padding(8.dp)
              .clip(RoundedCornerShape(50))
              .background(Color.Blue)
              .padding(16.dp)
    ) {
         Text("Buscar Precios", color = Color.Blue)

    }

   }
}

@Composable
private fun ProductsSearch(
    vm: HomeScreenViewModel,
    searchText1: String,
    focusRequester: FocusRequester,
    productsDropdownExpanded: Boolean,
    productsList: List<Product>,
    hideVirtualKeyboard1: Boolean
) {
    var searchText11 = searchText1
    var hideVirtualKeyboard11 = hideVirtualKeyboard1
    val searchTextFlow = remember { MutableStateFlow("") }
    LaunchedEffect(searchTextFlow) {
        searchTextFlow.debounce(1000) // Espera 2 segundos desde la última pulsación
            .collectLatest { text ->
                if (text.length >= MIN_THRESHOLD_SEARCH) {
                    if (text.length >= 4 && text.isDigitsOnly()) vm.onBarcodeScanned(text)
                    else vm.onProductTextInput(text, -34.586050, -58.504600)
                }
            }
    }

    SearchTextFieldWithScanner(
        text = searchText11,
        onTextChange = {
            searchText11 = it
            if (!searchText11.isEmpty() && searchText11.length > MIN_THRESHOLD_SEARCH) {
                searchTextFlow.value = it
            } else {
                vm.onEmptySeachText()
            }
        },
        onScannerClick = {
            vm.onScanPressed()
        },
        vm = vm,
        focusRequester = focusRequester,
        onFocusChanged = { hasFocus, focusRequester ->
            vm.onFocusChanged(hasFocus)
        },
        dropdownExpanded = productsDropdownExpanded,
        productsList = productsList,
        onDropdownExpandStatudChanged = { expanded ->
            hideVirtualKeyboard11 = expanded
        },

        )
}


@Composable
fun Salutation(salute: String) {
    println("Salutation $salute")
    Text(
        modifier = Modifier.fillMaxWidth(),
        style = StyleTextBig().copy(fontSize = textSize26, fontStyle = FontStyle.Italic),
        text = salute,
    )

}

@Composable
fun Categories(
    vm: HomeScreenViewModel,
    navController: NavHostController,
    title: String = "",
    data: List<Section> = emptyList()
) {
    Column {
        Text(
            title, style = TextStyle(
                fontWeight = FontWeight.Bold, fontSize = textSize16, color = textColor
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        CategoriesList(vm, navController, data)
    }
}

@Composable
fun CategoriesList(
    vm: HomeScreenViewModel, navController: NavHostController, data: List<Section> = emptyList()
) {

    val categoriesListState = rememberLazyGridState()


    val itemWidth =
        (getScreenWidth() / 2) - 15.dp // Ancho de cada item (mitad de la pantalla menos margen)

    LazyHorizontalGrid(rows = GridCells.Fixed(1),
        state = categoriesListState,
        reverseLayout = false,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.width(itemWidth * 2 + 10.dp).height(200.dp),

        content = {
            items(data) { item ->
                CategoryItem(
                    navController = navController, vm = vm, itemWidth = itemWidth, data = item
                )
            }
        })
}

@Composable
fun CategoryItem(
    vm: HomeScreenViewModel, navController: NavHostController, itemWidth: Dp, data: Section
) {
    when (data.sectionKey) {

        Constants.SECTION_SHOPPING_LIST -> {
            SectionShoppingListItem(itemWidth, data, onClick = {
                triggerHapticFeedback()
                val listId = (data.data as ShoppingList).listId!!
                val route = vm.onCreateRoutToShoppingListRequested(
                    listId
                )


                navController.navigate(route)
            })
        }

        else -> {
            Column(modifier = Modifier.width(itemWidth)) {
                Card(
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f / 1f), colors = CardColors(
                        containerColor = Color.White,
                        contentColor = textColor,
                        disabledContainerColor = Color.Blue,
                        disabledContentColor = Color.White
                    )
                ) {

                    Column(modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)) {


                        Card(
                            modifier = Modifier.fillMaxWidth().aspectRatio(1.45f / 1f),
                            elevation = CardDefaults.elevatedCardElevation()
                        ) {

                            AsyncImage(modifier = Modifier.fillMaxSize(),
                                model = data.photo,

                                contentDescription = data.title,
                                contentScale = ContentScale.Crop,
                                onError = {
                                    var error = it
                                })

                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(modifier = Modifier) {

                            Text(
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                text = data.title,
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal, fontSize = textSize20
                                )

                            )

                            Box(
                                modifier = Modifier.background(
                                    color = Color.LightGray, shape = CircleShape
                                )

                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.White
                                )

                            }
                        }

                    }

                }


            }
        }
    }


}

@Composable
fun SectionShoppingListItem(itemWidth: Dp, data: Section, onClick: () -> Unit) {
    Column(modifier = Modifier.width(itemWidth)) {
        Card(
            modifier = Modifier.fillMaxWidth().aspectRatio(1f / 1f), colors = CardColors(
                containerColor = Color.White,
                contentColor = textColor,
                disabledContainerColor = Color.Blue,
                disabledContentColor = Color.White
            ), onClick = onClick
        ) {

            Column(modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)) {


                Card(
                    modifier = Modifier.fillMaxWidth().aspectRatio(1.45f / 1f),
                    elevation = CardDefaults.elevatedCardElevation()
                ) {
/*
                    AsyncImage(modifier = Modifier.fillMaxSize(),
                        model = data.photo,
                        contentDescription = data.title,
                        contentScale = ContentScale.Crop,
                        onError = {
                            var error = it
                        })
*/


                    val imageLists = (data.data as? ShoppingList)?.items?.take(4)?.map { product ->
                        //"https://imagenes.preciosclaros.gob.ar/productos/${product.ean}.jpg"
                        getProductImageUrl(product.ean.toString())

                    } ?: emptyList()

                    val contentDescriptionLists =
                        (data.data as? ShoppingList)?.items?.map { product ->
                            "${product.ean}"
                        } ?: emptyList()

                    PicturesBoard(
                        modifier = Modifier.fillMaxSize(),
                        imagesList = imageLists,
                        contentDescriptionList = contentDescriptionLists,
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier) {

                    Text(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        text = data.title,
                        style = TextStyle(
                            fontWeight = FontWeight.Normal, fontSize = textSize20
                        )

                    )

                    Box(
                        modifier = Modifier.background(
                            color = Color.LightGray, shape = CircleShape
                        )

                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White
                        )

                    }
                }

            }

        }


    }

}

@Composable
fun RecomendedContacts() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth().height(200.dp)
    ) {
        item {

            Text(
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                ), text = "Recommended Contacts"
            )
        }

        items(10) {
            RecommendedContactItem()
        }
    }
}

@Composable
fun RecommendedContactItem() {
    Box(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.elevatedCardElevation()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(100.dp)
                    .padding(vertical = 10.dp, horizontal = 10.dp)
            ) {


                Box(
                    modifier = Modifier.height(100.dp).aspectRatio(1f).background(
                        Color.Gray, shape = RoundedCornerShape(20.dp)
                    )
                )

                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.weight(1f),

                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            ), text = "Cleaning"
                        )

                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }

                    Text(
                        text = "John Doe", style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                    )

                    Row {
                        Text(text = "4000/")
                        Text(text = "hour")

                    }
                }
            }
        }

    }
}


/****
 * Sirve para agregar un composable invisible al final y saber donde termina el scroll
 */

@Composable
fun bottomObject(onMeasured: (Float) -> Unit) {
    var theDensity: Float = LocalDensity.current.density
    Spacer(modifier = Modifier.onGloballyPositioned { position ->
        val result = (((position.positionInParent().y / theDensity) + 0.5).toFloat())
        onMeasured(result)
    }.height(20.dp))

}

@Composable
private fun MenuItems(navController: NavHostController) {

    Column {


    }

    //  val navigator = LocalNavigator.currentOrThrow
    // val postListScreen = rememberScreen(SharedScreen.PostServiceDetails)
    /*
        val menuItems = listOf(
            MenuItem(
                type = Specialties.ALL, title = "All", icon = painterResource(Res.drawable.ic_all_home)
            ), MenuItem(
                type = Specialties.CLEANING, title = "Clean", icon = painterResource(Res.drawable.ic_clean)
            ), MenuItem(
                type = Specialties.REPAIR_TV,
                title = "Repair",
                icon = painterResource(Res.drawable.ic_repair)
            ), MenuItem(
                type = Specialties.PEST_CONTROL,
                title = "Pest Control",
                icon = painterResource(Res.drawable.ic_pest)
            ), MenuItem(
                type = Specialties.LAUNDRY,
                title = "Laundry",
                icon = painterResource(Res.drawable.ic_laundry)
            )
        )



        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp),
            columns = GridCells.Fixed(3),
            userScrollEnabled = false,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            items(menuItems) { item ->
                ItemCategory(item.type, item.title, item.icon,
                    onClick = {
                     //   navigator.parent?.push(postListScreen)
                        navController.navigate(Routes.ProvidersList.createRoute(item.type.name.toString()))
                    })
            }

        }
    */
}

@Composable
fun Header(
    focusRequester: FocusRequester,
    vm: HomeScreenViewModel,
    fetchingLocations: Boolean,
    locations: List<Location>,
    uiState: HomeScreenViewModel.UiState
) {
    println("Header - ubicaciones = "+ Json.encodeToString(locations))



    Column(modifier = Modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.height(10.dp))

        LocationIndicator(
            focusRequester = focusRequester,
            vm = vm,
            uiState = uiState,
            fetchingLocations = fetchingLocations,
            locations = locations,

            navigateToAddLocation = {
                //  navController.navigate(Routes.AddLocation.createRoute())
                val pp = 33
            },
        )

    }


}

@Composable
fun HorizontalBanner() {
    val list = (0..100).toList()
    val pagerState = rememberLazyListState()
    val visibleIndex by remember {
        derivedStateOf {
            pagerState.firstVisibleItemIndex
        }
    }
    val coroutineScope = rememberCoroutineScope()
    Column {

        Spacer(Modifier.height(15.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val pagerState1 = rememberPagerState(initialPage = 0, pageCount = { 10 })
            val coroutineScope = rememberCoroutineScope()

            HorizontalPager(
                pageContent = { index ->
                    BannerItem(index)
                }, state = pagerState1, contentPadding = PaddingValues(end = 50.dp)
            )

            Spacer(Modifier.height(5.dp))
            PagerIndicator(
                Modifier.align(Alignment.CenterHorizontally), pagerState = pagerState1
            ) {
                coroutineScope.launch {
                    pagerState1.animateScrollToPage(it)
                }
            }

        }
    }


}


@Composable
fun BannerItem(index: Int) {
    Card(
        modifier = Modifier.width(300.dp).aspectRatio(2.46f / 1).padding(end = 20.dp),
        colors = CardColors(
            containerColor = Color(0xFF405768),
            contentColor = Color.White,
            disabledContainerColor = Color.Blue,
            disabledContentColor = Color.White
        )
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min).padding(14.dp)) {

            Column(
                modifier = Modifier

                    .fillMaxWidth().fillMaxHeight()
                    .padding(paddingValues = PaddingValues(start = 10.dp)).weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                MyText(text = "TOP DEAL $index")

                MyText(text = "40% DISCOUNT", fontSize = textSize20)

                Text(text = "all fresh vegetables", fontSize = textSize12)

            }


            Card(
                modifier = Modifier.width(110.dp).aspectRatio(1.30f / 1f), colors = CardColors(
                    containerColor = Color(0xFF6A7985),
                    contentColor = Color.White,
                    disabledContainerColor = Color.Blue,
                    disabledContentColor = Color.White
                )
            ) {}
        }


    }
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchTextFieldWithScanner(
    text: String,
    onTextChange: (String) -> Unit,
    onScannerClick: () -> Unit,
    vm: HomeScreenViewModel,
    productsList: List<Product> = emptyList(),
    dropdownExpanded: Boolean = false,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean, FocusRequester) -> Unit,
    onDropdownExpandStatudChanged: (Boolean) -> Unit

) {
    var showScannerIcon by remember { mutableStateOf(true) }

    //--------------------
    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    //  var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }



    onDropdownExpandStatudChanged(dropdownExpanded && productsList.size > 1)

    ExposedDropdownMenuBox(expanded = dropdownExpanded && productsList.size > 1,
        onExpandedChange = {
            vm.onPulldownStatusInvert()
        }) {


        ReusableSearchTextField(
            text = text,
            onTextChange = onTextChange,
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

        ExposedDropdownMenu(modifier = Modifier.fillMaxWidth(),
            expanded = dropdownExpanded && productsList.size > 1,
            onDismissRequest = {
                vm.onPulldownStatusInvert()
            }) {
            productsList.forEach { product ->
                DropdownMenuItem(onClick = {
                    triggerHapticFeedback()
                    selectedOptionText = product.name.toString()
                    vm.closeDropDown()
                    vm.onShowProductRequest(product.toProductOnSearch())
                }) {
                    DropdownItemProductSearch(product = product)
                }
            }
        }
    }

}

@Composable
fun DropdownItemProductSearch(
    product: Product, showAddButton: Boolean = false
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
    ) {
        // Imagen del producto


        if (product.haveImage == true) {
            val urlProduct = getProductImageUrl(product.ean.toString())

            AsyncImage(
                model = urlProduct,
                placeholder = painterResource(Res.drawable.sin_imagen),
                contentDescription = product.name,
                modifier = Modifier.size(48.dp).clip(CircleShape)
            )
        } else {
            Box(modifier = Modifier.size(48.dp)) {
                Image(
                    painter = painterResource(Res.drawable.sin_imagen),
                    contentDescription = "Sin imagen",
                    modifier = Modifier.size(48.dp).clip(CircleShape)
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        // Nombre y marca del producto
        Column {
            Text(text = product.name.toString(), fontWeight = FontWeight.Bold)
            Text(text = product.brand.toString(), fontSize = 12.sp)
        }

        if (showAddButton) {
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.AddCircle, contentDescription = "Add to shopping list")
            }
        }
    }
}

@Composable
private fun UserName(text: String) {
    Text(
        text = text, color = Color.White, style = TextStyle(
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold
        )
    )
}

@Composable
private fun SubTitle(text: String) {
    Text(
        text = text, color = Color.White, style = TextStyle(
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            color = Color.White,
            fontWeight = FontWeight.Normal
        )
    )
}
