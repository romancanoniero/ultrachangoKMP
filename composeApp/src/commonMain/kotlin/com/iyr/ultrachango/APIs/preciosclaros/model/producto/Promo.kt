package com.iyr.ultrachango.data.Api.preciosclaros.model.producto

import com.iyr.ultrachango.data.models.ProductPromo
import kotlinx.serialization.Serializable


@Serializable
data class Promo(
    val descripcion: String,
    val precio: String
) {
    fun toProductPromo(): ProductPromo {
        return ProductPromo(
            description = descripcion,
            price = if (precio.isNotEmpty()) precio.toDouble() else 0.0
        )
    }
}