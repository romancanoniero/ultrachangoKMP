package com.iyr.ultrachango.utils.firebase

import AppContext
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.iyr.ultrachango.MainActivity

import com.iyr.ultrachango.auth.AuthenticatedUser
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class GoogleAuthAndroid(
    private val activity: Activity,
    //  private val webClientId: String,
    //  private val signInLauncher: ActivityResultLauncher<IntentSenderRequest>,
    private val onSuccess: (FirebaseAuthResult) -> Unit,
    private val onFailure: (Exception) -> Unit,
    private val signInLauncher: ActivityResultLauncher<IntentSenderRequest>,
    private val webClientId: String
) : GoogleAuth {


//------------------------------------


    //-----------------------------------------
    private lateinit var pendient: CancellableContinuation<FirebaseAuthResult>
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val oneTapClient: SignInClient = Identity.getSignInClient(activity)

    /*
        val signInLauncher1 = (AppContext.activity as MainActivity).registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                GlobalScope.launch {
                    val firebaseUser =
                        (this@GoogleAuthAndroid as? GoogleAuthAndroid)?.handleSignInResult(result.data)

                    //     authenticatedUser = firebaseUser.toAuthenticatedUser()
                    //         pendient.resume(MyAuthResult(true, "", ""))

                }

            } else {
                Log.e("GoogleAuth", "Error en resultado de Sign-In")
                pendient.resume(FirebaseAuthResult(true, "", ""))

            }
        }
    */

    private var signInCallback: ((Boolean, String?) -> Unit)? = null


    init {



    }


    override suspend fun signIn(
        onResult: (FirebaseAuthResult) -> Unit,
        scope: CoroutineScope
    )  {

        AppContext.handlerSignInResult = { data: Intent? ->
        //---
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken

                if (idToken != null) {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

                    auth.signInWithCredential(firebaseCredential).addOnCompleteListener { task ->

                        val result = FirebaseAuthResult(
                            success = task.isSuccessful,
                            user = task.result.user?.toAppFirebaseUser(),
                            authToken = FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.result?.token,
                            errorMessage = task.exception?.localizedMessage
                        )
                        onResult(result)

                    }
                } else {
                    val result = FirebaseAuthResult(
                        success = false,
                        user = null,
                        errorMessage = "ID Token no encontrado"
                    )
                    onResult(result)
                    Log.e("GoogleAuth", "ID Token no encontrado")
                }
            } catch (e: Exception) {
                Log.e("GoogleAuth", "Error en handleSignInResult: ${e.localizedMessage}")
                val result = FirebaseAuthResult(
                    success = false,
                    user = null,
                    errorMessage = "Error en handleSignInResult: ${e.localizedMessage}"
                )
                onResult(result)
            }


            //----
        }

        val signInRequest = BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                .setServerClientId(webClientId).setFilterByAuthorizedAccounts(false).build()
        ).build()

        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener { result ->
            val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
            signInLauncher.launch(intentSenderRequest)
        }.addOnFailureListener { e ->
            Log.e("GoogleAuth", "Error en Sign-In: ${e.localizedMessage}")
        }
    }

    suspend fun handleSignInResult(data: Intent?): AuthenticatedUser? {
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken

            if (idToken != null) {
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

                auth.signInWithCredential(firebaseCredential).addOnCompleteListener { task ->

                    val result = FirebaseAuthResult(
                        success = task.isSuccessful,
                        user = task.result.user?.toAppFirebaseUser(),
                        errorMessage = task.exception?.localizedMessage
                    )
                    onSuccess(result)

                }
            } else {
                val result = FirebaseAuthResult(
                    success = false,
                    user = null,
                    errorMessage = "ID Token no encontrado"
                )
                onSuccess(result)
                Log.e("GoogleAuth", "ID Token no encontrado")
            }
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Error en handleSignInResult: ${e.localizedMessage}")
            val result = FirebaseAuthResult(
                success = false,
                user = null,
                errorMessage = "Error en handleSignInResult: ${e.localizedMessage}"
            )

        }
        return null
    }

}

// Implementación `actual`
/*
actual fun getGoogleAuth(): GoogleAuth {
    return GoogleAuthAndroid(
        AppContext.activity,
        "1077576417175-8b3deus3foi11547ikbjr3plhoi52b6f.apps.googleusercontent.com",
        AppContext.signInLauncher
    )
}
*/
// Método para inicializar la autenticación de Google con un Activity
fun provideGoogleAuth(
    scope : CoroutineScope,
    activity: Activity,
    webClientId: String,
    signInLauncher: ActivityResultLauncher<IntentSenderRequest>,
    onSuccess: (FirebaseAuthResult) -> Unit,
    onFailure: (Exception) -> Unit
): GoogleAuth {

    lateinit var  googleAuthAndroid : GoogleAuthAndroid

    val onResult = { data : Intent? ->
        scope.launch {
            googleAuthAndroid.handleSignInResult(data)
        }
    }

    googleAuthAndroid = GoogleAuthAndroid(
        activity = activity,
        //       signInLauncher = signInLauncher,
        signInLauncher = signInLauncher,
        webClientId =webClientId,
        onSuccess = onSuccess,
        onFailure = onFailure
    )

    return googleAuthAndroid
}
/*
actual fun getGoogleAuth(): GoogleAuth {
    TODO("Not yet implemented")
}

 */