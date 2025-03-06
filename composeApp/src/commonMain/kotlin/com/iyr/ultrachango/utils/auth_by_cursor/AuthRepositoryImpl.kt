package com.iyr.ultrachango.utils.auth_by_cursor

import com.iyr.ultrachango.utils.auth_by_cursor.auth.AppleAuthProvider
import com.iyr.ultrachango.utils.auth_by_cursor.auth.AuthError
import com.iyr.ultrachango.utils.auth_by_cursor.auth.FacebookAuthProvider
import com.iyr.ultrachango.utils.auth_by_cursor.auth.FirebaseAuth
import com.iyr.ultrachango.utils.auth_by_cursor.auth.GoogleAuthProvider
import com.iyr.ultrachango.utils.auth_by_cursor.auth.NativeAuthResult
import com.iyr.ultrachango.utils.auth_by_cursor.auth.NativeUser
import com.iyr.ultrachango.utils.auth_by_cursor.auth.PhoneAuthProvider
import com.iyr.ultrachango.utils.auth_by_cursor.auth.TwitterAuthProvider
import com.iyr.ultrachango.utils.auth_by_cursor.models.AuthResult
import com.iyr.ultrachango.utils.auth_by_cursor.models.AuthUser
import com.iyr.ultrachango.utils.auth_by_cursor.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    private fun mapNativeUserToAuthUser(nativeUser: NativeUser?): AuthUser? =
        nativeUser?.let {
            AuthUser(
                uid = it.uid,
                email = it.email,
                displayName = it.displayName,
                photoUrl = it.photoUrl,
                isEmailVerified = it.isEmailVerified,
                providerId = it.providerId
            )
        }

    private fun handleNativeResult(result: NativeAuthResult): AuthResult<AuthUser> =
        when {
        //    result.error != null -> AuthResult.Error(error = result.error!!)
            result.user != null -> AuthResult.Success(mapNativeUserToAuthUser(result.user)!!)
          else ->
          {

              AuthResult.Error(AuthError.fromException(Exception("Unknown error")))
          }
        }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): AuthResult<AuthUser> =
        handleNativeResult(firebaseAuth.signInWithEmailPassword(email, password))

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): AuthResult<AuthUser> =
        handleNativeResult(firebaseAuth.createUserWithEmailPassword(email, password))

    override suspend fun verifyPhoneNumber(phoneNumber: String): AuthResult<String> =
        try {
            val verificationId = firebaseAuth.verifyPhoneNumber(phoneNumber)
            if (verificationId.isNotEmpty()) {
                AuthResult.Success(verificationId)
            } else {
                AuthResult.Error(AuthError.Unknown("Error desconocido"))
            }
        } catch (e: Exception) {
            AuthResult.Error(AuthError.Unknown(e.message.toString()))
        }

    override suspend fun signInWithPhoneNumber(
        verificationId: String,
        code: String
    ): AuthResult<AuthUser> =
        handleNativeResult(firebaseAuth.signInWithPhoneCredential(verificationId, code))

    override suspend fun signInWithGoogle(idToken: String): AuthResult<AuthUser> =
        handleNativeResult(
            firebaseAuth.signInWithCredential(
                GoogleAuthProvider.getCredential(idToken, null)
            )
        )

    override suspend fun signInWithFacebook(accessToken: String): AuthResult<AuthUser> =
        handleNativeResult(
            firebaseAuth.signInWithCredential(
                FacebookAuthProvider.getCredential(accessToken)
            )
        )

    override suspend fun signInWithApple(
        idToken: String,
        nonce: String?
    ): AuthResult<AuthUser> =
        handleNativeResult(
            firebaseAuth.signInWithCredential(
                AppleAuthProvider.getCredential(idToken, nonce)
            )
        )

    override suspend fun signInWithTwitter(
        token: String,
        secret: String
    ): AuthResult<AuthUser> =
        handleNativeResult(
            firebaseAuth.signInWithCredential(
                TwitterAuthProvider.getCredential(token, secret)
            )
        )

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUser(): AuthUser? =
        mapNativeUserToAuthUser(firebaseAuth.getCurrentUser())

    override fun isUserSignedIn(): Boolean =
        firebaseAuth.getCurrentUser() != null

    override fun getAuthState(): Flow<AuthUser?> = callbackFlow {
        val listener = firebaseAuth.addAuthStateListener { user ->
            trySend(mapNativeUserToAuthUser(user))
        }
        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }

    override suspend fun sendEmailVerification(): AuthResult<Unit> =
        TODO("Implement email verification")

    override suspend fun updateProfile(
        displayName: String?,
        photoUrl: String?
    ): AuthResult<Unit> =
        TODO("Implement profile update")

    override suspend fun updateEmail(email: String): AuthResult<Unit> =
        TODO("Implement email update")

    override suspend fun updatePassword(password: String): AuthResult<Unit> =
        TODO("Implement password update")

    override suspend fun sendPasswordResetEmail(email: String): AuthResult<Unit> =
        TODO("Implement password reset")

    override suspend fun linkWithPhoneNumber(
        verificationId: String,
        code: String
    ): AuthResult<AuthUser> =
        handleNativeResult(
            firebaseAuth.linkWithCredential(
                PhoneAuthProvider.getCredential(verificationId, code)
            )
        )

    override suspend fun linkWithGoogle(idToken: String): AuthResult<AuthUser> =
        handleNativeResult(
            firebaseAuth.linkWithCredential(
                GoogleAuthProvider.getCredential(idToken, null)
            )
        )

    override suspend fun linkWithFacebook(accessToken: String): AuthResult<AuthUser> =
        handleNativeResult(
            firebaseAuth.linkWithCredential(
                FacebookAuthProvider.getCredential(accessToken)
            )
        )

    override suspend fun linkWithApple(
        idToken: String,
        nonce: String?
    ): AuthResult<AuthUser> =
        handleNativeResult(
            firebaseAuth.linkWithCredential(
                AppleAuthProvider.getCredential(idToken, nonce)
            )
        )

    override suspend fun linkWithTwitter(
        token: String,
        secret: String
    ): AuthResult<AuthUser> =
        handleNativeResult(
            firebaseAuth.linkWithCredential(
                TwitterAuthProvider.getCredential(token, secret)
            )
        )

    override suspend fun deleteAccount(): AuthResult<Unit> =
        TODO("Implement account deletion")
}