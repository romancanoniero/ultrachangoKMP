package com.iyr.ultrachango.utils.auth_by_cursor.models

import com.iyr.ultrachango.utils.auth_by_cursor.auth.AuthError

sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>()

    data class Error(val error: AuthError) : AuthResult<Nothing>()
    object Loading : AuthResult<Nothing>()

    companion object {
        fun <T> error(exception: Exception): AuthResult<T> =
            Error(AuthError.fromException(exception))
    }
}


/*
data class AuthUser(
    val uid: String,
    val email: String?,
    val phoneNumber: String?,
    val displayName: String?,
    val photoUrl: String?,
    val isEmailVerified: Boolean,
    val providerId: String,

) {
    fun toAppUser(): AppUser {
        return AppUser(
            uid = uid,
            email = email,
            phoneNumber = phoneNumber,
            displayName = displayName,
            profilePictureUrl = photoUrl,
            isEmailVerified = isEmailVerified,
        )
    }
}

 */