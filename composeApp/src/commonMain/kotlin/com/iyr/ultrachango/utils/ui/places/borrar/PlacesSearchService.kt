package com.iyr.ultrachango.utils.ui.places.borrar

import com.iyr.ultrachango.utils.ui.places.google.model.PlacesResults
import com.iyr.ultrachango.utils.ui.places.models.CustomPlace

import com.iyr.ultrachango.utils.ui.places.models.toCustomPlace
import com.iyr.ultrachango.utils.ui.places.nominatim.model.NominatimModelItem
import com.iyr.ultrachango.utils.ui.places.toNominatim
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

import kotlinx.serialization.json.Json


class PlacesSearchService(
    private val client: HttpClient
) {

    private val BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json?"

    fun searchPlaces(query: String): Flow<PlacesResults> = callbackFlow<PlacesResults> {
        val url = StringBuilder()
            .append(BASE_URL)
            .append("address=").append(query)
            .append("&key=").append("AIzaSyBa9vlBC1KmTMdK_PiR821g4OIN4H-zhAA")
            .toString()

        val call = client.get(url)
        val payload = call.body<PlacesResults>()
        payload
    }

    fun searchPlacesNominatim(query: String): Flow<List<CustomPlace>> =
        callbackFlow  {

          //  val query1 = "Mexico 1117 ciudad de buenos aires"

            val url = query.toNominatim(nameDetails = 0, limit = 1)

            val call = client.get(url)
            val responseAsText = call.bodyAsText()
            val jsonSerializer = Json {
                ignoreUnknownKeys = true
                isLenient = false

            }

            val results = jsonSerializer.decodeFromString<List<NominatimModelItem>>(responseAsText)
            //  val payload = call.body<List<NominatimResults>>()

           val payload =  results.map { it -> it.toCustomPlace()}
            trySend(payload)

            awaitClose {
                this.cancel()
                close()
            }

        }


}
