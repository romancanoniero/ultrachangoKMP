package com.iyr.ultrachango.utils.auth_by_cursor.repository


import com.iyr.ultrachango.utils.auth_by_cursor.models.AuthResult
import com.iyr.ultrachango.utils.auth_by_cursor.models.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    // Email y contraseña
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult<AuthUser>
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult<AuthUser>
    suspend fun sendPasswordResetEmail(email: String): AuthResult<Unit>

    // Autenticación con teléfono
    suspend fun verifyPhoneNumber(phoneNumber: String): AuthResult<String> // Retorna el verificationId
    suspend fun signInWithPhoneNumber(verificationId: String, code: String): AuthResult<AuthUser>

    // Autenticación con proveedores
    suspend fun signInWithGoogle(idToken: String): AuthResult<AuthUser>
    suspend fun signInWithFacebook(accessToken: String): AuthResult<AuthUser>
    suspend fun signInWithApple(idToken: String, nonce: String? = null): AuthResult<AuthUser>
    suspend fun signInWithTwitter(token: String, secret: String): AuthResult<AuthUser>

    // Gestión de sesión
    suspend fun signOut()
    fun getCurrentUser(): AuthUser?
    fun isUserSignedIn(): Boolean

    // Estado de autenticación
    fun getAuthState(): Flow<AuthUser?>

    // Verificación de email
    suspend fun sendEmailVerification(): AuthResult<Unit>

    // Actualización de perfil
    suspend fun updateProfile(displayName: String? = null, photoUrl: String? = null): AuthResult<Unit>
    suspend fun updateEmail(email: String): AuthResult<Unit>
    suspend fun updatePassword(password: String): AuthResult<Unit>

    // Vinculación de cuentas
    suspend fun linkWithPhoneNumber(verificationId: String, code: String): AuthResult<AuthUser>
    suspend fun linkWithGoogle(idToken: String): AuthResult<AuthUser>
    suspend fun linkWithFacebook(accessToken: String): AuthResult<AuthUser>
    suspend fun linkWithApple(idToken: String, nonce: String? = null): AuthResult<AuthUser>
    suspend fun linkWithTwitter(token: String, secret: String): AuthResult<AuthUser>

    // Eliminación de cuenta
    suspend fun deleteAccount(): AuthResult<Unit>
}