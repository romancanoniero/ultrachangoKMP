package com.iyr.ultrachango.utils.auth_by_cursor

import com.iyr.ultrachango.data.api.cloud.auth.CloudAuthService
import com.iyr.ultrachango.getUserLocally
import com.iyr.ultrachango.storeUserLocally
import com.iyr.ultrachango.utils.auth_by_cursor.auth.AppleAuthProvider
import com.iyr.ultrachango.utils.auth_by_cursor.auth.AuthError
import com.iyr.ultrachango.utils.auth_by_cursor.auth.FacebookAuthProvider
import com.iyr.ultrachango.utils.auth_by_cursor.auth.FirebaseAuth
import com.iyr.ultrachango.utils.auth_by_cursor.auth.GoogleAuthProvider
import com.iyr.ultrachango.utils.auth_by_cursor.auth.GoogleSignInAuth
import com.iyr.ultrachango.utils.auth_by_cursor.auth.NativeAuthResult
import com.iyr.ultrachango.utils.auth_by_cursor.auth.NativeUser
import com.iyr.ultrachango.utils.auth_by_cursor.auth.PhoneAuthProvider
import com.iyr.ultrachango.utils.auth_by_cursor.auth.TwitterAuthProvider
import com.iyr.ultrachango.utils.auth_by_cursor.models.AppUser
import com.iyr.ultrachango.utils.auth_by_cursor.models.AuthResult
import com.iyr.ultrachango.utils.auth_by_cursor.repository.AuthRepository
import com.mmk.kmpauth.google.GoogleUser
import com.russhwolf.settings.Settings
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

private fun NativeUser?.toAppUser(): AppUser {

    return AppUser(
        uid = this?.uid ?: "",
        email = this?.email,
        displayName = this?.displayName,
        profilePictureUrl = this?.photoUrl,
        isEmailVerified = this?.isEmailVerified == true,
    )


}

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val apiAuth: CloudAuthService,
    private val settings: Settings,

    ) : AuthRepository {

    private fun mapNativeUserToAppUser(nativeUser: NativeUser?): AppUser? {
        println("trace: mapNativeUserToAppUser")

        return nativeUser?.let {
            AppUser(
                uid = it.uid,
                email = it.email,
                displayName = it.displayName,
                profilePictureUrl = it.photoUrl,
                isEmailVerified = it.isEmailVerified,
                providerId = it.providerId,

                )
        }
    }


    private fun handleNativeResult(result: NativeAuthResult): AuthResult<AppUser> {
        println("trace: handleNativeResult")
        return when {
            //    result.error != null -> AuthResult.Error(error = result.error!!)

            result.user != null -> AuthResult.Success(mapNativeUserToAppUser(result.user)!!)
            else -> {

                AuthResult.Error(AuthError.fromException(Exception("Unknown error")))
            }
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): AuthResult<AppUser> =
        handleNativeResult(firebaseAuth.signInWithEmailPassword(email, password))

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): AuthResult<AppUser> =
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
    ): AuthResult<AppUser> =
        handleNativeResult(firebaseAuth.signInWithPhoneCredential(verificationId, code))

    override suspend fun signInWithGoogle(idToken: String): AuthResult<AppUser> {

        var pp = 22
        /*
        val signInResult = googleSignIn.signIn()
        signInResult.user

        return if (signInResult.user != null) {
            val credential = GoogleAuthProvider.getCredential(
                idToken = "signInResult.data",
                accessToken = null
            )
            handleNativeResult(firebaseAuth.signInWithCredential(credential))
        } else if (signInResult.error != null) {
            AuthResult.Error(signInResult.error)
        } else {
            AuthResult.Loading
        }
        */
        return AuthResult.Loading
    }

    override suspend fun signInWithGoogle(user: GoogleUser?): AuthResult<AppUser> {
       when(user)
       {
              null -> {
                return AuthResult.Error(AuthError.Unknown("No se pudo obtener idToken"))
              }
              else -> {
                val credential = GoogleAuthProvider.getCredential(
                     idToken = user.idToken,
                     accessToken = null
                )
                return handleNativeResult(firebaseAuth.signInWithCredential(credential))
              }
       }

    }

    override suspend fun signInWithFacebook(accessToken: String): AuthResult<AppUser> =
        handleNativeResult(
            firebaseAuth.signInWithCredential(
                FacebookAuthProvider.getCredential(accessToken)
            )
        )

    override suspend fun signInWithApple(
        idToken: String,
        nonce: String?
    ): AuthResult<AppUser> =
        handleNativeResult(
            firebaseAuth.signInWithCredential(
                AppleAuthProvider.getCredential(idToken, nonce)
            )
        )

    override suspend fun signInWithTwitter(
        token: String,
        secret: String
    ): AuthResult<AppUser> =
        handleNativeResult(
            firebaseAuth.signInWithCredential(
                TwitterAuthProvider.getCredential(token, secret)
            )
        )

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUser(): AppUser? {
        return settings.getUserLocally()
        // return mapNativeUserToAppUser(firebaseAuth.getCurrentUser())
    }

    override fun getUserKey(): String? = getCurrentUser()?.uid


    override fun isUserSignedIn(): Boolean =
        firebaseAuth.getCurrentUser() != null

    override fun getAuthState(): Flow<AppUser?> = callbackFlow {
        val listener = firebaseAuth.addAuthStateListener { user ->
            trySend(mapNativeUserToAppUser(user))
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
    ): AuthResult<AppUser> =
        handleNativeResult(
            firebaseAuth.linkWithCredential(
                PhoneAuthProvider.getCredential(verificationId, code)
            )
        )

    override suspend fun linkWithGoogle(idToken: String): AuthResult<AppUser> =
        handleNativeResult(
            firebaseAuth.linkWithCredential(
                GoogleAuthProvider.getCredential(idToken, null)
            )
        )

    override suspend fun linkWithFacebook(accessToken: String): AuthResult<AppUser> =
        handleNativeResult(
            firebaseAuth.linkWithCredential(
                FacebookAuthProvider.getCredential(accessToken)
            )
        )

    override suspend fun linkWithApple(
        idToken: String,
        nonce: String?
    ): AuthResult<AppUser> =
        handleNativeResult(
            firebaseAuth.linkWithCredential(
                AppleAuthProvider.getCredential(idToken, nonce)
            )
        )

    override suspend fun linkWithTwitter(
        token: String,
        secret: String
    ): AuthResult<AppUser> =
        handleNativeResult(
            firebaseAuth.linkWithCredential(
                TwitterAuthProvider.getCredential(token, secret)
            )
        )

    override suspend fun deleteAccount(): AuthResult<Unit> =
        TODO("Implement account deletion")


    override fun getAuthToken(refresh: Boolean): String? =
        try {
            val token = firebaseAuth.getCurrentUser()?.let { user ->
                firebaseAuth.getIdToken(refresh)

            } ?: throw Exception("Usuario no autenticado")

            token
        } catch (e: Exception) {
            throw e
        }




    override suspend fun getAuthTokenS(refresh: Boolean): AuthResult<String> =
        try {
            val token = firebaseAuth.getCurrentUser()?.let { user ->
                firebaseAuth.getIdToken(refresh)

            } ?: throw Exception("Usuario no autenticado")

            AuthResult.Success(token)
        } catch (e: Exception) {
            AuthResult.Error(AuthError.Unknown(e.message.toString()))
        }

    override fun storeAuthToken(
        token: String,
    ) {
        settings.putString("auth_token", token)
    }

    suspend fun fetchCurrentUser(
        forceRefresh: Boolean = false, callback: (user: AppUser?) -> Unit
    ) {
        val userKey = getUserKey()
        var result: AppUser? = null
        //       scope.launch(Dispatchers.IO) {
        if (!forceRefresh) {
            // Busca el usuario almacenado , si no esta almacenado lo busca en el servidor
            val userInSharedPrefs: AppUser? = settings.getUserLocally()
            userKey?.let {
                result = userInSharedPrefs
            }
            if (result != null) {
                //    _currentUser.value = result

            } else {
                userKey?.let {
                    try {
                        //              result = syncUser(userKey)
                        callback(result)

                    } catch (ex: Exception) {
                        result = userInSharedPrefs
                        callback(result)
                    }
                }
            }
        } else {
            userKey?.let {
                try {
                    result = syncUser(userKey)
                } catch (ex: Exception) {
                    result = null
                }
            }
        }

//            _currentUser.value = result
        result?.let {
            //  storeUser(it)
        }
        callback(result)


    }


    private suspend fun syncUser(userId: String): AppUser? {
        val userFromServer = apiAuth.getAuthenticatedUser(userId)
        userFromServer?.let { it ->
            settings.storeUserLocally(it)
        }
        return userFromServer
    }

}