package com.iyr.ultrachango.utils.auth_by_cursor.ui

import com.iyr.ultrachango.utils.auth_by_cursor.models.AppUser


sealed class AuthState {
    data object Initial : AuthState()
    data object Loading : AuthState()
    data class Success(val user: AppUser) : AuthState()
    data class Error(
        val message: String,
        val type: AuthErrorType,
        val retryAction: (() -> Unit)? = null
    ) : AuthState()
    data class PhoneVerificationSent(
        val verificationId: String,
        val phoneNumber: String
    ) : AuthState()
}

enum class AuthErrorType {
    INVALID_EMAIL,
    INVALID_PASSWORD,
    EMAIL_ALREADY_IN_USE,
    USER_NOT_FOUND,
    WRONG_PASSWORD,
    NETWORK_ERROR,
    PHONE_VERIFICATION_FAILED,
    GOOGLE_SIGN_IN_FAILED,
    FACEBOOK_SIGN_IN_FAILED,
    APPLE_SIGN_IN_FAILED,
    UNKNOWN
}