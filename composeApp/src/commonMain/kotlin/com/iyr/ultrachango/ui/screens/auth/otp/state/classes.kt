package com.iyr.ultrachango.ui.screens.auth.otp.state

import com.iyr.ultrachango.utils.auth_by_cursor.models.AppUser

sealed class OtpState {
    data object Initial : OtpState()
    data object Loading : OtpState()
    data class CodeSent(
        val verificationId: String,
        val phoneNumber: String
    ) : OtpState()
    data class Error(val message: String) : OtpState()
    data class Success(val user: AppUser) : OtpState()
}

// shared/commonMain/presentation/auth/state/OtpEvent.kt
sealed class OtpEvent {
    data class VerifyCode(val code: String) : OtpEvent()
    data object ResendCode : OtpEvent()
    data object NavigateBack : OtpEvent()
}