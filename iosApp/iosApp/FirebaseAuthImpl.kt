package com.iyr.ultrachango.auth2


import cocoapods.FirebaseAuth.FIRAuth
import com.iyr.ultrachango.utils.shared.firebase.FirebaseAuth
import platform.Foundation.NSError

class FirebaseAuthImpl : FirebaseAuth {
    private val auth: FIRAuth = FIRAuth.auth()

    override fun signIn(email: String, password: String, onComplete: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password) { _, error: NSError? ->
            onComplete(error == null)
        }
    }
}