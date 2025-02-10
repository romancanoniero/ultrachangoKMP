package com.iyr.ultrachango.data.models


import kotlinx.serialization.Serializable

enum class Locations {
    CUSTOM,
    CURRENT_LOCATION,
    ENABLE_LOCATION
}

@Serializable
//@Entity(tableName = "locations")
data class Location(
 //  @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String = "",
    var title: String = "",
    //  val icon: String? = "",
    val street: String = "",
    val number: String = "",
    val floor: String? = "",
    val apartment: String? = "",
    val city: String = "",
    val province: String? = "",
    val country: String = "",
    val postalCode: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    var locationType: Locations? = Locations.CUSTOM
) {
    override fun toString(): String {

        return "$street $number, $city"
    }
}

val sampleLocations = listOf(
    Location(
        title = "Central Park",
        //    icon = "https://example.com/icons/central_park.png",
        street = "5th Ave",
        number = "59",
        floor = "",
        apartment = "",
        city = "New York",
        province = "NY",
        country = "USA",
        postalCode = "10022",
        latitude = 40.785091,
        longitude = -73.968285
    ),
    Location(
        title = "Eiffel Tower",
        //    icon = "https://example.com/icons/eiffel_tower.png",
        street = "Champ de Mars",
        number = "5",
        floor = "",
        apartment = "",
        city = "Paris",
        province = "Île-de-France",
        country = "France",
        postalCode = "75007",
        latitude = 48.858844,
        longitude = 2.294351
    ),
    Location(
        title = "Colosseum",
        //    icon = "https://example.com/icons/colosseum.png",
        street = "Piazza del Colosseo",
        number = "1",
        floor = "",
        apartment = "",
        city = "Rome",
        province = "Lazio",
        country = "Italy",
        postalCode = "00184",
        latitude = 41.890251,
        longitude = 12.492373
    )
)