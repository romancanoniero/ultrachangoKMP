package com.iyr.ultrachango.utils.ui.places.models

import com.iyr.ultrachango.data.models.Location
import com.iyr.ultrachango.utils.ui.places.nominatim.model.NominatimModelItem
import kotlinx.serialization.Serializable

data class CustomPlace(
    val address: CustomAddress,
    val addresstype: String,
    val boundingbox: List<String>,
    val display_name: String,
    val importance: Double,
    val lat: String,
    val lon: String,
    val name: String,
)
{
    override
    fun toString():String
    {
        return "${address.road} ${address.house_number}, ${address.suburb}, ${address.city}"
    }

}

@Serializable
data class CustomAddress(
    val city: String,
    val country: String,
    val country_code: String,
    val house_number: String = "",
    val postcode: String,
    val road: String = "",
    val state: String,
    val state_district: String,
    val suburb: String
)


fun NominatimModelItem.toCustomPlace():CustomPlace
{
    return  CustomPlace(
        address = CustomAddress(
            city = this.address.city,
            country = this.address.country,
            country_code = this.address.country_code,
            house_number = this.address.house_number,
            postcode = this.address.postcode,
            road = this.address.road,
            state = this.address.state,
            state_district = this.address.state_district,
            suburb = this.address.suburb
        ),
        addresstype = this.addresstype,
        boundingbox = this.boundingbox,
        display_name = this.display_name,
        importance = this.importance,
        lat = this.lat,
        lon = this.lon,
        name = this.name
    )
}


fun CustomPlace.toLocation(userKey: String, name: String): Location {
    return Location(
        userId = userKey,
        title = name,
        street = this.address.road,
        number = this.address.house_number,
        city = this.address.city,
        country = this.address.country,
        postalCode = this.address.postcode,
        latitude = this.lat.toDouble(),
        longitude = this.lon.toDouble()
     )
}
