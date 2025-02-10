package com.iyr.ultrachango.utils.auth.google

// commonMain
interface GoogleAuthProvider {
    suspend fun signIn(): GoogleUser?
    suspend fun signOut()
}

data class GoogleUser(
    val idToken: String,
    val displayName: String,
    val profilePictureUrl: String?
)
