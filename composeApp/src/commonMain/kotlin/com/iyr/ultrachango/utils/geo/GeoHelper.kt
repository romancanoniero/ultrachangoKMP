package com.iyr.ultrachango.utils.geo

import dev.jordond.compass.Place
import dev.jordond.compass.geocoder.Geocoder
import dev.jordond.compass.geocoder.placeOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

suspend fun getPlaceFromCoordinates(lat: Double, lng: Double): Place? {
    val geocoder = Geocoder()
    return geocoder.placeOrNull(lat, lng)
}


fun getPlaceFromCoordinates(
    scope: CoroutineScope,
    lat: Double,
    lng: Double,
    onResult: (Place?) -> Unit
)
{
    val geocoder = Geocoder()
    scope.launch {
        onResult(geocoder.placeOrNull(lat, lng))
    }

}