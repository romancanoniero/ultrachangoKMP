package com.iyr.ultrachango.utils.auth_by_cursor.auth

// shared/androidMain/domain/auth/GoogleSignInAuth.android.kt
import AppContext
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class GoogleSignInAuth  constructor(
    private val activity: ComponentActivity,
    private val clientId: String
) {
    private var currentContinuation: CancellableContinuation<NativeAuthResult>? = null
    private val signInClient = GoogleSignIn.getClient(
        activity,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestEmail()
            .build()
    )

    private val launcher = activity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> handleSignInResult(result) }

    actual suspend fun signIn(): NativeAuthResult = suspendCancellableCoroutine { continuation ->
        try {
            currentContinuation = continuation
            launcher.launch(signInClient.signInIntent)

            continuation.invokeOnCancellation {
                currentContinuation = null
            }
        } catch (e: Exception) {
            continuation.resume(NativeAuthResult.error(e))
            currentContinuation = null
        }
    }

    private fun handleSignInResult(result: ActivityResult) {
        try {
            if (result.resultCode == -1) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken

                if (idToken != null) {
                    currentContinuation?.resume(


                        NativeAuthResult(task.result as NativeUser
                           ,
                            error = null
                        )
                    )
                } else {
                    currentContinuation?.resume(
                        NativeAuthResult.error(AuthError.Unknown("No se pudo obtener idToken"))
                    )
                }
            } else {
                currentContinuation?.resume(
                    NativeAuthResult.error(AuthError.Cancelled("El usuario canceló la operación"))
                )
            }
        } catch (e: Exception) {
            currentContinuation?.resume(NativeAuthResult.error(e))
        } finally {
            currentContinuation = null
        }
    }

    actual companion object {

        actual fun create(clientId: String): GoogleSignInAuth {
            val activity = AppContext.activity
                ?: throw IllegalStateException("No se encontró la Activity actual")
            return GoogleSignInAuth(activity as ComponentActivity, clientId)
        }
    }
}