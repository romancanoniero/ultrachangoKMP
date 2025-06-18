package com.iyr.ultrachango.data.preferences

interface UserPreferences {
    fun saveAuthToken(token: String)
    suspend fun getAuthToken(): String?
    suspend fun clearAuthToken()

    // Preferencias de autenticación
    fun saveLastLoginMethod(method: kotlin.String)
    fun saveLastLoginTimestamp(time: kotlin.Long)


    /*
       // ... otros métodos de preferencias
       // Datos de usuario
       suspend fun saveUserId(userId: String)
       suspend fun getUserId(): String?

       // Preferencias de autenticación
       suspend fun saveLastLoginMethod(method: String)
       suspend fun getLastLoginMethod(): String?
       suspend fun setRememberMe(enabled: Boolean)
       suspend fun isRememberMeEnabled(): Boolean

       // Datos de sesión
       suspend fun saveLastLoginTimestamp(timestamp: Long)
       suspend fun getLastLoginTimestamp(): Long?
       suspend fun saveSessionExpirationTime(timestamp: Long)
       suspend fun getSessionExpirationTime(): Long?

       // Preferencias de notificaciones
       suspend fun setNotificationsEnabled(enabled: Boolean)
       suspend fun areNotificationsEnabled(): Boolean

       // Preferencias de biometría
       suspend fun setBiometricAuthEnabled(enabled: Boolean)
       suspend fun isBiometricAuthEnabled(): Boolean

       // Preferencias de idioma
       suspend fun savePreferredLanguage(language: String)
       suspend fun getPreferredLanguage(): String?

       // Datos de configuración
       suspend fun saveThemePreference(isDarkMode: Boolean)
       suspend fun getThemePreference(): Boolean

       // Método para limpiar todas las preferencias
       suspend fun clearAllPreferences()

     */
}