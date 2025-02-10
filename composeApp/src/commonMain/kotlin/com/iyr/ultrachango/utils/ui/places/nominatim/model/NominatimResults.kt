package com.iyr.ultrachango.utils.ui.places.nominatim.model

import kotlinx.serialization.Serializable

@Serializable
data class NominatimResults (val results : List<NominatimModelItem>)