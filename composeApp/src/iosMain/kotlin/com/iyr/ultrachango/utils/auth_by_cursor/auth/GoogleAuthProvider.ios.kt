@file:OptIn(ExperimentalForeignApi::class)

package com.iyr.ultrachango.utils.auth_by_cursor.auth

import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseAuth.FIRGoogleAuthProvider
import cocoapods.FirebaseAuth.FIRUser
import cocoapods.GoogleSignIn.GIDSignIn
import kotlinx.cinterop.ExperimentalForeignApi

actual class GoogleAuthProvider @OptIn(ExperimentalForeignApi::class) constructor(
    private val signIn: GIDSignIn,
    private val auth: FIRAuth
)  {

    actual companion object {
    @OptIn(ExperimentalForeignApi::class)
    actual fun getCredential(idToken: String, accessToken: String?): AuthCredential =
        AuthCredential(FIRGoogleAuthProvider.credentialWithIDToken(idToken, accessToken!!))
}
    actual suspend fun signIn(): Result<NativeUser> {
        return try {
           // Arreglar esto
  /*
             val result = signIn.signIn()
            val credential = FIRGoogleAuthProvider.credentialWithIDToken(
                result?.authentication?.idToken,
                result?.authentication?.accessToken
            )

            val authResult = auth.signInWithCredential(credential, null)
            //  Result.success(authResult.user.toAuthUser())
            Result.success(NativeUser(FIRUser()))
*/

            Result.success(NativeUser(FIRUser()))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    actual suspend fun signOut() {
        signIn.signOut()
    }

    actual fun getLastError(): String? = null
    actual suspend fun getGoogleIdToken(): Result<String> {
        TODO("Not yet implemented")
    }
}