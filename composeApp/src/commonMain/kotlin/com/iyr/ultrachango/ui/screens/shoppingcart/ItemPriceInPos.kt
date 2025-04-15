@file:OptIn(ExperimentalMaterialApi::class)

package com.iyr.ultrachango.ui.screens.shoppingcart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iyr.ultrachango.data.Api.preciosclaros.model.producto.Sucursale
import com.iyr.ultrachango.utils.formatCurrency
import com.iyr.ultrachango.utils.ui.device.getScreenWidth
import com.iyr.ultrachango.utils.ui.elements.Body1Text
import com.iyr.ultrachango.utils.ui.elements.Body2Text
import com.iyr.ultrachango.utils.ui.elements.H3Text
import com.iyr.ultrachango.utils.ui.elements.ImageBox

@Composable
fun ItemPriceInPos(pos: Sucursale, onPriceSelected: (Sucursale) -> Unit, selected: Sucursale?) {

    Content(pos, onPriceSelected,selected)


}

@Composable
private fun Content(
    pos: Sucursale,
    onPriceSelected: (Sucursale) -> Unit,
    selected: Sucursale?
) {

    val squareSize = getScreenWidth() * 0.3f

    Card(
        backgroundColor = if (pos == selected) Color.LightGray else Color.White,
        elevation = 4.dp ,
        onClick = {
            onPriceSelected(pos)
        },

    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
        )
        {

            Card(
                modifier = Modifier.size(squareSize)
                    .padding(0.dp),
                elevation = 4.dp,

            ) {
                val urlBrand =
                    "https://imagenes.preciosclaros.gob.ar/comercios/${pos.comercioId}-${pos.banderaId}.jpg"
                ImageBox(
                    modifier = Modifier.fillMaxSize().padding(0.dp),
                    imageModel = urlBrand,
                    contentDesription = "Product Image",
                    showImage = true,
                )
            }

            Column(
                modifier = Modifier.weight(1f)
                    .height(IntrinsicSize.Min)
                    .padding(start = 4.dp),
            ) {

                Body1Text(
                    text = pos.banderaDescripcion,
                    fontWeight = FontWeight.ExtraBold,
                )

                Body2Text(
                    text = pos.direccion,
                )

                Body2Text(
                    text = pos.distanciaDescripcion,
                )


            }

            Column(
                modifier = Modifier

                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            )
            {


                PricesDetail(pos)
            }
        }
    }
}



