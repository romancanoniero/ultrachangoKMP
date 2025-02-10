package com.iyr.ultrachango.data.database.repositories

import com.iyr.ultrachango.data.api.cloud.location.CloudLocationsService

import com.iyr.ultrachango.data.models.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class UserLocationsRepository(
  //  private val locationsListDao: ProductsDao,
    private val locationsCloudService: CloudLocationsService,
) {


    suspend fun save(location: Location) {
        try {
            val stringaso = Json.encodeToString(location)
            locationsCloudService.save(location)?.let { result ->
                val pp = 3
            }

        } catch (e: Exception) {
            // si falla la nube, elimino el registro local
            //   shoppingListDao.deleteShoppingList(shoppingList.id)
            throw e
        }
    }


    fun fetchLists(userKey: String): Flow<List<Location>> = flow {
        val cloudCall = locationsCloudService.list(userKey)
        emit(cloudCall)
    }


    suspend fun list(userKey: String): List<Location>  {
        val cloudCall =ArrayList(locationsCloudService.list(userKey))
     /*
        val currentLocation = Location()
        currentLocation.title = "Current Location"
        currentLocation.locationType = Locations.CURRENT_LOCATION
        cloudCall.add(0, currentLocation)
*/

        return cloudCall
    }


    suspend fun delete(userKey: String, locationID: Int) {
        try {
            locationsCloudService.delete(userKey, locationID)
        } catch (e: Exception) {
            throw e
        }

    }

}