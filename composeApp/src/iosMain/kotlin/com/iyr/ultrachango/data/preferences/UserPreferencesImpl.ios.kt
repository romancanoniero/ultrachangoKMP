package com.iyr.ultrachango.data.preferences

import platform.Foundation.NSUserDefaults

actual class UserPreferencesImpl : UserPreferences {
    private val userDefaults = NSUserDefaults.standardUserDefaults



   actual override fun saveAuthToken(token: String) {
        userDefaults.setObject(token, "auth_token")
    }

    override suspend fun getAuthToken(): String? {
        return userDefaults.objectForKey("auth_token") as? String
    }

    override suspend fun clearAuthToken() {
        userDefaults.removeObjectForKey("auth_token")
    }

    override fun saveLastLoginMethod(method: String) {
        userDefaults.setObject(method, "last_login_method")
    }

    override fun saveLastLoginTimestamp(time: Long) {
        userDefaults.setObject(time, "last_login_timestamp")
    }

    // ... otras implementaciones
}