package com.iyr.ultrachango.data.Api.preciosclaros.model.producto

import com.iyr.ultrachango.data.models.ProductPromo
import kotlinx.serialization.Serializable


@Serializable
data class Precio(
    private val precioLista: String? = null,
    val promo1: Promo?,
    val promo2: Promo?

) {
    fun getPrecioLista(): Double {
        return try {
            precioLista?.toDouble() ?: 0.0
        } catch (exception: Exception) {
            return 0.0
        }
    }

}
