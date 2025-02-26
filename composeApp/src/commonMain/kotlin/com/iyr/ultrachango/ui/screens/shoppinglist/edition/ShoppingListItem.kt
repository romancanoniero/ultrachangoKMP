package com.iyr.ultrachango.ui.screens.shoppinglist.edition

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.outlined.FrontHand
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.iyr.ultrachango.data.models.ShoppingListProductComplete
import com.iyr.ultrachango.data.models.ShoppingListQuantities
import com.iyr.ultrachango.ui.theme.cardViewColors
import com.iyr.ultrachango.ui.theme.cardViewElevation
import com.iyr.ultrachango.utils.helpers.getProductImageUrl
import com.iyr.ultrachango.utils.helpers.getProfileImageURL
import com.iyr.ultrachango.utils.ui.UserImage
import com.iyr.ultrachango.utils.ui.elements.ItemListImageBox
import com.iyr.ultrachango.utils.ui.elements.ItemListTextRegular
import com.iyr.ultrachango.utils.ui.elements.ItemListTextSubHeader
import com.iyr.ultrachango.utils.ui.elements.UserPictureRegular
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import org.jetbrains.compose.resources.painterResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.profile_pic
import ultrachango2.composeapp.generated.resources.sin_imagen

private val Color.Companion.Orange: Color
    get() {
        return Color(0xFFFFA500)
    }


@Composable
fun ShoppingListProductRow(
    product: ShoppingListProductComplete,
    onProductClicked: () -> Unit = {},
    onProductDeleted: () -> Unit = {},
    onToggle: () -> Unit = {},
    onIncrement: (Int, String, String, Double) -> Unit = { _, _, _, _ -> },
    onDecrement: (Int, String, String, Double) -> Unit = { _, _, _, _ -> }

) {

    val expanded = rememberSaveable { mutableStateOf(false) }


    ProductInfoItem(
        product,
        isExpanded = expanded.value,
        onToggle = onToggle,

        onToggleVisibility = {
//            expanded.value = !expanded.value

        },
        onIncrement = onIncrement,
        onDecrement = onDecrement
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
        ProductQuantitiesExtended(product, onIncrement, onDecrement)
    }
    /*
     Divider(
         modifier = Modifier.fillMaxWidth().height(2.dp)
     )
 */

}

@Composable
fun UsersWhoWantIt(
    modifier: Modifier = Modifier,
    product: ShoppingListProductComplete,
    onClick: () -> Unit,
) {
    val quantities = product.quantities
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
        }

    }
}


@Composable
fun UserQuantityWithBadge(
    modifier: Modifier = Modifier,
    qtyRecord: ShoppingListQuantities,
    onClick: () -> Unit?,
) {
    Box(
        modifier = modifier
            .padding(0.dp)
            //    .background(Color.LightGray)
            .clickable {
                onClick()
            }
    ) {
        getProfileImageURL(qtyRecord.userId.toString(), "").let {
            UserPictureRegular(
                modifier = Modifier.size(60.dp),
                imageModel = it!!,
                contentDesription = "",
                onClick = {
                    triggerHapticFeedback()
                    onClick()
                },

                )
        }



        Badge(
            modifier = Modifier
                .offset(-2.dp, 2.dp)
                .border(1.dp, color = Color.White, shape = CircleShape)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
        ) {
            Text(
                text = if (qtyRecord.qty % 1 == 0.0) qtyRecord.qty.toInt()
                    .toString() else qtyRecord.qty.toString()
            )
        }
    }


}


@Composable
fun UserWithHand(
    modifier: Modifier = Modifier,
    qtyRecord: ShoppingListQuantities,
    onClick: () -> Unit?,
) {


    Box(
        modifier = modifier
            .padding(0.dp)
            //    .background(Color.LightGray)
            .clickable {
                onClick()
            }
        , contentAlignment = Alignment.BottomStart
    ) {

        val imageUrl = getProfileImageURL(qtyRecord.userId.toString(), qtyRecord.user?.fileName)




        UserImage(
            modifier = Modifier.fillMaxHeight(.80f)
                .aspectRatio(1f),
            urlImage = imageUrl,
            onClick = {
                triggerHapticFeedback()
                onClick()
            },
        )


        Badge(
            modifier = Modifier
                .offset(-2.dp, 2.dp)
                .fillMaxHeight(.4f)
                .aspectRatio(1f)
                .border(1.dp, color = Color.LightGray, shape = CircleShape)
                .align(Alignment.TopEnd)
                .clip(CircleShape),
            containerColor = Color.White
        ) {
            if (qtyRecord.qty == 0.0) {
                Icon(
                    imageVector = Icons.Outlined.FrontHand,
                    contentDescription = "solicitado",
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.FrontHand,
                    contentDescription = "solicitado",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Orange

                )
            }
        }
    }


}

@Composable
fun ProductQuantitiesExtended(
    product: ShoppingListProductComplete,
    onIncrement: (Int, String, String, Double) -> Unit,
    onDecrement: (Int, String, String, Double) -> Unit
) {
    val quantities = product.quantities
    Column {
        quantities?.forEach { qtyRecord ->
            QuantitySelectorItem(
                qtyRecord = qtyRecord,
                onIncrement = onIncrement,
                onDecrement = onDecrement
            )
        }
    }

}

@Composable
fun ProductInfoItem(
    product: ShoppingListProductComplete,
    isExpanded: Boolean = false,
    onToggleVisibility: () -> Unit = {},
    onToggle: () -> Unit = {},
    onIncrement: (Int, String, String, Double) -> Unit,
    onDecrement: (Int, String, String, Double) -> Unit
) {
//    var urlProduct = "https://imagenes.preciosclaros.gob.ar/productos/${product.product?.ean}.jpg"
    var urlProduct = getProductImageUrl(product.product?.ean.toString())
    //    "https://imagenes.preciosclaros.gob.ar/productos/${product.product?.ean}.jpg"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp), colors = cardViewColors, elevation = cardViewElevation
        // colors = CardDefaults.cardColors().copy(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {


            Row {
           Card(
               modifier = Modifier.padding(10.dp),
               colors = CardDefaults.cardColors().copy(
                     containerColor = Color.White,

               ),
               elevation = CardDefaults.elevatedCardElevation(
                     defaultElevation = 2.dp,
               )
           )
           {
               Box(modifier = Modifier.size(80.dp))
               {
                   if (product.product?.haveImage ?: false) {
                       ItemListImageBox(
                           modifier = Modifier.fillMaxSize(),
                           imageModel = urlProduct,
                           contentDesription = product.product?.name ?: ""
                       )
                   }
                   else
                   {
                       Image(modifier = Modifier.fillMaxSize(),
                           painter = painterResource(Res.drawable.sin_imagen),
                           contentDescription = "No image",
                           contentScale = ContentScale.Crop)
                   }
               }

           }


                Column(modifier = Modifier.fillMaxWidth()) {
                    ItemListTextRegular(text = product.product?.ean ?: "")
                    ItemListTextSubHeader(text = product.product?.name ?: "")

                    Row(modifier = Modifier.fillMaxWidth())
                    {
                        ItemListTextRegular(
                            modifier = Modifier.weight(1f),
                            text = product.product?.brand ?: ""
                        )


//                        if (!isExpanded) {
                        UsersWhoWantIt(
                            modifier = Modifier.wrapContentWidth(),
                            product,
                            onClick = {
                                triggerHapticFeedback()
                                onToggle()
                            })

                        /*
                                              } else {

                                                  Box(modifier = Modifier.size(40.dp))
                                                  {
                                                      if (isExpanded) {
                                                          Icon(imageVector = Icons.Outlined.RemoveCircle,
                                                              contentDescription = "expandir",
                                                              modifier = Modifier
                                                                  .fillMaxSize()
                                                                  .clickable {
                                                                      onToggleVisibility()
                                                                  })

                                                          println("expandir")
                                                      } else {
                                                          Icon(imageVector = Icons.Outlined.AddCircle,
                                                              contentDescription = "comprimir",
                                                              modifier = Modifier
                                                                  .fillMaxSize()
                                                                  .clickable {
                                                                      onToggleVisibility()
                                                                  })
                                                          println("compressed")

                                                      }
                                                  }
                                              }

                         */
                    }

                }
            }



            if (isExpanded) {
                ProductQuantitiesExtended(product, onIncrement, onDecrement)
            }
        }

    }
}

@Composable
fun QuantitySelectorItem(
    modifier: Modifier = Modifier,
    profilePictureModel: String = "https://api.dicebear.com/7.x/pixel-art/png",
    contentDescription: String = "product",
    userkey: String = "",
    qtyRecord: ShoppingListQuantities,
    onIncrement: (Int, String, String, Double) -> Unit,
    onDecrement: (Int, String, String, Double) -> Unit,
    onClick: () -> Unit? = {},
) {

    val counter = rememberSaveable { mutableStateOf(qtyRecord.qty) }
    var prevValue = rememberSaveable { mutableStateOf(qtyRecord.qty) }

    Row {
        getProfileImageURL(qtyRecord.userId, "").let {

            UserPictureRegular(
                modifier = Modifier.size(60.dp),
                imageModel = it!!,
                contentDesription = contentDescription,
                onClick = onClick,

                )

        }


        ItemListTextRegular(text = userkey)

        Row {
            Row {
                IconButton(
                    onClick = {
                        triggerHapticFeedback()
                        if (counter.value > 0) {
                            counter.value--
                        }
                        try {
                            onDecrement(
                                qtyRecord.listId, qtyRecord.ean, qtyRecord.userId, counter.value
                            )
                            prevValue.value = counter.value
                        } catch (e: Exception) {
                            counter.value = prevValue.value
                        }
                    },
                    modifier = Modifier.size(48.dp).clip(CircleShape).shadow(4.dp, CircleShape)
                        .background(Color.Gray).clickable(onClick = {

                        },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() })
                ) {
                    Text("-", color = Color.White, style = MaterialTheme.typography.titleLarge)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = "${counter.value}", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        triggerHapticFeedback()
                        println("Aumentar")
                        counter.value++
                        println(counter.value)

                        try {
                            onIncrement(
                                qtyRecord.listId, qtyRecord.ean, qtyRecord.userId, counter.value
                            )
                            prevValue.value = counter.value
                        } catch (e: Exception) {
                            counter.value = prevValue.value
                        }
                    },
                    modifier = Modifier.size(48.dp).clip(CircleShape).shadow(4.dp, CircleShape)
                        .background(Color.Gray).clickable(onClick = {

                        },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() })
                ) {
                    Text("+", color = Color.White, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
