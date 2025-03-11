package com.iyr.ultrachango.utils.auth_by_cursor.repository

import com.iyr.ultrachango.utils.auth_by_cursor.models.AppUser
import kotlinx.coroutines.flow.Flow

interface CurrentUserRepository {
    // Obtener datos del usuario autenticado
    suspend fun getCurrentUser(): Result<AppUser>

    // Observar cambios en el usuario autenticado
    fun observeCurrentUser(): Flow<AppUser?>

    // Actualizar datos del usuario
    suspend fun updateProfile(
        displayName: String? = null,
        photoUrl: String? = null,
        phoneNumber: String? = null
    ): Result<Unit>

    // Actualizar preferencias
    suspend fun updatePreferences(): Result<Unit>

    // Registrar token del dispositivo
    suspend fun registerDeviceToken(token: String): Result<Unit>

    // Sincronizar datos con el servidor
    suspend fun syncCurrentUser(): Result<Unit>

    // Limpiar datos locales al cerrar sesión
    suspend fun clearLocalData()
}