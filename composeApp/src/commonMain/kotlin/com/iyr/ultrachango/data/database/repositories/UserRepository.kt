package com.iyr.ultrachango.data.database.repositories

import com.iyr.ultrachango.auth.AuthenticatedUser
import com.iyr.ultrachango.data.models.User

interface UserRepository {
    suspend fun saveUser(user: AuthenticatedUser)
    suspend fun getUser(userId: String): User?
  //  suspend fun updateUser(user: User)
    suspend fun updateUser(user: AuthenticatedUser, image : ByteArray? = null)
  //  suspend fun syncUser(userId: String): User? // Obtiene los datos del servidor y los actualiza localmente
    suspend fun storeUser(user: AuthenticatedUser)
}