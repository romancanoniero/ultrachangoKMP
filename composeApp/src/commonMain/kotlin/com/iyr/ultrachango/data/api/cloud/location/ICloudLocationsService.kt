package com.iyr.ultrachango.data.api.cloud.location

import com.iyr.ultrachango.data.models.UserAddress

interface ICloudLocationsService {

    suspend fun save(UserAddress: UserAddress): UserAddress?
    suspend fun list(userKey: String): List<UserAddress>
    suspend fun delete(userKey: String,locationID: Int)

}