package com.iyr.ultrachango.utils.auth_by_cursor.auth

expect class FirebaseAuth() {
    fun getCurrentUser(): NativeUser?
    suspend fun signInWithEmailPassword(email: String, password: String): NativeAuthResult
    suspend fun createUserWithEmailPassword(email: String, password: String): NativeAuthResult
    suspend fun signOut()
    fun addAuthStateListener(listener: (NativeUser?) -> Unit): AuthStateListener
    fun removeAuthStateListener(listener: AuthStateListener)

    // Phone Auth
    suspend fun verifyPhoneNumber(phoneNumber: String): String
    suspend fun signInWithPhoneCredential(verificationId: String, code: String): NativeAuthResult

    // OAuth
    suspend fun signInWithCredential(credential: AuthCredential): NativeAuthResult
    suspend fun linkWithCredential(credential: AuthCredential): NativeAuthResult


}

/***
 * This is the common interface for the user object returned by the FirebaseAuth
 */
expect class NativeUser {
    val uid: String
    val email: String?
    val displayName: String?
    val photoUrl: String?
    val isEmailVerified: Boolean
    val providerId: String;


}

expect class AuthCredential

expect class AuthStateListener
/*
expect object GoogleAuthProvider {
    fun getCredential(idToken: String, accessToken: String? = null): AuthCredential
}
*/
expect object FacebookAuthProvider {
    fun getCredential(accessToken: String): AuthCredential
}

expect object AppleAuthProvider {
    fun getCredential(idToken: String, nonce: String? = null): AuthCredential
}

expect object TwitterAuthProvider {
    fun getCredential(token: String, secret: String): AuthCredential
}

expect object PhoneAuthProvider {
    fun getCredential(verificationId: String, code: String): AuthCredential
}

