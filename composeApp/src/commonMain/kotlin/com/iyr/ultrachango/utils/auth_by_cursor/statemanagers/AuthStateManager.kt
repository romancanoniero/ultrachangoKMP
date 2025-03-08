package com.iyr.ultrachango.utils.auth_by_cursor.statemanagers


import com.iyr.ultrachango.utils.auth_by_cursor.AuthRepositoryImpl
import com.iyr.ultrachango.utils.auth_by_cursor.models.AppUser
import com.iyr.ultrachango.utils.auth_by_cursor.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


enum class AuthStates {
    AUTHENTICATED,
    UNAUTHENTICATED,
    ONBOARDING,
    VERIFICATION_PENDING
}

class AuthStateManager(
    private val authRepository: AuthRepository
) {
    // Estado actual de autenticación
    fun getAuthState(): Flow<AuthStates> =
        authRepository.getAuthState().map { user ->
            when {
                user != null -> AuthStates.AUTHENTICATED
                // Aquí puedes añadir más lógica según tus necesidades
                // Por ejemplo, verificar si es la primera vez que se abre la app
                else -> AuthStates.UNAUTHENTICATED
            }
        }

    // Verificar estado inicial
    fun checkInitialAuthState(): AuthStates =
        if (authRepository.isUserSignedIn()) {
            AuthStates.AUTHENTICATED
        } else {
            AuthStates.UNAUTHENTICATED
        }

    // Obtener usuario actual
    fun getCurrentUser(): AppUser? =
        authRepository.getCurrentUser()

    // Verificar si el usuario está autenticado
    fun isUserSignedIn(): Boolean =
        authRepository.isUserSignedIn()
}