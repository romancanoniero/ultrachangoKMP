package com.iyr.ultrachango.data.api.cloud.users

import com.iyr.ultrachango.data.models.FamilyMember
import com.iyr.ultrachango.data.models.User

interface ICloudUsersService  {

    suspend fun saveUser(user: User)
    suspend fun getUser(userId: String): User?
  //  suspend fun updateUser(user: User)
    suspend fun updateUser(user: User, image : ByteArray?=null)
    suspend fun syncUser(userId: String): User // Obtiene los datos del servidor y los actualiza localmente


}