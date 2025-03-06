package com.iyr.ultrachango.utils.auth_by_cursor.auth

sealed class AuthError {
    data class NetworkError(val message: String) : AuthError()
    data class InvalidCredentials(val message: String) : AuthError()
    data class UserNotFound(val message: String) : AuthError()
    data class WeakPassword(val message: String) : AuthError()
    data class EmailAlreadyInUse(val message: String) : AuthError()
    data class InvalidEmail(val message: String) : AuthError()
    data class InvalidVerificationCode(val message: String) : AuthError()
    data class Unknown(val message: String) : AuthError()

    companion object {
        fun fromException(e: Exception): AuthError {
            return when (e.message) {
                // Aquí puedes mapear los mensajes de error específicos de Firebase
                "ERROR_INVALID_EMAIL" -> InvalidEmail("El email no es válido")
                "ERROR_WRONG_PASSWORD" -> InvalidCredentials("Contraseña incorrecta")
                "ERROR_USER_NOT_FOUND" -> UserNotFound("Usuario no encontrado")
                "ERROR_WEAK_PASSWORD" -> WeakPassword("La contraseña es muy débil")
                "ERROR_EMAIL_ALREADY_IN_USE" -> EmailAlreadyInUse("El email ya está en uso")
                "ERROR_INVALID_VERIFICATION_CODE" -> InvalidVerificationCode("Código de verificación inválido")
                else -> Unknown(e.message ?: "Error desconocido")
            }
        }
    }
}