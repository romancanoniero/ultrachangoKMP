package com.iyr.ultrachango.presentation.auth

sealed class PhoneAuthState {
    object Initial : PhoneAuthState()
    object Loading : PhoneAuthState()
    data class CodeSent(val verificationId: String) : PhoneAuthState()
    data class Error(val message: String) : PhoneAuthState()
}