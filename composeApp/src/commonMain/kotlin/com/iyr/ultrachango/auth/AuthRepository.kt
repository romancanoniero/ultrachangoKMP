package com.iyr.ultrachango.auth

import com.iyr.ultrachango.data.models.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUserId: String
    val isAuthenticated: Boolean

  //  val currentUser: Flow<User?>

    suspend fun authenticate(email: String, password: String)
    suspend fun createUser(email: String, password: String)
    suspend fun createUser(phoneNumber: String)

    suspend fun signOut()

    fun isUserLoggedIn(): Boolean

    fun getAuthToken(): String?

    fun saveSession(userId: String,
                    token: String,
                    userName : String? = null)

    fun clearSession()

    fun getUserKey(): String?
  //  fun getUser(): Flow<User?>

    fun saveUser(user: User)
//    fun getUser(): User?
    fun storeUser(user: User)
  //  fun getCurrentUser(callback: (user: User?) -> Unit)
    suspend fun updateUser(user: User)
    fun getCurrentUser(): User?
}