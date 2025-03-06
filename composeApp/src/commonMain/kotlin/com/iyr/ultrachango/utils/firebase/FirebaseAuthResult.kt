package com.iyr.ultrachango.utils.firebase

import com.iyr.ultrachango.auth.AuthenticatedUser

/*
sealed class FirebaseAuthResult {
    data class Success(
        val user: AppFirebaseUser,
        val authToken: String?
    ) : FirebaseAuthResult() {
        fun toAuthenticatedUser(): AuthenticatedUser {
            return AuthenticatedUser(user.uid!!).apply {
                uid = user.uid
                displayName = user.displayName
                email = user.email
                phoneNumber = user.phoneNumber
                photoUrl = user.photoUrl
                isAnonymous = user.isAnonymous ?: false
            }
        }
    }

    data class Failure(val errorMessage: String) : FirebaseAuthResult()
}
*/