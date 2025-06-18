package com.iyr.ultrachango.data.preferences

import android.content.Context

actual class UserPreferencesImpl(
    private val context: Context
) : UserPreferences {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)


    actual override fun saveAuthToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    override suspend fun getAuthToken(): String? {
        return prefs.getString("auth_token", null)
    }

    override suspend fun clearAuthToken() {
        prefs.edit().remove("auth_token").apply()
    }

    override fun saveLastLoginMethod(method: String) {
        prefs.edit().putString("last_login_method", method).apply()
    }

    override fun saveLastLoginTimestamp(time: Long) {
        prefs.edit().putLong("last_login_timestamp", time).apply()
    }

    // ... otras implementaciones
}