package com.iyr.ultrachango.utils.ui.places.google.model

import kotlinx.serialization.Serializable


@Serializable
data class PlacesResults(
    val results: List<Result>,
    val status: String
)