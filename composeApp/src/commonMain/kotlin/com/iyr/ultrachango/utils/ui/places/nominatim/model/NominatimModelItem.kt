package com.iyr.ultrachango.utils.ui.places.nominatim.model

import kotlinx.serialization.Serializable


@Serializable
data class NominatimModelItem(
    val address: Address,
    val addresstype: String,
    val boundingbox: List<String>,
    val `class`: String,
    val display_name: String,
    val importance: Double,
    val lat: String,
    val licence: String,
    val lon: String,
    val name: String,
    val namedetails: Namedetails? = null,
    val osm_id: Int,
    val osm_type: String,
    val place_id: Int,
    val place_rank: Int,
    val type: String
)