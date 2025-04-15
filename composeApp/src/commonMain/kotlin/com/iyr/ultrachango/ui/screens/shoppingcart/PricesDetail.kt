package com.iyr.ultrachango.ui.screens.shoppingcart

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.iyr.ultrachango.data.Api.preciosclaros.model.producto.Sucursale
import com.iyr.ultrachango.utils.formatCurrency
import com.iyr.ultrachango.utils.ui.elements.Body1Text
import com.iyr.ultrachango.utils.ui.elements.H3Text

@Composable
fun PricesDetail(pos: Sucursale) {
    Column()
    {


        val havePromo =
            pos.preciosProducto.promo1?.precio?.toDoubleOrNull() != null || pos.preciosProducto.promo2?.precio?.toDoubleOrNull() != null

        var priceFirstLine: Double = 0.0
        var priceSecondLine: Double = 0.0
        var priceThirdLine: Double = 0.0

        if (havePromo) {
            try {
                priceFirstLine = pos.preciosProducto.getPrecioLista().toDouble()
            } catch (e: Exception) {
            }

            try {
                priceThirdLine = maxOf(
                    pos.preciosProducto.promo1?.precio?.toDoubleOrNull() ?: 0.0,
                    pos.preciosProducto.promo2?.precio?.toDoubleOrNull() ?: 0.0
                )
            } catch (e: Exception) {
            }
            try {
                priceThirdLine = minOf(
                    pos.preciosProducto.promo1?.precio?.toDoubleOrNull()
                        ?: Double.MAX_VALUE,
                    pos.preciosProducto.promo2?.precio?.toDoubleOrNull() ?: Double.MAX_VALUE
                )
                val pp = 3
            } catch (e: Exception) {
            }

        } else {
            priceThirdLine = pos.preciosProducto.getPrecioLista().toDouble()
        }


        if (priceFirstLine > 0.0) {
            Body1Text(
                text = formatCurrency(priceFirstLine ?: 0.0),
                fontWeight = if (!havePromo) FontWeight.Normal else FontWeight.ExtraBold,
            )


        }

        if (priceSecondLine > 0) {
            Body1Text(
                text = formatCurrency(priceSecondLine ?: 0.0),
                fontWeight = if (!havePromo) FontWeight.Normal else FontWeight.ExtraBold,
            )
        }

        if (priceThirdLine > 0) {
            H3Text(
                text = formatCurrency(priceThirdLine!!),
                fontWeight = FontWeight.ExtraBold,
                color = if (!havePromo) Color.Black else Color.Yellow,
                shadow = havePromo,
                shadowColor = Color.Black
            )
        }
    }
}
