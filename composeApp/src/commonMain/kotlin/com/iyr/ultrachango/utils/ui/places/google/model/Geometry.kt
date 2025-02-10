package com.iyr.ultrachango.utils.ui.places.google.model


import kotlinx.serialization.Serializable


@Serializable
data class Geometry(
    val location: Location,
    val location_type: String,
    val viewport: Viewport
)