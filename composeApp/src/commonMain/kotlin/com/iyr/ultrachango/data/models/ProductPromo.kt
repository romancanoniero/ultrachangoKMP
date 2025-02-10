package com.iyr.ultrachango.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ProductPromo(
    val description: String,
    val price: Double
)
