package com.iyr.ultrachango.data.Api.preciosclaros.model.producto

import kotlinx.serialization.Serializable

@Serializable
data class PreciosProducto(
    val precioLista: Double,
    val promo1: Promo,
    val promo2: Promo
)