package com.iyr.ultrachango.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.utils.helpers.getProductImageUrl
import com.iyr.ultrachango.utils.ui.elements.buttonShapeSmall
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import org.jetbrains.compose.resources.painterResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.sin_imagen


@Composable
fun AddProductConfirmationDialog(
    product: Product,
    onAddButtonClicked: (Product, String) -> Unit,
    onDismissRequest: (() -> Unit)? = null,
    userId: String
) {


    var selectedList by remember { mutableStateOf(mutableStateListOf<ShoppingList>()) }
    var showListsSelector by remember { mutableStateOf<Boolean>(false) }
    var showNoMessagesText by remember { mutableStateOf(true) }



    Dialog(onDismissRequest = {
        if (onDismissRequest != null) {
            onDismissRequest()
        }
    }) {
        Card(
            modifier = Modifier.padding(10.dp), shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {


                 //   var urlProduct =
                 //       "https://imagenes.preciosclaros.gob.ar/productos/${product.ean}.jpg"
                    val urlProduct = getProductImageUrl(product.ean.toString())

                    AsyncImage(
                        model = urlProduct,
                        placeholder = painterResource(Res.drawable.sin_imagen),
                        contentDescription = product.name,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.height(200.dp)

                    )


                }
                Text(
                    text = product.name.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                )

                Text(
                    text = product.brand.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 8.dp),
                )
                Button(
                    modifier = Modifier.padding(8.dp),
                    shape = buttonShapeSmall,
                    onClick = {
                        triggerHapticFeedback()
                        onAddButtonClicked(product, userId)
                    },
                    content = {
                        Text(
                            color = Color.White,
                            text = "Agregar"
                        )
                    },
                    colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                )


                Button(
                    modifier = Modifier.padding(8.dp)
                        .fillMaxWidth(),
                    shape = buttonShapeSmall,
                    onClick = {
                        triggerHapticFeedback()
                        if (onDismissRequest != null) {
                            onDismissRequest()
                        }
                    },
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
    }
}


