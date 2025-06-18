package com.iyr.ultrachango.services

import com.iyr.ultrachango.data.database.repositories.UserLocationsRepository
import com.iyr.ultrachango.data.models.UserAddress
import com.iyr.ultrachango.data.models.Locations
import com.iyr.ultrachango.Constants
import com.iyr.ultrachango.authModule
import com.iyr.ultrachango.getUserLocally
import com.iyr.ultrachango.utils.coroutines.Resource
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
import com.ultrachango2.features.location.domain.model.LocationOption
import com.ultrachango2.features.location.domain.model.LocationType
import com.ultrachango2.features.location.domain.model.ReferenceLocation
import com.ultrachango2.features.location.domain.models.SystemOptionLocationRequestType

class LocationService(
    private val permissionsController: PermissionsController,
    private val userLocationsRepository: UserLocationsRepository,
    private val settings: Settings
) {


    /**
     * Get the reference location from the settings or returns the real location.
     * @return Pair of latitude and longitude or null if not found.
     */
    suspend fun getRefLocationOption(): LocationOption? {

        return try {

            var response: Pair<Double, Double>? = null
            val settings: Settings = Settings()
            var value = settings.getStringOrNull(Constants.CURRENT_LOCATION)
            if (value == null) {
                value = Constants.CURRENT_LOCATION
            }

            val selectedLocation = Json.decodeFromString<UserAddress>(value)
            when (selectedLocation.locationType) {
                Locations.CUSTOM -> {

                    LocationOption.StoredLocation(
                        location = ReferenceLocation(
                            id = selectedLocation.id.toString(),
                            name = selectedLocation.title.toString(),
                            address = "",
                            latitude = selectedLocation.latitude,
                            longitude = selectedLocation.longitude,
                            locationType = LocationType.CUSTOM
                        ), isSelected = true
                    )
                }

                Locations.CURRENT_LOCATION -> {
                    val locationResult = fetchRealTimeLocation()
                    locationResult?.let { it ->
                        val coordinates = it.coordinates

                        LocationOption.StoredLocation(
                            location = ReferenceLocation(
                                id = "",
                                name = "Ubicación actual",
                                address = "",
                                latitude = coordinates.latitude,
                                longitude = coordinates.longitude,
                                locationType = LocationType.CURRENT_LOCATION
                            ), isSelected = true
                        )
                    }
                }

                Locations.ENABLE_LOCATION -> LocationOption.SystemOption(type = SystemOptionLocationRequestType.LOCATION_SERVICES)
                Locations.LOCATION_ERROR -> LocationOption.LocationError(message = "")
                Locations.PERMISSION_REQUIRED -> LocationOption.SystemOption(type = SystemOptionLocationRequestType.LOCATION_PERMISSION)
                else -> LocationOption.LocationError(message = "Error desconocido")
            }
        } catch (e: Exception) {
            LocationOption.LocationError(message = "Error al obtener la ubicación : " + e.message.toString())
        }

    }


    suspend fun getCurrentLocationFlow(): Flow<UserAddress> = flow {
        when {
            !isGpsPresent() -> {
                emit(
                    UserAddress(
                        title = "GPS no disponible", locationType = Locations.LOCATION_ERROR
                    )
                )
            }

            !isGpsEnabled() -> {
                emit(
                    UserAddress(
                        title = "GPS desactivado", locationType = Locations.ENABLE_LOCATION
                    )
                )
            }

            !permissionsController.isPermissionGranted(Permission.LOCATION) -> {
                emit(
                    UserAddress(
                        title = "Permisos requeridos", locationType = Locations.PERMISSION_REQUIRED
                    )
                )
            }

            else -> {
                try {
                    val location = fetchRealTimeLocation()
                    emit(
                        UserAddress(
                            title = "Ubicación actual",
                            latitude = location.coordinates.latitude,
                            longitude = location.coordinates.longitude,
                            locationType = Locations.CURRENT_LOCATION
                        )
                    )
                } catch (e: Exception) {
                    emit(
                        UserAddress(
                            title = "Error de ubicación", locationType = Locations.LOCATION_ERROR
                        )
                    )
                }
            }
        }
    }


    private suspend fun getCurrentLocation(): Resource<UserAddress> {

        return when {
            !isGpsPresent() -> {
                Resource.Error<UserAddress>(message = "GPS no disponible")
            }

            !isGpsEnabled() -> {
                Resource.Error<UserAddress>(message = "GPS desactivado")
            }

            !permissionsController.isPermissionGranted(Permission.LOCATION) -> {
                Resource.Error<UserAddress>(message = "Permisos requeridos")
            }

            else -> {
                try {

                    val location = fetchRealTimeLocation()
                    Resource.Success<UserAddress>(
                        UserAddress(
                            title = "Ubicación actual",
                            latitude = location.coordinates.latitude,
                            longitude = location.coordinates.longitude,
                            locationType = Locations.CURRENT_LOCATION
                        )
                    )


                } catch (e: Exception) {
                    Resource.Error<UserAddress>(message = "Error de ubicación")
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

