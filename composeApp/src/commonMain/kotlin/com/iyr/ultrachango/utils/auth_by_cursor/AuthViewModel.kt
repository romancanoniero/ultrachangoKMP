package com.iyr.ultrachango.utils.auth_by_cursor

import com.iyr.ultrachango.utils.auth_by_cursor.models.AuthResult
import com.iyr.ultrachango.utils.auth_by_cursor.models.AuthUser
import com.iyr.ultrachango.utils.auth_by_cursor.repository.AuthRepository
import com.iyr.ultrachango.utils.auth_by_cursor.statemanagers.AuthState
import com.iyr.ultrachango.utils.auth_by_cursor.statemanagers.AuthStateManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val authState: AuthState = AuthState.UNAUTHENTICATED,
    val user: AuthUser? = null,
    val error: String? = null,
    val verificationId: String? = null
)


class AuthViewModel(
    private val authRepository: AuthRepository,
    private val authStateManager: AuthStateManager
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        // Verificar estado inicial de autenticación
        checkInitialAuthState()
        // Observar cambios en el estado de autenticación
        observeAuthState()
    }

    private fun checkInitialAuthState() {
        val initialState = authStateManager.checkInitialAuthState()
        _uiState.update { it.copy(
            authState = initialState,
            user = authStateManager.getCurrentUser()
        )}
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authStateManager.getAuthState().collect { authState ->
                _uiState.update { it.copy(
                    authState = authState,
                    user = authStateManager.getCurrentUser()
                )}
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = authRepository.signInWithEmailAndPassword(email, password)) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        user = result.data,
                        authState = AuthState.AUTHENTICATED
                    )}
                }
                is AuthResult.Error -> {

                    _uiState.update { it.copy(
                        isLoading = false,
                        error = result.error.toString()
                    )}
                }
                else -> Unit
            }
        }
    }

    fun verifyPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = authRepository.verifyPhoneNumber(phoneNumber)) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        verificationId = result.data,
                        authState = AuthState.VERIFICATION_PENDING
                    )}
                }
                is AuthResult.Error -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = result.error.toString()
                    )}
                }
                else -> Unit
            }
        }
    }

    fun signInWithPhoneNumber(code: String) {
        viewModelScope.launch {
            val verificationId = _uiState.value.verificationId ?: return@launch

            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = authRepository.signInWithPhoneNumber(verificationId, code)) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        user = result.data,
                        authState = AuthState.AUTHENTICATED,
                        verificationId = null
                    )}
                }
                is AuthResult.Error -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = result.error.toString()
                    )}
                }
                else -> Unit
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.update { it.copy(
                authState = AuthState.UNAUTHENTICATED,
                user = null,
                verificationId = null
            )}
        }
    }
}