package com.iyr.ultrachango.ui.screens.searchitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.iyr.ultrachango.data.models.ProductOnSearch
import com.iyr.ultrachango.utils.helpers.getProductImageUrl
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import org.jetbrains.compose.resources.painterResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.sin_imagen


/**
 * Composable que representa un elemento de búsqueda de producto .
 * Muestra la imagen, nombre y marca del producto,sin otro contenido
 *
 * @param product El producto a mostrar.
 * @param onImageClick Acción a realizar al hacer clic en la imagen del producto.
 * @param onAddClick Acción a realizar al hacer clic en el icono de agregar.
 */
@Composable
fun ProductSearchItem(
    product: ProductOnSearch,
    onImageClick: (ProductOnSearch) -> Unit,
    onItemClick: (ProductOnSearch) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .clickable {
                onItemClick.invoke(product) }
    ) {
        // Imagen del producto
       // if (product.haveImage == true) {
            AsyncImage(
                model = getProductImageUrl(product.ean ?: ""),
                placeholder = painterResource(Res.drawable.sin_imagen),
                contentDescription = product.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable { onImageClick(product) },
                onSuccess = {

                    if (product.haveImage?: false == false) {
//                        TODO() : Pedir al servidor que mande a descargar la imagen. y cambiar a true
                    }
                }
            )
      //  }


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
    }
}

/**
 * Composable que representa un elemento de producto en la pantalla de inicio.
 * Muestra la imagen, nombre y marca del producto y un boton para agregar.
 */
@Composable
fun HomeProductItem(
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
        when (product.status) {
            "exists" -> Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Ya existe",
                tint = Color.Green
            )

            else -> IconButton(
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

