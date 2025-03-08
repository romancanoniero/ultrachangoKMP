package com.iyr.ultrachango.data.database.repositories

import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.utils.auth_by_cursor.models.AppUser

interface UserRepository {
    suspend fun saveUser(user: AppUser)
    suspend fun getUser(userId: String): User?
  //  suspend fun updateUser(user: User)
    suspend fun updateUser(user: AppUser, image : ByteArray? = null)
  //  suspend fun syncUser(userId: String): User? // Obtiene los datos del servidor y los actualiza localmente
    suspend fun storeUser(user: AppUser)
}