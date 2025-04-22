package com.iyr.ultrachango.services

import com.iyr.ultrachango.data.database.repositories.UserLocationsRepository
import com.iyr.ultrachango.data.models.UserAddress
import com.iyr.ultrachango.data.models.Locations
import com.iyr.ultrachango.Constants
import com.russhwolf.settings.Settings
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import com.iyr.ultrachango.utils.isGpsEnabled
import com.iyr.ultrachango.utils.isGpsPresent

class LocationService(
    private val permissionsController: PermissionsController,
    private val userLocationsRepository: UserLocationsRepository,
    private val settings: Settings
) {
    suspend fun getCurrentLocation(): Flow<UserAddress> = flow {
        when {
            !isGpsPresent() -> {
                emit(UserAddress(
                    title = "GPS no disponible",
                    locationType = Locations.LOCATION_ERROR
                ))
            }
            !isGpsEnabled() -> {
                emit(UserAddress(
                    title = "GPS desactivado",
                    locationType = Locations.ENABLE_LOCATION
                ))
            }
            !permissionsController.isPermissionGranted(Permission.LOCATION) -> {
                emit(UserAddress(
                    title = "Permisos requeridos",
                    locationType = Locations.PERMISSION_REQUIRED
                ))
            }
            else -> {
                try {
                    val location = fetchRealTimeLocation()
                    emit(UserAddress(
                        title = "Ubicación actual",
                        latitude = location.coordinates.latitude,
                        longitude = location.coordinates.longitude,
                        locationType = Locations.CURRENT_LOCATION
                    ))
                } catch (e: Exception) {
                    emit(UserAddress(
                        title = "Error de ubicación",
                        locationType = Locations.LOCATION_ERROR
                    ))
                }
            }
        }
    }

    suspend fun saveLocation(location: UserAddress) {
        userLocationsRepository.save(location)
        if (location.locationType == Locations.CUSTOM) {
            settings.putString(Constants.CURRENT_LOCATION, Json.encodeToString(location))
        }
    }

    suspend fun getStoredLocations(userKey: String): List<UserAddress> {
        return userLocationsRepository.list(userKey)
    }

    suspend fun getLastSelectedLocation(): UserAddress? {
        return settings.getString(Constants.CURRENT_LOCATION, "")?.let {
            if (it.isEmpty()) null
            else Json.decodeFromString<UserAddress>(it)
        }
    }

    private suspend fun fetchRealTimeLocation(): dev.jordond.compass.Location {
        val geolocator = Geolocator.mobile()
        return when (val result = geolocator.current()) {
            is GeolocatorResult.Success -> result.data
            else -> throw Exception("No se pudo obtener la ubicación")
        }
    }

    suspend fun requestLocationPermission() {
        permissionsController.providePermission(Permission.LOCATION)
    }

    fun openAppSettings() {
        permissionsController.openAppSettings()
    }
}

