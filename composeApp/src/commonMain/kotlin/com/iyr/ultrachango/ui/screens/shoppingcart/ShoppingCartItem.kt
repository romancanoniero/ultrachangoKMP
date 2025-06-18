@file:OptIn(ExperimentalMaterialApi::class)

package com.iyr.ultrachango.ui.screens.shoppingcart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import com.iyr.ultrachango.data.Api.preciosclaros.model.producto.Sucursale
import com.iyr.ultrachango.data.models.ShoppingCartProduct
import com.iyr.ultrachango.ui.theme.cardViewColors
import com.iyr.ultrachango.ui.theme.cardViewElevation
import com.iyr.ultrachango.utils.extensions.isDigitsOnly
import com.iyr.ultrachango.utils.formatCurrency
import com.iyr.ultrachango.utils.helpers.getProductImageUrl
import com.iyr.ultrachango.utils.ui.device.getScreenWidth
import com.iyr.ultrachango.utils.ui.elements.IconBox
import com.iyr.ultrachango.utils.ui.elements.IncDecSelector
import com.iyr.ultrachango.utils.ui.elements.ImageBox
import com.iyr.ultrachango.utils.ui.elements.ItemListTextRegular
import com.iyr.ultrachango.utils.ui.elements.ItemListTextSubHeader
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import com.kevinnzou.swipebox.SwipeBox
import com.kevinnzou.swipebox.SwipeDirection
import com.kevinnzou.swipebox.widget.SwipeIcon

@Composable
fun ShoppingCartItem(
    product: ShoppingCartProduct,
    isExpanded: Boolean = false,
    onToggleVisibility: () -> Unit = {},
    onToggle: () -> Unit = {},
    onIncrement: (String, Double) -> Unit,
    onDecrement: (String, Double) -> Unit,
    onProductClicked: (ShoppingCartProduct) -> Unit = {},
    onDeleteClicked: () -> Unit,
    currentPrice: Sucursale? = null,
) {


    SwipeBox(
        modifier = Modifier.fillMaxWidth(),
        swipeDirection = SwipeDirection.EndToStart,
        endContentWidth = 60.dp,
        endContent = { swipeableState, endSwipeProgress ->
            ShoppingCartProductItemActions(
                onDeleteClicked = {
                    triggerHapticFeedback()
                    onDeleteClicked.invoke()
                })
        }) { _, _, _ ->


        ShoppingCartProductItemContent(

            product = product,
            isExpanded = isExpanded,
            onToggleVisibility = onToggleVisibility,
            onToggle = onToggle,
            onItemClick = onProductClicked,
            onIncrement = onIncrement,
            onDecrement = onDecrement,
            currentPrice = currentPrice
        )

    }


}

@Composable
fun ShoppingCartProductItemActions(
    onDeleteClicked: () -> Unit,
) {

    val buttonWidth = getScreenWidth() / 6
    Row(
        modifier = Modifier.width(buttonWidth).fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {


        SwipeIcon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = "Delete",
            tint = Color.White,
            background = Color.Red,
            weight = 1f,

            iconSize = 48.dp,

            ) {
            onDeleteClicked.invoke()
        }
    }
}


@Composable
fun ShoppingCartProductItemContent(
    modifier: Modifier = Modifier,
    product: ShoppingCartProduct,
    isExpanded: Boolean = false,
    onToggleVisibility: () -> Unit = {},
    onToggle: () -> Unit = {},
    onImageClick: (String) -> Unit = {},
    onItemClick: (ShoppingCartProduct) -> Unit = {},
    onIncrement: (String, Double) -> Unit,
    onDecrement: (String, Double) -> Unit,
    currentPrice: Sucursale?
) = Card(
    modifier = modifier
        .fillMaxWidth()
        .padding(bottom = 10.dp)
        .height(IntrinsicSize.Max),
    colors = cardViewColors,
    elevation = cardViewElevation
    // colors = CardDefaults.cardColors().copy(containerColor = Color.White)
) {

    var currentQuantity = remember { mutableStateOf(product.qty ?: 0.0) }
    var previousQuantity = remember { mutableStateOf(product.qty ?: 0.0) }
    var urlProduct = getProductImageUrl(product?.ean.toString())

    val squareSize = getScreenWidth() * 0.3f


        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.size(squareSize).padding(6.dp)
                    .clickable(enabled = true, onClick = {
                        onItemClick(product)
                    })
            ) {
                Card(
                    modifier = Modifier.size(squareSize).clickable(enabled = true, onClick = {
                        onItemClick(product)
                    }),
                    colors = CardDefaults.cardColors().copy(
                        containerColor = Color.White,
                    ), elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 4.dp,
                    )
                ) {
                    if (product.ean.toString().isDigitsOnly()) {
                        ImageBox(
                            modifier = Modifier.fillMaxSize()
                                .focusable(false),
                            imageModel = urlProduct,
                            contentDesription = product?.name ?: "",
                            showImage = product?.haveImage ?: false,
                            onClick = { onImageClick(product.ean.toString()) }
                        )
                    }
                    else
                    {

                        IconBox(
                            modifier = Modifier.fillMaxSize()
                                .focusable(false),
                            icon = Icons.Outlined.QuestionMark,
                            contentDesription = product?.name ?: "",
                            showImage = product?.haveImage ?: false,
                            onClick = { onImageClick(product.ean.toString()) }
                        )
                    }


                    /*
                    if (product?.haveImage == true) {
                        ItemListImageBox(
                            modifier = Modifier.fillMaxSize()
                                .focusable(false)
                            ,
                            imageModel = urlProduct,
                            contentDesription = product?.name ?: ""
                        )
                    } else {
                        Image(
                            modifier = Modifier.fillMaxSize()
                                .focusable(false),
                            painter = painterResource(Res.drawable.sin_imagen),
                            contentDescription = "No image",
                            contentScale = ContentScale.Crop
                        )
                    }
                    */
                }

            }


            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)

            ) {

                ItemListTextSubHeader(
                    modifier = Modifier
                        .focusable(false),
                    text = product?.name?.toUpperCase(Locale.current) ?: ""
                )
                ItemListTextSubHeader(
                    modifier = Modifier
                        .focusable(false),

                    text = product?.brand?.toUpperCase(Locale.current) ?: ""
                )
                ItemListTextRegular(
                    modifier = Modifier
                        .focusable(false),
                    text = (if (product?.ean.toString()
                            .isDigitsOnly()
                    ) product?.ean else "").toString()
                )

                Row(
                    modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.Bottom
                ) {

                    if (currentPrice != null) {
                        PricesDetail(currentPrice)
                    }

                    RequirersSection(items = product.requirers)

                    Spacer(modifier = Modifier.fillMaxWidth().weight(1f))

                    IncDecSelector(
                        counter = currentQuantity,
                        prevValue = previousQuantity,
                        onIncrement = { value ->
                            onIncrement(
                                product?.ean ?: "",
                                value ?: 0.0,
                            )
                        },
                        onDecrement = { value ->
                            onDecrement(
                                product?.ean ?: "",
                                value ?: 0.0,
                            )
                        })
                }


//---------------
            }

        }






    if (isExpanded) {
        //       ProductQuantitiesExtended(product, onIncrement, onDecrement)
    }
    //     }

    //}
}



@Composable
fun CurrentPriceSection(precio: Sucursale) {
    Text(text = formatCurrency(precio.preciosProducto.getPrecioLista()))
}

