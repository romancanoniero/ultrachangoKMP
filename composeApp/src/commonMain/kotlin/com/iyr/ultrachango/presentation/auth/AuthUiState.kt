package com.iyr.ultrachango.presentation.auth

import com.iyr.ultrachango.domain.auth.models.AppUser

sealed class AuthUiState {
    object Initial : AuthUiState()
    object Loading : AuthUiState()
    object NotAuthenticated : AuthUiState()
    object VerificationPending : AuthUiState()
    data class Authenticated(val user: AppUser) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}