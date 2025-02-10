package com.iyr.ultrachango.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent


class AuthViewModel(private val authRepository: AuthRepositoryImpl)
    : ViewModel(), KoinComponent {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    init {
        // Verificar el estado de autenticación al inicializar
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        try {
            val userId = authRepository.getUserKey() // Obtener desde Settings
            _isLoggedIn.value = userId != null
        }
        catch (e: Exception) {
            _isLoggedIn.value = false
        }
    }

    fun isProfileComplete(): Boolean {
     return  false
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> get() = _authState.asStateFlow()

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val credential = GoogleAuthProvider.credential(idToken, null)
                Firebase.auth.signInWithCredential(credential)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An error occurred")
            }
        }
    }

}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}