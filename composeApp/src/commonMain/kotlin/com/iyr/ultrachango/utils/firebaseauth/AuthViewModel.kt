package com.iyr.ultrachango.utils.firebaseauth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(private val authHelper: FirebaseAuthHelper) : ViewModel() {

    var isAuthenticated = false
        private set

    fun signIn(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val result = authHelper.signInWithEmail(email, password)
                onResult(true)

            }
            catch (exception: Exception) {
                isAuthenticated = false
                onResult(false)
                println(exception.message)
            }
        }
    }

    fun signUp(email: String, password: String, onResult: (Boolean) -> Unit) {
        println("Email: $email, Password: $password")

        viewModelScope.launch {
            try {
                val result = authHelper.signUpWithEmailAndPassword(email, password)
                println("SignUo result: $result")
                onResult(true)
            }
            catch (exception: Exception) {
                isAuthenticated = false
                onResult(false)
                println(exception.message)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authHelper.signOut()
            isAuthenticated = false
        }
    }
}