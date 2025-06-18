package com.iyr.ultrachango.utils.geo

import androidx.compose.ui.text.intl.Locale
import com.iyr.ultrachango.utils.extensions.formatDigits
import dev.icerock.moko.permissions.PermissionsController
import dev.jordond.compass.Place
import dev.jordond.compass.geocoder.Geocoder
import dev.jordond.compass.geocoder.placeOrNull
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.StringFormat


suspend fun getCurrentLocation(
    permissionsController: PermissionsController,
): GeolocatorResult {
    val geolocator: Geolocator = Geolocator.mobile()
    val result: GeolocatorResult = geolocator.current()
    return result
}


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


fun Float.toTextDistance(decimals: Int = 2, locale: Locale = Locale.current): String {
    return if (this < 1000) {
        if (this % 1 == 0f) {
            "${this.toInt()} ${if (locale.language == "es") "metros" else "meters"}"
        } else {
            "${this.toInt()} ${if (locale.language == "es") "metros" else "meters"} "
                //.format(this)
        }
    } else {
        if ((this / 1000) % 1 == 0f) {
            "${(this / 1000).toInt()} ${if (locale.language == "es") "kilómetros" else "kilometers"}"
        } else {
            "${(this / 1000).formatDigits( decimals)} ${if (locale.language == "es") "kilómetros" else "kilometers"}"
                //.(this / 1000)


        }
    }
}


