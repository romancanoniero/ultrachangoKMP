package com.iyr.ultrachango.services


import dev.icerock.moko.permissions.PermissionsController
import dev.jordond.compass.Location
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.currentLocationOrNull
import dev.jordond.compass.geolocation.mobile


class LocationService(private val permissionsController: PermissionsController) {
    val geolocator: Geolocator = Geolocator.mobile()
    suspend fun getCurrentLocation(): Location? {
        return geolocator.currentLocationOrNull()
    }
}

