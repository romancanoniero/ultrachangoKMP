package com.iyr.ultrachango.utils.auth_by_cursor.repository

import com.iyr.ultrachango.utils.auth_by_cursor.models.AppUser
import com.iyr.ultrachango.utils.auth_by_cursor.models.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    // Email y contraseña
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult<AppUser>
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult<AppUser>
    suspend fun sendPasswordResetEmail(email: String): AuthResult<Unit>

    // Autenticación con teléfono
    suspend fun verifyPhoneNumber(phoneNumber: String): AuthResult<String> // Retorna el verificationId
    suspend fun signInWithPhoneNumber(verificationId: String, code: String): AuthResult<AppUser>

    // Autenticación con proveedores
    suspend fun signInWithGoogle(idToken: String): AuthResult<AppUser>
    suspend fun signInWithFacebook(accessToken: String): AuthResult<AppUser>
    suspend fun signInWithApple(idToken: String, nonce: String? = null): AuthResult<AppUser>
    suspend fun signInWithTwitter(token: String, secret: String): AuthResult<AppUser>

    // Gestión de sesión
    suspend fun signOut()
    fun getCurrentUser(): AppUser?
    fun getUserKey(): String?
    fun isUserSignedIn(): Boolean
    fun getAuthToken(refresh : Boolean): String?
    fun storeAuthToken( token: String,)





    // Estado de autenticación
    fun getAuthState(): Flow<AppUser?>

    // Verificación de email
    suspend fun sendEmailVerification(): AuthResult<Unit>

    // Actualización de perfil
    suspend fun updateProfile(displayName: String? = null, photoUrl: String? = null): AuthResult<Unit>
    suspend fun updateEmail(email: String): AuthResult<Unit>
    suspend fun updatePassword(password: String): AuthResult<Unit>

    // Vinculación de cuentas
    suspend fun linkWithPhoneNumber(verificationId: String, code: String): AuthResult<AppUser>
    suspend fun linkWithGoogle(idToken: String): AuthResult<AppUser>
    suspend fun linkWithFacebook(accessToken: String): AuthResult<AppUser>
    suspend fun linkWithApple(idToken: String, nonce: String? = null): AuthResult<AppUser>
    suspend fun linkWithTwitter(token: String, secret: String): AuthResult<AppUser>

    // Eliminación de cuenta
    suspend fun deleteAccount(): AuthResult<Unit>
}