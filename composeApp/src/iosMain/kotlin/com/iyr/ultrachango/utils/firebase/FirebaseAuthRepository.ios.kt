package com.iyr.ultrachango.utils.firebase

import firebaseauth.FIRCrashlytics
import firebaseauth.FIRApp
import firebaseauth.*
import kotlinx.coroutines.CoroutineScope

import kotlinx.cinterop.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class FirebaseAuthRepository actual constructor() {

    actual  fun getAuthToken(refresh: Boolean): String?
    {
        var token : String?  = null
        return token
    }
    actual suspend fun signInWithEmail(
        email: String,
        password: String
    ): FirebaseAuthResult {
        TODO("Not yet implemented")
    }
    
    actual suspend fun signUpWithEmail(
        email: String,
        password: String
    ): FirebaseAuthResult {
        return suspendCancellableCoroutine { continuation ->


            /*
            FIRAuth.auth()?.createUserWithEmail(email, password) { result, error ->
                if (error != null) {
                    continuation.resumeWithException(Exception(error.localizedDescription))
                } else if (result != null) {
                    val user = result.user
                    continuation.resume(FirebaseAuthResult(
                        success = true,
                        user = user,
                        ))
                } else {
                    continuation.resumeWithException(Exception("Unknown error occurred"))
                }
            }
            */
        }

    }

    actual suspend fun signInWithGoogle(
        onResult: (FirebaseAuthResult) -> Unit,
        scope: CoroutineScope
    ) {
        TODO("Not yet implemented")
    }

    actual suspend fun signInWithFacebook() {
        TODO("Not yet implemented")
    }

    actual suspend fun signInWithApple() {
        TODO("Not yet implemented")
    }

    actual suspend fun signInWithInstagram() {
        TODO("Not yet implemented")
    }

    actual suspend fun signInWithPhone(
        phoneNumber: String,
        onSuccess: (FirebaseAuthResult) -> Unit ,
        onFailure: (Exception) -> Unit ,
        scope: CoroutineScope
    ) {
        TODO("Not yet implemented")
    }

    actual suspend fun signOut() {
    }


    actual var currentUser: AppFirebaseUser?
        get() = AppFirebaseUser()
        set(value) {}

    actual var auth: Auth
        get() = TODO("Not yet implemented")
        set(value) {}
    actual val currentUserId: String?
        get() = TODO("Not yet implemented")



    actual var isLoggedIn: Boolean
        get() = false
        set(value) {}



/*
    actual suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): FirebaseAuthResult {
        TODO("Not yet implemented")
    }
*/
    @OptIn(ExperimentalForeignApi::class)
    actual fun onOTPCodeEntered(
        code: String,
        onSuccess: (FirebaseAuthResult) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

// References Objective-C code that interacts with
    // a Swift Package Manager dependency to call the Crashlytics API.
    FIRCrashlytics.crashlytics().log("ddddd")
    val app = FIRApp.configure()


//FIRAuth.auth().signInWithCustomToken(token) { authResult, error in
//firebaseauth.FIRA


    }

}

