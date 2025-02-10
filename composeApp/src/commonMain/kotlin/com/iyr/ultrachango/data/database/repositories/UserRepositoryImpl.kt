package com.iyr.ultrachango.data.database.repositories

import com.iyr.ultrachango.auth.AuthRepositoryImpl
import com.iyr.ultrachango.data.api.cloud.location.CloudLocationsService
import com.iyr.ultrachango.data.api.cloud.users.CloudUsersService
import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.viewmodels.UserViewModel
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class UserRepositoryImpl(
    private val apiClient: CloudUsersService,
    private val settings : Settings,
) : UserRepository {


    override suspend fun getUser(userId: String): User? {
        return apiClient.getUser(userId)
    }

    override suspend fun saveUser(user: User) {
        try {
            apiClient.saveUser(user) // Envía al servidor
            //      localStorage.saveUser(user) // Guarda localmente
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun updateUser(user: User, image: ByteArray?) {
        try {
            apiClient.updateUser(user, image) // Actualiza en el servidor
            settings.storeUserLocally(user)
        }
        catch (exception: Exception) {
            throw exception
        }
        //    localStorage.saveUser(user) // Actualiza localmente
    }




    override suspend fun storeUser(user: User) {
        settings.storeUserLocally(user)
    }





}

fun Settings.storeUserLocally(user: User) {
    var EntityAsJson = Json.encodeToString(user)
    this.set("user", EntityAsJson);
}

fun Settings.getAuthToken(): String {
    return this.getStringOrNull("auth_token").toString()
}