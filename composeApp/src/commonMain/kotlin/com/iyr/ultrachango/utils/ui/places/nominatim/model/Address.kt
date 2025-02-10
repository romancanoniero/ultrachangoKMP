package com.iyr.ultrachango.utils.ui.places.nominatim.model

import kotlinx.serialization.Serializable


@Serializable
data class Address(
    val city: String,
    val country: String,
    val country_code: String,
    val house_number: String = "",
    val postcode: String,
    val road: String = "",
    val state: String,
    val state_district: String,
    val suburb: String
)