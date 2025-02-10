package com.iyr.ultrachango.utils.ui.places.google.model

import kotlinx.serialization.Serializable


@Serializable
data class Result(
    var address_components: List<AddressComponent>?,
    val formatted_address: String?,
    val geometry: Geometry?,
    val place_id: String?,
    val plus_code:  PlusCode?,
    val types: List<String>?
)