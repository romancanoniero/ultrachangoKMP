@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.iyr.ultrachango.utils.firebase


import com.iyr.ultrachango.utils.auth_by_cursor.models.AppUser

import kotlinx.coroutines.CoroutineScope


sealed class AuthResult {
    data class Success(val user: AppUser,
                       val authToken: String?) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

data class FirebaseAuthResult(
    val success: Boolean,
    val errorMessage: String? = null,
    val user: AppFirebaseUser? = null,
    val authToken: String? = null
) {
    fun toAuthenticatedUser(): AppUser? {


        val user = AppUser(
            uid = this.user?.uid ?: return null,
            displayName = this.user.displayName,
            email = this.user.email,
            phoneNumber = this.user.phoneNumber,
            profilePictureUrl = this.user.photoUrl,
        )
        return user
    }
}


class AppFirebaseUser {

    var uid: String? = null
    var providerId: String? = null
    var displayName: String? = null
    var phoneNumber: String? = null
    var email: String? = null
    var photoUrl: String? = null
    var isAnonymous: Boolean = false
    // var providerData: MutableList<out UserInfo>


    fun toAppUser(): AppUser {
        return AppUser(
            uid = this.uid ?: "",
            displayName = this.displayName,
            email = this.email,
            phoneNumber = this.phoneNumber,
            profilePictureUrl = this.photoUrl,

        )
    }
}


expect class FirebaseAuthRepository() {

    var auth: Auth
    val currentUserId: String?
    var isLoggedIn: Boolean


    suspend fun signUpWithEmail(email: String, password: String): AuthResult

    //  suspend fun signUpWithEmailAndPassword( email: String, password: String ): FirebaseAuthResult
    suspend fun signInWithEmail(email: String, password: String): AuthResult
    suspend fun signInWithPhone(
        phoneNumber: String,
        onSuccess: (AuthResult) -> Unit = {},
        onFailure: (Exception) -> Unit = {},
        scope: CoroutineScope
    )

    suspend fun signInWithGoogle(onResult: (AuthResult) -> Unit, scope: CoroutineScope)
    suspend fun signInWithFacebook()
    suspend fun signInWithApple()
    suspend fun signInWithInstagram()


    suspend fun signOut()


    fun getAuthToken(refresh: Boolean = false): String?

    var currentUser: AppFirebaseUser?


    fun onOTPCodeEntered(
        code: String,
        onSuccess: (FirebaseAuthResult) -> Unit,
        onFailure: (Exception) -> Unit
    )


}


class Auth {
    var currentUser: AppUser? = null
}

