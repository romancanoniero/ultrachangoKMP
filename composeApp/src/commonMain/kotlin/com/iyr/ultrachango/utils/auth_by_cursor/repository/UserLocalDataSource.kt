package com.iyr.ultrachango.utils.auth_by_cursor.repository

import com.iyr.ultrachango.utils.auth_by_cursor.models.AppUser
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    suspend fun getUser(userId: String): AppUser?
    fun observeUser(userId: String): Flow<AppUser?>
    suspend fun saveUser(user: AppUser)
    suspend fun deleteUser(userId: String)
    suspend fun clearAll()
}