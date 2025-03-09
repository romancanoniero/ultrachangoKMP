@file:OptIn(ExperimentalForeignApi::class)

package com.iyr.ultrachango.utils.auth_by_cursor.auth

// shared/iosMain/domain/auth/GoogleSignInAuth.ios.kt
import cocoapods.GoogleSignIn.GIDConfiguration
import cocoapods.GoogleSignIn.GIDSignIn
import cocoapods.GoogleSignIn.*
import com.iyr.ultrachango.utils.uiHelper.UIHelper
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class GoogleSignInAuth private constructor(
    private val clientId: String
) {
    private val uiHelper = UIHelper()

    actual suspend fun signIn(): NativeAuthResult = suspendCancellableCoroutine { continuation ->
        try {
            val rootViewController = uiHelper.getRootController() as? UIViewController
                ?: throw Exception("No se pudo obtener el view controller principal")

            // Configurar Google Sign In
            GIDSignIn.sharedInstance.configuration = GIDConfiguration(clientID = clientId)

            GIDSignIn.sharedInstance.signInWithPresentingViewController(
                presentingViewController = rootViewController
            ) { result, error ->
                if (error != null) {

                    continuation.resume(NativeAuthResult.error(AuthError.Unknown(error.localizedDescription)))
                    return@signInWithPresentingViewController
                }

                val user = result?.user
                val idToken = user?.idToken?.tokenString

                if (idToken != null) {
                    continuation.resume(
                        NativeAuthResult( user as NativeUser
                            ,
                            error = null
                        )
                    )
                } else {
                    continuation.resume(
                        NativeAuthResult.error(AuthError.Unknown("No se pudo obtener el ID token"))
                    )
                }
            }
        } catch (e: Exception) {
            continuation.resume(NativeAuthResult.error(e))
        }
    }

    actual companion object {
        actual fun create(clientId: String): GoogleSignInAuth = GoogleSignInAuth(clientId)
    }
}