package com.iyr.ultrachango.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ProductPrice(
    val regularPrice: Double,
    val promo1: ProductPromo?,
    val promo2: ProductPromo?
) {
}



