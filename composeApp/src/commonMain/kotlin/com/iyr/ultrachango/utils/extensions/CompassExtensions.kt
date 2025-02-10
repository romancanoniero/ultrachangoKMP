package com.iyr.ultrachango.utils.extensions

import dev.jordond.compass.Location


fun Location.toLocalLocation() : com.iyr.ultrachango.data.models.Location
{

    return com.iyr.ultrachango.data.models.Location(
        title = "Ubicacion Actual",
        latitude = this.coordinates.latitude,
        longitude = this.coordinates.longitude,
        locationType = com.iyr.ultrachango.data.models.Locations.CURRENT_LOCATION
    )
}