package com.iyr.ultrachango.data.api.cloud.location

import com.iyr.ultrachango.data.models.FamilyMember
import com.iyr.ultrachango.data.models.Location

interface ICloudLocationsService {

    suspend fun save(location: Location): Location?
    suspend fun list(userKey: String): List<Location>
    suspend fun delete(userKey: String,locationID: Int)

}