package com.iyr.ultrachango.data.models

import com.iyr.ultrachango.data.Api.preciosclaros.model.producto.Promo

data class ProductForComparission
    (
    var id: String? = null,
    var marca: String? = null,
    var message: String? = null,
    var nombre: String? = null,
    var precioLista: Double? = null,
    var presentacion: String? = null,
    var promo1: Promo? = null,
    var promo2: Promo? = null,
    var precioMinimo: Double? = null,
    var precioMaximo: Double? = null
)
{
    var lowestPrice: Double
        get() {
            // si promo1 y promo2 en su campo precio son iguales a "". devuelve precioLista
            if (promo1?.precio == "" && promo2?.precio == "") {
                return precioLista ?: 0.0
            }
            // si propo1 o promo2 en su campo precion no son iguales a "", toma el mas bajo entre preciolista, promo1.precio y promo2.precio
            else if (promo1?.precio != "" || promo2?.precio != "") {
                val promo1Price = promo1?.precio?.toDoubleOrNull() ?: Double.MAX_VALUE
                val promo2Price = promo2?.precio?.toDoubleOrNull() ?: Double.MAX_VALUE
                val listPrice = precioLista ?: Double.MAX_VALUE
                return minOf(listPrice, promo1Price, promo2Price)
            }
            // si precioLista es null, devuelve 0.0
            else if (precioLista == null) {
                return 0.0
            }
            return 0.0
        }
        set(value) {}
}