package com.iyr.ultrachango.data.repository
/// src/commonMain/kotlin/com/iyr/ultrachango/data/repository/AuthRepositoryImpl.kt

import com.iyr.ultrachango.domain.auth.AuthRepository
import com.iyr.ultrachango.domain.auth.models.AppUser
import com.iyr.fbauthentication.platform.FirebaseAuthPlatform
import com.iyr.fbauthentication.common.AuthResult
import com.iyr.fbauthentication.common.AuthUser
import com.iyr.ultrachango.data.api.cloud.auth.CloudAuthService
import com.iyr.ultrachango.data.preferences.UserPreferences
import com.iyr.ultrachango.domain.auth.exceptions.UserDataNotFoundException
import com.iyr.ultrachango.presentation.auth.AuthErrorType

import com.iyr.ultrachango.utils.coroutines.Resource
import com.iyr.ultrachango.utils.coroutines.Resource.*

class AuthRepositoryImpl(
    private val authLibrary: FirebaseAuthPlatform,
    private val authService: CloudAuthService,
    private val userPreferences: UserPreferences
) : AuthRepository {

    private fun AuthUser.toAppUser(): AppUser {
        return AppUser(
            uid = this.uid,
            email = this.email,
            displayName = this.displayName,
            profilePicturePath = this.photoUrl,
            isEmailVerified = this.isEmailVerified,
            providerId = this.providerId
        )
    }

    private var userKey: String? = null


    suspend fun handleAuthResult(result: AuthResult<AuthUser?>): Resource<AppUser> {
        return when (result) {
            is AuthResult.Success -> {
                val appUser = result.data?.toAppUser()
                this.userKey = appUser?.uid.toString()

                try {
                    result.data?.let { authUser ->
                        val userFromServer = authService.getAuthenticatedUser(authUser.uid)
                        if (userFromServer != null) {
                            // Usuario encontrado en el servidor
                            result.token?.let { token ->
                                userPreferences.saveAuthToken(token)
                            }
                            Success(userFromServer)
                        } else {
                            // Usuario autenticado pero sin datos de perfil
                            // Retornar Resource.Error con mensaje específico para que AuthViewModel maneje la redirección
                            val newUser = AppUser(
                                uid = authUser.uid,
                                email = authUser.email ?: "",
                                displayName = authUser.displayName ?: "",
                                profilePicturePath = authUser.photoUrl ?: "",
                                isEmailVerified = authUser.isEmailVerified,
                                providerId = authUser.providerId,
                                phoneNumber = authUser.phoneNumber,


                                )

                            Success(newUser)
                            /*
                            Error(
                                "Usuario autenticado pero requiere completar perfil",
                                exception = UserDataNotFoundException(),
                                errorType = AuthErrorType.USER_DATA_NOT_FOUND
                            )
                            */
                        }
                    } ?: run {
                        Error("Problemas en la autenticación")
                    }
                } catch (e: Exception) {
                    Error(
                        "Error al obtener el usuario: ${e.message}",
                        exception = e,
                        errorType = AuthErrorType.UNKNOWN
                    )

                }
            }

            is AuthResult.Error -> Error(result.message)
            AuthResult.Loading -> Loading()
        }
    }

    override fun getUserKey(): String {
        return userKey.toString()
    }

    override fun isUserSignedIn(): Boolean {
        return authLibrary.isUserSignedIn()
    }

    override suspend fun signInWithEmail(email: String, password: String): Resource<AppUser> {
        return handleAuthResult(authLibrary.signInWithEmailAndPassword(email, password))
    }

    override suspend fun signInWithGoogle(): Resource<AppUser> {
        //val result = authService.launchCredentialManager()

        return handleAuthResult(authLibrary.launchCredentialManager())
    }

    override suspend fun signInWithFacebook(accessToken: String): Resource<AppUser> {
        return handleAuthResult(authLibrary.signInWithFacebook(accessToken))
    }

    override suspend fun signInWithApple(idToken: String, nonce: String?): Resource<AppUser> {
        return handleAuthResult(authLibrary.signInWithApple(idToken))
    }

    override suspend fun signInWithTwitter(token: String, secret: String): Resource<AppUser> {
        return Resource.Error("Not implemented")//handleAuthResult(authService.signInWithTwitter(token, secret))
    }


    /**
     * Sign in with phone number using verification ID and code.
     */
    override suspend fun signInWithPhone(phoneNumber: String): Resource<String?> {
        val result = authLibrary.signInWithPhone(phoneNumber)
        return when (result) {
            is AuthResult.Success -> {
                val verificationId = result.data
                return Success(verificationId)
            }

            is AuthResult.Error -> return Error(result.message)
            AuthResult.Loading -> return Loading()
        }
    }


    override suspend fun verifyPhoneNumber(
        verificationId: String,
        code: String
    ): Resource<AppUser> {
        val result: AuthResult<AuthUser?> = authLibrary.verifyPhoneCode(verificationId, code)
        // Ahora converge en handleAuthResult como los otros métodos

        return handleAuthResult(result)

        //try {


        /*
         } catch (e: Exception) {
             Resource.Error(e.message ?: "Error al verificar el número de teléfono")
         }
         */
    }

    override suspend fun createUserWithEmail(email: String, password: String): Resource<AppUser> {
        return handleAuthResult(authLibrary.createUserWithEmailAndPassword(email, password))
    }

    override suspend fun signOut(): Resource<Unit> {
        return try {
            authLibrary.signOut()
            userPreferences.clearAuthToken()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al cerrar sesión")
        }
    }

    override fun getCurrentUser(): AppUser? {
        return authLibrary.getCurrentUser()?.toAppUser()
    }


    override suspend fun getAuthToken(forceRefresh: Boolean): String? {
        return authLibrary.getAuthToken()

    }

    override suspend fun sendPasswordResetEmail(email: String): Resource<Unit> {
        return try {
            authLibrary.sendPasswordResetEmail(email)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al enviar el email de recuperación")
        }
    }

    override suspend fun confirmPasswordReset(code: String, newPassword: String): Resource<Unit> {
        return try {
            authLibrary.confirmPasswordReset(code, newPassword)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al restablecer la contraseña")
        }
    }

    override suspend fun updateProfile(
        user: AppUser,
        bytes: ByteArray?
    ): Resource<Unit> {
        return try {
            authService.updateUser(user, bytes)
            Resource.Success<Unit>()
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al actualizar el perfil del usuario")
        }
    }
}