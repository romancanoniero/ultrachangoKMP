package com.iyr.ultrachango.data.database.repositories

import com.iyr.ultrachango.services.LocationService
import dev.jordond.compass.Location

class LocationRepository(private val locationService: LocationService) {
    suspend fun getCurrentLocation(): Location? {
        return locationService.getCurrentLocation()
    }
}