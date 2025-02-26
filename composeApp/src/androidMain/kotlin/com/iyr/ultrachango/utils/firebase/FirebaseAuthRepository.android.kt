package com.iyr.ultrachango.utils.firebase

import AppContext
import AppContext.activity
import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.rememberCoroutineScope
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.iyr.ultrachango.MainActivity
import com.iyr.ultrachango.preferences.managers.settings
import com.russhwolf.settings.set
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit


actual class FirebaseAuthRepository actual constructor() {


    private val webClientId =
        "1077576417175-8b3deus3foi11547ikbjr3plhoi52b6f.apps.googleusercontent.com"

    actual var auth: Auth
        get() = TODO("Not yet implemented")
        set(value) {}

    actual var currentUser: AppFirebaseUser?
        get() = AppFirebaseUser()
        set(value) {}

    init {
        //     currentUser = FirebaseAuth.getInstance().currentUser?.toLocal()

    }


    actual val currentUserId: String?
        get() = FirebaseAuth.getInstance().uid


    actual var isLoggedIn: Boolean
        get() = FirebaseAuth.getInstance().currentUser != null
        set(value) {}


   actual  fun getAuthToken(refresh: Boolean): String?
   {
       var token : String?  = null
       runBlocking {
           token =  FirebaseAuth.getInstance().currentUser?.getIdToken(refresh)?.await()?.token
       }
      return token
   }

    actual suspend fun signInWithEmail(
        email: String,
        password: String
    ): FirebaseAuthResult {
        return try {
            val result =
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()

            var tokenCall = FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.await()
            var authToken : String?  = tokenCall?.token
            val user = result.user?.toAppFirebaseUser()
            FirebaseAuthResult(
                success = true,
                user = user,
                authToken = authToken!!
            )
        } catch (e: Exception) {
            FirebaseAuthResult(success = false, errorMessage = e.message)
        }

    }


    actual suspend fun signUpWithEmail(
        email: String,
        password: String
    ): FirebaseAuthResult = coroutineScope {
        return@coroutineScope async {
            try {
                val result =
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .await()
                val user = result.user?.toAppFirebaseUser()
                FirebaseAuthResult(success = true,
                    user = user,
                    authToken = FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.await()?.token!!)
            } catch (e: Exception) {
                FirebaseAuthResult(success = false, errorMessage = e.message)
            }
        }.await()
    }


    actual suspend fun signInWithGoogle(
        onResult: (FirebaseAuthResult) -> Unit,
        scope: CoroutineScope,
    ) {
        AppContext.googleAuth.signIn(
            scope = scope,
            onResult = { result ->
                onResult(result)
            }
        )
    }

    //: AuthResult
    actual suspend fun signInWithPhone(
        phoneNumber: String,
        onSuccess: (FirebaseAuthResult) -> Unit ,
        onFailure: (Exception) -> Unit ,
        scope: CoroutineScope) {



        val  callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.

                GlobalScope.launch {
                    signInWithPhoneAuthCredential(credential,
                        onResult= { result ->
                            onSuccess(result)
                        },
                    )
                }

            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }
                onFailure(e)
                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
            //    Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later


                settings.set("storedVerificationId", verificationId)
           //     settings.set("resendToken", token)

            }
        }



        val options = PhoneAuthOptions.newBuilder(AppContext.firebaseAuth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(AppContext.activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential,
                                                      onResult: (FirebaseAuthResult) -> Unit? = {},
                                                      scope: CoroutineScope? = null
                                                      ) {

        AppContext.firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(AppContext.activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                 //   Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    val result = FirebaseAuthResult(
                        success = true,
                        user = user?.toAppFirebaseUser(),
                        authToken = FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.result?.token,
                        errorMessage = null
                    )
                    onResult(result)
                } else {
                    // Sign in failed, display a message and update the UI
                   // Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    actual fun onOTPCodeEntered(code: String,
                                onSuccess: (FirebaseAuthResult) -> Unit,
                                onFailure: (Exception) -> Unit)
    {
        runBlocking {
            val storedVerificationId = settings.getString("storedVerificationId", "")
            val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
            signInWithPhoneAuthCredential(credential,
                onResult = { result ->
                    onSuccess(result)
                }
            )

        }

    }
    //: AuthResult
    actual suspend fun signInWithFacebook() {
        TODO("Not yet implemented")
    }

    //: AuthResult
    actual suspend fun signInWithApple() {
        TODO("Not yet implemented")
    }

    //    : AuthResult
    actual suspend fun signInWithInstagram() {
        TODO("Not yet implemented")
    }



    actual suspend fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
/*
    actual suspend fun signUpWithEmail(
        email: String,
        password: String
    ): FirebaseAuthResult {
        TODO("Not yet implemented")
    }
*/
    /*
    actual suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): FirebaseAuthResult {
        TODO("Not yet implemented")
    }
*/
}


fun FirebaseUser.toAppFirebaseUser(): AppFirebaseUser {
    var user = AppFirebaseUser()
    user.uid = this.uid
    user.email = this.email
    user.phoneNumber = this.phoneNumber
    user.isAnonymous = this.isAnonymous
    user.displayName = this.displayName
    user.photoUrl = this.photoUrl?.path
    user.providerId = this.providerId
    // user.providerData = this.providerData
    return user
}

/*
fun com.google.firebase.auth.FirebaseUser.toLocal(): AuthenticatedUser {
    var user = com.iyr.ultrachango.utils.firebase.AuthenticatedUser()
    user.uid = this.uid
    user.email = this.email
    user.phoneNumber = this.phoneNumber
    user.isAnonymous = this.isAnonymous
    user.displayName = this.displayName
    user.photoUrl = this.photoUrl?.path
    user.providerId = this.providerId
    // user.providerData = this.providerData
    return user
}
*/