package com.iyr.ultrachango.utils.ui.places.google.model

import kotlinx.serialization.Serializable


@Serializable
data class Location(
    val lat: Double,
    val lng: Double
)