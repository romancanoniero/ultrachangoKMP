package com.iyr.ultrachango.utils.ui.elements.searchwithscanner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ProductOnSearch
import com.iyr.ultrachango.utils.helpers.getProductImageUrl
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import org.jetbrains.compose.resources.painterResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.sin_imagen

@Composable
fun DropdownItemProductSearch(
    product: ProductOnSearch,
    showAddButton: Boolean = false,
    onImageClicked: (Product) -> Unit = {},
    onAddButtonClicked: (Product) -> Unit = {},
    onExistingIcon: ImageVector? = null,
    onNonExistingIcon: ImageVector? = null,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        // Imagen del producto

     //   val urlProduct = "https://imagenes.preciosclaros.gob.ar/productos/${product.ean}.jpg"
        val urlProduct = getProductImageUrl(product.ean.toString())

        AsyncImage(
            model = urlProduct,
            placeholder = painterResource(Res.drawable.sin_imagen),
            contentDescription = product.name,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable { onImageClicked(product.toProduct() ) }
        )
        Spacer(Modifier.width(8.dp))
        // Nombre y marca del producto
        Column(
            Modifier.weight(1f)
        ) {
            Text(text = product.name.toString(), fontWeight = FontWeight.Bold)
            Text(text = product.brand.toString(), fontSize = 12.sp)
        }

        if (showAddButton) {
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
                        IconButton(
                            modifier = Modifier,
                            onClick = {
                                triggerHapticFeedback()
                                onAddButtonClicked(product.toProduct()) }) {
                            Icon(it, contentDescription = "Add to shopping list")
                        }
                    }
                }
            }

        }
    }
}

