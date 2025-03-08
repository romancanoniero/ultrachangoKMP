package com.iyr.ultrachango.data.database.repositories


import com.iyr.ultrachango.data.api.cloud.users.CloudUsersService
import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.storeUserLocally
import com.iyr.ultrachango.utils.auth_by_cursor.models.AppUser
import com.russhwolf.settings.Settings


class UserRepositoryImpl(
    private val apiClient: CloudUsersService,
    private val settings : Settings,
) : UserRepository {


    override suspend fun getUser(userId: String): User? {
        return apiClient.getUser(userId)
    }

    override suspend fun saveUser(user: AppUser) {
        try {
            apiClient.saveUser(user) // Envía al servidor
            //      localStorage.saveUser(user) // Guarda localmente
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun updateUser(user: AppUser, image: ByteArray?) {
        try {
            apiClient.updateUser(user, image) // Actualiza en el servidor
            settings.storeUserLocally(user)
        }
        catch (exception: Exception) {
            throw exception
        }
        //    localStorage.saveUser(user) // Actualiza localmente
    }
    override suspend fun storeUser(user: AppUser) {
        settings.storeUserLocally(user)
    }
}


