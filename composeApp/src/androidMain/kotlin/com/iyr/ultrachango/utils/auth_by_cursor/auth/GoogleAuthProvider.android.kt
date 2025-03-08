package com.iyr.ultrachango.utils.auth_by_cursor.auth

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.iyr.ultrachango.data.models.enums.AuthenticationMethods
import com.iyr.ultrachango.utils.auth_by_cursor.models.AppUser

import kotlinx.coroutines.tasks.await

actual class GoogleAuthProvider(
    private val googleSignInClient: GoogleSignInClient,
    private val firebaseAuth: FirebaseAuth
)  {

    actual companion object {
        actual fun getCredential(idToken: String, accessToken: String?): AuthCredential =
            AuthCredential(GoogleAuthProvider.getCredential(idToken, accessToken))
    }

    actual suspend fun getGoogleIdToken(): Result<String> {
        return try {
            val account = googleSignInClient.silentSignIn().await()
            Result.success(account.idToken!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    actual suspend fun signIn(): Result<NativeUser> {
        return try {
            val account = googleSignInClient.silentSignIn().await()
            val credential = getCredential(account.idToken!!, null)
            val authResult = firebaseAuth.signInWithCredential(credential)

            Result.success(authResult.user ?: throw Exception("Error al obtener usuario"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    actual suspend fun signOut() {
        googleSignInClient.signOut().await()
    }

    actual fun getLastError(): String? = null
}

private fun NativeUser?.toAppUser(): AppUser? {
return AppUser(
    uid = this?.uid ?: "",
    providerId = this?.providerId ?: "",
    email =  this?.email ?: "",
    displayName = this?.displayName ?: "",
    profilePictureUrl = this?.photoUrl ?: "",
    isEmailVerified = this?.isEmailVerified ?: false, )
}
