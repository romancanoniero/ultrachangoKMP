package com.iyr.ultrachango.ui.dialogs

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewModelScope
import coil3.compose.AsyncImage
import com.iyr.ultrachango.data.models.PriceInBranch
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.ui.screens.home.HomeScreenViewModel
import com.iyr.ultrachango.ui.theme.SFProMediumFontFamily
import com.iyr.ultrachango.utils.coroutines.Resource
import com.iyr.ultrachango.utils.formatCurrency
import com.iyr.ultrachango.utils.helpers.getBrandImageUrl
import com.iyr.ultrachango.utils.helpers.getProductImageUrl
import com.iyr.ultrachango.utils.ui.LoadingIndicator
import com.iyr.ultrachango.utils.ui.elements.StyleTextRegular
import com.iyr.ultrachango.utils.ui.elements.buttonShapeSmall
import com.iyr.ultrachango.utils.ui.elements.textSize12
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.sin_imagen
import kotlin.math.roundToInt


@Composable
fun ProductInfoDialog(
    vm: HomeScreenViewModel? = null,
    userKey: String? = null,
    data: Product,
    availableList: List<ShoppingList>?,
    selectedShoppingLists: List<Long>,
    onDismissRequest: () -> Unit,
    onListUnselected: (list: ShoppingList) -> Unit,
    onListSelected: (list: ShoppingList) -> Unit,
    onFavPressed: (Product, Boolean) -> Unit,

    ) {


    //  var selectedLists by remember { mutableStateOf(mutableStateListOf<List<Long>>(selectedShoppingLists)) }
    var showListsSelector by remember { mutableStateOf<Boolean>(false) }
    var showNoMessagesText by remember { mutableStateOf(true) }

    var isFavorite by remember { mutableStateOf(false) }


    var isSearchingPrices by remember { mutableStateOf(false) }
    var pricesAround by remember { mutableStateOf<List<PriceInBranch>?>(null) }


    val pxToMove = with(LocalDensity.current) {
        10.dp.toPx().roundToInt()
    }
    val offset by animateIntOffsetAsState(
        targetValue = if (pricesAround != null) {
            IntOffset(pxToMove, pxToMove)
        } else {
            IntOffset.Zero
        },
        label = "offset"
    )


    Dialog(
        properties = DialogProperties(dismissOnClickOutside = false),
        onDismissRequest = {
            onDismissRequest()
        }) {
        Card(
            modifier = Modifier.padding(10.dp), shape = RoundedCornerShape(16.dp)
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
            )
            {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                            .height(if (pricesAround == null) 200.dp else 100.dp)
                            //  .background(Color.Yellow)
                            .graphicsLayer {

                                this.scaleX = if (pricesAround == null) 1.0f else 0.5f
                                this.scaleY = if (pricesAround == null) 1.0f else 0.5f
                            },
                        contentAlignment = Alignment.Center
                    ) {


                        var urlProduct =
                            "https://imagenes.preciosclaros.gob.ar/productos/${data.ean}.jpg"
/*
                        var urlProduct = getProductImageUrl(data.ean.toString())
                        urlProduct = getProductImageUrl("7790240002010")
*/

                        AsyncImage(
                            model = urlProduct,
                            placeholder = painterResource(Res.drawable.sin_imagen),

                            contentDescription = data.name,
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier.fillMaxSize().offset { offset }


                        )


                    }
                    Text(
                        text = data.name.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp),
                    )

                    Text(
                        text = data.brand.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 8.dp),
                    )

                    if (pricesAround == null) {
                        Button(
                            modifier = Modifier.padding(8.dp),
                            shape = buttonShapeSmall,
                            onClick = {
                                triggerHapticFeedback()
                                showListsSelector = true },
                            content = {
                                Text(
                                    color = Color.White,
                                    text = "Agregar a lista"
                                )
                            },
                            colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            ),
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        modifier = Modifier.padding(8.dp),
                        shape = buttonShapeSmall,
                        onClick = {
                            triggerHapticFeedback()
                            isSearchingPrices = true
                            vm?.viewModelScope?.launch {

                                val coords: Pair<Double, Double>? = vm.getRefLocation()
                                if (coords != null) {
                                    vm.getPrices(
                                        ean = data.ean.toString(),
                                        latitude = coords.first,
                                        longitude = coords.second,
                                        userId = userKey!!,
                                        onResults = { results ->
                                            isSearchingPrices = false
                                            var pp = results
                                            pricesAround = results.data
                                        },
                                        onError = { message ->
                                            isSearchingPrices = false
                                         //   ErrorDialog("Error", message ?: "Error desconocido")
                                        }
                                    )


                                } else {
                                    // TODO : Mostrar mensaje de error
                                }
                            }
                            //                  showListsSelector = true

                        },
                        content = {

                            Row()
                            {


                                Text(
                                    color = Color.White,
                                    text = "Consulta Precios"
                                )
                                if (isSearchingPrices) {
                                    LoadingIndicator(
                                        modifier = Modifier.size(22.dp),
                                        enabled = true,
                                    )
                                }
                            }
                        },
                        colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        ),
                    )


                    if (pricesAround != null) {

                        PricesSection(pricesAround)

                    }


                    if (pricesAround == null) {
                        if (showListsSelector == true) {
                            if ((availableList?.size ?: 0) > 0) {

                                ChipsSelector(
                                    availableList!!,
                                    selectedShoppingLists,
                                    onListUnselected = {
                                        onListUnselected(it)
                                    },
                                    onListSelected = {
                                        onListSelected(it)
                                    })
                                /*
                                                                        ListsSelector(
                                                                            lists = list!!,
                                                                            selectedList = selectedList,
                                                                            onListUnselected = {
                                                                                onListUnselected(it)
                                                                            },
                                                                            onListSelected = {
                                                                                onListSelected(it)
                                                                            })

                                                 */
                            } else {

                                if (showNoMessagesText)
                                    LaunchedEffect(Unit) {
                                        kotlinx.coroutines.delay(2000)
                                        showNoMessagesText = false
                                    }
                                if (showNoMessagesText) {
                                    Text("No hay listas disponibles")
                                }

                            }
                        }
                    }


                    Button(
                        modifier = Modifier.padding(8.dp)
                            .fillMaxWidth(),
                        shape = buttonShapeSmall,
                        onClick = {
                            triggerHapticFeedback()
                            onDismissRequest() },
                        content = {
                            Text(
                                color = Color.White,
                                text = "Cerrar"
                            )
                        },
                        colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        ),

                        )

                }
            }


            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(end = 20.dp, top = 20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        triggerHapticFeedback()
                        isFavorite = !isFavorite
                        onFavPressed(data, isFavorite)
                    },

                    ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (isFavorite) "Favorite icon" else "Not favorite icon",
                        tint = if (isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                }

            }
        }
    }
}

@Composable
private fun PricesSection(pricesAround: List<PriceInBranch>?) {
   Column(modifier = Modifier.fillMaxWidth())
   {
        Box(modifier = Modifier.fillMaxWidth())
        {
            Row()
            {
                Button( onClick = {})
                {
                    Text("Ordenar por precio")
                    // TODO : Ordenar por precio
                }
                Button(onClick =  {})
                {
                    Text("Ordenar por distancia")
                    // TODO : Ordenar por precio
                }

            }

        }
       LazyRow(modifier = Modifier.fillMaxWidth()) {
           pricesAround!!.forEach {
               item {
                   ItemPriceInBranch(it)
               }
           }
       }

   }


}

@Composable
fun ItemPriceInBranch(price: PriceInBranch) {
    Card(
        modifier = Modifier.fillMaxWidth(.5f)
            .aspectRatio(1f)
    )
    {
        Column()
        {

            val urlBrand = getBrandImageUrl(price.storeId!!, price.flagId!!)

            Row(modifier = Modifier.fillMaxWidth())
            {
                AsyncImage(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable {

                        },
                    model = urlBrand,
                    placeholder = painterResource(Res.drawable.sin_imagen),
                    contentDescription = price.branchName,
                )

                Column {
                    Text(
                        text = price.address.toString(),
                        style = StyleTextRegular().copy(fontSize = 12.sp),
                        maxLines = 2,


                        )

                    Text(
                        text = if (price.distance < 1)
                            (price.distance * 100).toString() + " mts"
                        else
                            price.distance.toString() + " kms",
                        style = StyleTextRegular().copy(fontSize = 12.sp)
                    )


                }
            }

            Row()
            {
                Text(
                    text = formatCurrency(price.price?.regularPrice ?: 0.0, "$"),
                    style = StyleTextRegular().copy(fontSize = textSize12)
                )

                if (price.price?.promo1?.price != 0.0) {
                    Text(text = (price.price?.promo1?.price ?: "0.0").toString())
                }
                if (price.price?.promo2?.price != 0.0) {
                    Text(text = (price.price?.promo2?.price ?: "0.0").toString())
                }
            }
        }
    }
}


@Composable
fun ListsSelector(
    lists: List<ShoppingList>,
    onListSelected: (ShoppingList) -> Unit,
    onListUnselected: (ShoppingList) -> Unit,
    selectedList: SnapshotStateList<ShoppingList>
) {


    Column {
        lists.forEach { shoppingList ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = shoppingList.listName.toString(),
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = SFProMediumFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    ),

                    modifier = Modifier.padding(16.dp),
                )


                Checkbox(
                    checked = selectedList.contains(shoppingList),

                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            selectedList.add(shoppingList)
                            onListSelected(shoppingList)
                        } else {
//                        selectedToppings.remove(topping)
                            onListUnselected(shoppingList)
                            selectedList.remove(shoppingList)
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipsSelector(
    lists: List<ShoppingList>,
    selectedList: List<Long>,
    onListSelected: (ShoppingList) -> Unit,
    onListUnselected: (ShoppingList) -> Unit,
) {

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp),
        modifier = Modifier
            .padding(7.dp)
    ) {
        lists.forEach { shoppingList ->
            val isSelected = selectedList.toList().contains(shoppingList.listId?.toLong())
            ItemChip(shoppingList, isSelected, onListSelected, onListUnselected)
        }
    }
}

@Composable
fun ItemChip(
    shoppingList: ShoppingList,
    isSelected: Boolean,
    onListSelected: (ShoppingList) -> Unit,
    onListUnselected: (ShoppingList) -> Unit
) {
    var selected by remember { mutableStateOf(isSelected) }

    FilterChip(
        onClick = {
            triggerHapticFeedback()
            selected = !selected
            if (selected) {
                onListSelected(shoppingList)
            } else {
                onListUnselected(shoppingList)
            }

        },
        label = {
            Text(shoppingList.listName.toString())
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )

}
