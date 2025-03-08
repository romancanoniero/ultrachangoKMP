package com.iyr.ultrachango.utils.firebase

/*
import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseAuth.FIRAuthUIDelegateProtocol
import cocoapods.FirebaseAuth.FIRPhoneAuthProvider
import cocoapods.FirebaseAuth.FIRUser
import cocoapods.GoogleSignIn.GIDSignIn
 */
import kotlinx.coroutines.CoroutineScope
import kotlinx.cinterop.*

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UIKit.UIApplication

import platform.darwin.dispatch_semaphore_create

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseAuthRepository actual constructor() {

    actual fun getAuthToken(refresh: Boolean): String? {
        var token: String? = null
       /*
        val user = FIRAuth.auth()?.currentUser() ?: return null
        user.getIDTokenForcingRefresh(true, completion = { idToken, error ->
            if (error == null) {
                token = idToken?.toString()
                println("token = ${token}")
            } else {
                println("error obteniendo el token = ${error}")
            }
        })
       */

        return "token"
    }

    /*
    actual suspend fun signInWithEmail(
        email: String,
        password: String
    ): FirebaseAuthResult {
        TODO("Not yet implemented")
    }
    */

    actual suspend fun signInWithEmail(
        email: String, password: String
    ): AuthResult {
        return suspendCancellableCoroutine { continuation ->
            println("Ingreso con Email")
        /*
            FIRAuth.auth()?.let {

                it.signInWithEmail(email = email, password = password) { authResult, error ->
                    if (error != null) {
                        println("Algo Malo paso")

                        val response =
                            AuthResult.Error(error.localizedDescription ?: "Unknown error")

                        println("Hubo un error = ${error.localizedDescription}")
                        continuation.resume(response)

                    } else {

                        println("Todo bien - 1")
                        println(" 1  = ${authResult?.user()?.uid()}")
                        val user = authResult?.user()?.toAuthenticatedUser()
                        println(" 2  = ${Json.encodeToString(user)}")

                        val token = getAuthToken(false)

                        println("Usuario obtenido = ${user?.uid}")
                        println("Usuario  = ${Json.encodeToString(user)}")

                        println(Json.encodeToString(user))


                        val response = AuthResult.Success(
                            user = user!!, authToken = token
                        )
                        continuation.resume(response)

                    }
                }
                println("Termino")

            }
*/

        }
    }

    actual suspend fun signUpWithEmail(
        email: String, password: String
    ): AuthResult {
        return suspendCancellableCoroutine { continuation ->
 /*
            FIRAuth.auth()?.createUserWithEmail(email, password) { result, error ->
                if (error != null) {
//                    continuation.resumeWithException(Exception(error.localizedDescription))
                    continuation.resume(AuthResult.Error(error.localizedDescription))
                } else if (result != null) {

                    val authToken = getAuthToken(false)
                    val user = result.user()

                    val response = AuthResult.Success(
                        user = user.toAuthenticatedUser(), authToken = authToken
                    )
                    continuation.resume(response)
                } else {
                    //     continuation.resumeWithException(Exception("Unknown error occurred"))
                    continuation.resume(AuthResult.Error("Unknown error occurred"))
                }
            }
*/
        }

    }

    actual suspend fun signInWithGoogle(
        onResult: (AuthResult) -> Unit, scope: CoroutineScope
    ) {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController!!

        if (rootViewController == null) {
            onResult(AuthResult.Error("RootViewController is null"))
        } else {
      /*
            GIDSignIn.sharedInstance.signInWithPresentingViewController(rootViewController) { gidSignInResult, nsError ->
                nsError?.let { println("Error While signing: $nsError") }
                val idToken = gidSignInResult?.user?.idToken?.tokenString
                val profile = gidSignInResult?.user?.profile
                if (idToken != null) {

                    val authToken = idToken
                    val newUser = AuthenticatedUser()
                    newUser.uid = gidSignInResult?.user?.userID
                    newUser.displayName = profile?.name ?: ""
                    newUser.email = profile?.email
                    newUser.photoUrl = profile?.imageURLWithDimension(320u)?.absoluteString
                    newUser.firstName = profile?.givenName ?: ""
                    newUser.familyName = profile?.familyName ?: ""
                    println("** - User = ${Json.encodeToString(newUser)}")
                    onResult(AuthResult.Success(newUser, authToken))
                } else {
//                        continutation.resume(null)
                    onResult(AuthResult.Error("Error while signing in with Google"))

                }
            }
    */
        }
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
        onSuccess: (AuthResult) -> Unit,
        onFailure: (Exception) -> Unit,
        scope: CoroutineScope
    ) {
        val semaphore = dispatch_semaphore_create(0)
        var verificationId: String? = null
        var error: Exception? = null

        println("Validacion por telefono - 1")

        try {
            /*
            val provider = FIRPhoneAuthProvider.provider()

            println("Validacion por telefono - 2")
            println(phoneNumber)
            provider.verifyPhoneNumber(phoneNumber, null, completion = { verificationID, err ->
                println("Validacion por telefono - 3")
                if (err != null) {
                    println("Validacion por telefono - 4 - error")
                    error = Exception(err.localizedDescription)
                } else {
                    verificationId = verificationID
                    println("Validacion por telefono - 5 ${verificationID} ")
                }
                dispatch_semaphore_signal(semaphore)

            })
            println("Validacion por telefono - 6.........")
            dispatch_semaphore_wait(semaphore, DISPATCH_TIME_FOREVER)
            println("Validacion por telefono - 6 - semaphore wait completed")
            if (error != null) {
                println("Validacion por telefono - 7 - ${error}")
                onFailure(error!!)
            } else {
                println("Salio Bien??? pero no llego nada")
                // Save verificationId for later use
                settings.set("storedVerificationId", verificationId!!)
                onSuccess(AuthResult.Success(user = AuthenticatedUser(), authToken = null))
            }

             */
        } catch (ex: Exception) {
            println("Error en la validacion por telefono = ${ex}")
        }

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
        code: String, onSuccess: (FirebaseAuthResult) -> Unit, onFailure: (Exception) -> Unit
    ) {

// References Objective-C code that interacts with
        // a Swift Package Manager dependency to call the Crashlytics API.
        //  FIRCrashlytics.crashlytics().log("ddddd")
        //  val app = FIRApp.configure()


//FIRAuth.auth().signInWithCustomToken(token) { authResult, error in
//firebaseauth.FIRA


    }


}
/*
@OptIn(ExperimentalForeignApi::class)
private fun FIRUser.toAuthenticatedUser(): AuthenticatedUser {

    val newObject = AuthenticatedUser(this.uid())
    newObject.displayName = this.displayName()
    newObject.firstName = ""
    newObject.familyName = ""
    newObject.email = this.email()
    newObject.phoneNumber = this.phoneNumber()
    newObject.photoUrl = this.photoURL()?.absoluteString
    newObject.isAnonymous = this.isAnonymous()
    return newObject
}



 */