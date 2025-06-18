package com.iyr.ultrachango.domain.auth

import com.iyr.fbauthentication.common.AuthUser
import com.iyr.ultrachango.domain.auth.models.AppUser
import com.iyr.ultrachango.utils.coroutines.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun getUserKey(): String
    fun isUserSignedIn(): Boolean
    suspend fun signInWithEmail(email: String, password: String): Resource<AppUser>
    suspend fun signInWithGoogle(): Resource<AppUser>
    suspend fun signInWithFacebook(accessToken: String): Resource<AppUser>
    suspend fun signInWithApple(idToken: String, nonce: String? = null): Resource<AppUser>
    suspend fun signInWithTwitter(token: String, secret: String): Resource<AppUser>
    suspend fun signInWithPhone(phoneNumber : String): Resource<String?>
    suspend fun verifyPhoneNumber(verificationId: String, code: String): Resource<AppUser>
    suspend fun createUserWithEmail(email: String, password: String): Resource<AppUser>
    suspend fun signOut(): Resource<Unit>
    fun getCurrentUser(): AppUser?
    suspend fun getAuthToken(forceRefresh: Boolean = false): String?
    suspend fun sendPasswordResetEmail(email: String): Resource<Unit>
    suspend fun confirmPasswordReset(code: String, newPassword: String): Resource<Unit>
    suspend fun updateProfile(user: AppUser, bytes: ByteArray?): Resource<Unit>
}