package com.iyr.ultrachango.utils.auth_by_cursor.statemanagers

import com.iyr.ultrachango.utils.auth_by_cursor.models.AuthUser
import com.iyr.ultrachango.utils.auth_by_cursor.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


enum class AuthState {
    AUTHENTICATED,
    UNAUTHENTICATED,
    ONBOARDING,
    VERIFICATION_PENDING
}

class AuthStateManager(
    private val authRepository: AuthRepository
) {
    // Estado actual de autenticación
    fun getAuthState(): Flow<AuthState> =
        authRepository.getAuthState().map { user ->
            when {
                user != null -> AuthState.AUTHENTICATED
                // Aquí puedes añadir más lógica según tus necesidades
                // Por ejemplo, verificar si es la primera vez que se abre la app
                else -> AuthState.UNAUTHENTICATED
            }
        }

    // Verificar estado inicial
    fun checkInitialAuthState(): AuthState =
        if (authRepository.isUserSignedIn()) {
            AuthState.AUTHENTICATED
        } else {
            AuthState.UNAUTHENTICATED
        }

    // Obtener usuario actual
    fun getCurrentUser(): AuthUser? =
        authRepository.getCurrentUser()

    // Verificar si el usuario está autenticado
    fun isUserSignedIn(): Boolean =
        authRepository.isUserSignedIn()
}