package com.iyr.ultrachango.utils.auth_by_cursor.auth

// shared/commonMain/domain/auth/GoogleSignInAuth.kt
expect class GoogleSignInAuth {
    suspend fun signIn(): NativeAuthResult

    companion object {
        fun create(clientId: String): GoogleSignInAuth
    }
}