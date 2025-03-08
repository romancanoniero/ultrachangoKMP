package com.iyr.ultrachango.utils.auth_by_cursor.auth



expect class GoogleAuthProvider {
    suspend fun getGoogleIdToken(): Result<String>
     suspend fun signIn(): Result<NativeUser>
    suspend fun signOut()
    fun getLastError(): String?

    companion object {
        fun getCredential(idToken: String, accessToken: String? = null): AuthCredential
    }
}