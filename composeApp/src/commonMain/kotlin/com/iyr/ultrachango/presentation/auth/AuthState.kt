package com.iyr.ultrachango.presentation.auth

import com.iyr.ultrachango.domain.auth.models.AppUser

// src/commonMain/kotlin/com/iyr/ultrachango/presentation/auth/AuthState.kt

sealed class AuthState {
    data object Initial : AuthState()
    data object Loading : AuthState()
    data class Success(
        val user: AppUser,
        val loading: Boolean = false,
        val showErrorMessage: Boolean = false
    ) : AuthState()

    data class PhoneVerificationSent(
        val verificationId: String,
        val phoneNumber: String
    ) : AuthState()

    data class Error(
        val message: String,
        val type: AuthErrorType
    ) : AuthState()
}

// Nuevo sistema de eventos centralizados
sealed class AuthEvent {
    data object ShowLoading : AuthEvent()
    data object HideLoading : AuthEvent()
    
    data class ShowError(
        val message: String,
        val type: AuthErrorType,
        val isRetrying: Boolean = false,
        val retryAttempt: Int = 0
    ) : AuthEvent()
    
    data object HideError : AuthEvent()
    
    data class ShowSuccess(
        val message: String
    ) : AuthEvent()
    
    data class ShowRetryDialog(
        val message: String,
        val onRetry: () -> Unit,
        val onCancel: () -> Unit
    ) : AuthEvent()
    
    // Evento específico para redirigir a completar perfil
    data class RequireProfileCompletion(
        val user: AppUser? = null
    ) : AuthEvent()
}

// Estados de carga específicos
data class LoadingState(
    val isLoading: Boolean = false,
    val message: String = "",
    val canCancel: Boolean = false
)

// Estados de error específicos
data class ErrorState(
    val hasError: Boolean = false,
    val message: String = "",
    val type: AuthErrorType = AuthErrorType.UNKNOWN,
    val isRetrying: Boolean = false,
    val retryAttempt: Int = 0,
    val maxRetries: Int = 3,
    val canRetry: Boolean = false
)

enum class AuthErrorType {
    NETWORK_ERROR,
    INVALID_CREDENTIALS,
    USER_NOT_FOUND,
    USER_DATA_NOT_FOUND, // Usuario autenticado pero sin datos de perfil
    WRONG_PASSWORD,
    EMAIL_ALREADY_IN_USE,
    INVALID_EMAIL,
    WEAK_PASSWORD,
    INVALID_PHONE_NUMBER,
    INVALID_VERIFICATION_CODE,
    GOOGLE_SIGN_IN_FAILED,
    FACEBOOK_SIGN_IN_FAILED,
    APPLE_SIGN_IN_FAILED,
    TWITTER_SIGN_IN_FAILED,
    PHONE_VERIFICATION_FAILED,
    BACKEND_ERROR,
    TIMEOUT_ERROR,
    UNKNOWN
}