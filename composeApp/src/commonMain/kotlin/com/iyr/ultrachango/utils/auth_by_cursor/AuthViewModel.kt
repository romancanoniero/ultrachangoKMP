package com.iyr.ultrachango.utils.auth_by_cursor

import androidx.lifecycle.ViewModel
import com.iyr.ultrachango.utils.auth_by_cursor.auth.AuthError
import com.iyr.ultrachango.utils.auth_by_cursor.models.AppUser
import com.iyr.ultrachango.utils.auth_by_cursor.models.AuthResult

import com.iyr.ultrachango.utils.auth_by_cursor.repository.AuthRepository
import com.iyr.ultrachango.utils.auth_by_cursor.statemanagers.AuthStates
import com.iyr.ultrachango.utils.auth_by_cursor.statemanagers.AuthStateManager
import com.iyr.ultrachango.utils.auth_by_cursor.ui.AuthErrorType
import com.iyr.ultrachango.utils.auth_by_cursor.ui.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

data class AuthUiState(
    val isLoading: Boolean = false,
    val authState: AuthStates = AuthStates.UNAUTHENTICATED,
    val user: AppUser? = null,
    val error: String? = null,
    val verificationId: String? = null
)


class AuthViewModel(
    private val authRepository: AuthRepository,
    private val authStateManager: AuthStateManager
) : ViewModel(), KoinComponent {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()


    init {
        // Verificar estado inicial de autenticación
        checkInitialAuthState()
        // Observar cambios en el estado de autenticación
        observeAuthState()
    }

    private fun checkInitialAuthState() {
        val initialState = authStateManager.checkInitialAuthState()
        _uiState.update {
            it.copy(
                authState = initialState,
                user = authStateManager.getCurrentUser()
            )
        }
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authStateManager.getAuthState().collect { authState ->
                _uiState.update {
                    it.copy(
                        authState = authState,
                        user = authStateManager.getCurrentUser()
                    )
                }
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = authRepository.signInWithEmailAndPassword(email, password)) {
                is AuthResult.Success -> {


                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = result.data,
                            authState = AuthStates.AUTHENTICATED
                        )
                    }
                }

                is AuthResult.Error -> {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.error.toString()
                        )
                    }
                }

                else -> Unit
            }
        }
    }

    fun verifyPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {


            when (val result = authRepository.verifyPhoneNumber(phoneNumber)) {
                is AuthResult.Success -> {


                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            verificationId = result.data,
                            authState = AuthStates.VERIFICATION_PENDING
                        )
                    }
                }

                is AuthResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.error.toString()
                        )
                    }
                }

                AuthResult.Loading ->   _uiState.update { it.copy(isLoading = true, error = null) }
            }
        }
    }

    fun signInWithPhoneNumber(code: String) {
        viewModelScope.launch {
            val verificationId = _uiState.value.verificationId ?: return@launch

            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = authRepository.signInWithPhoneNumber(verificationId, code)) {
                is AuthResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = result.data,
                            authState = AuthStates.AUTHENTICATED,
                            verificationId = null
                        )
                    }
                }

                is AuthResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.error.toString()
                        )
                    }
                }

                else -> Unit
            }
        }
    }

    // Google
    suspend fun signInWithGoogle(idToken: String) {
        _authState.value = AuthState.Loading
        when (val result = authRepository.signInWithGoogle(idToken)) {
            is AuthResult.Success -> _authState.value = AuthState.Success(result.data)
            is AuthResult.Error -> _authState.value = AuthState.Error(
                message = when (val error = result.error) {
                    is AuthError.NetworkError -> error.message
                    is AuthError.InvalidCredentials -> error.message
                    is AuthError.UserNotFound -> error.message
                    is AuthError.WeakPassword -> error.message
                    is AuthError.EmailAlreadyInUse -> error.message
                    is AuthError.InvalidEmail -> error.message
                    is AuthError.InvalidVerificationCode -> error.message
                    is AuthError.Unknown -> error.message
                },
                type = AuthErrorType.GOOGLE_SIGN_IN_FAILED
            )

            AuthResult.Loading -> TODO()
        }
    }

    // Facebook
    suspend fun signInWithFacebook(accessToken: String) {
        _authState.value = AuthState.Loading
        when (val result = authRepository.signInWithFacebook(accessToken)) {
            is AuthResult.Success -> _authState.value = AuthState.Success(result.data)
            is AuthResult.Error -> _authState.value = AuthState.Error(
                message = when (val error = result.error) {
                    is AuthError.NetworkError -> error.message
                    is AuthError.InvalidCredentials -> error.message
                    is AuthError.UserNotFound -> error.message
                    is AuthError.WeakPassword -> error.message
                    is AuthError.EmailAlreadyInUse -> error.message
                    is AuthError.InvalidEmail -> error.message
                    is AuthError.InvalidVerificationCode -> error.message
                    is AuthError.Unknown -> error.message
                },
                type = AuthErrorType.GOOGLE_SIGN_IN_FAILED
            )

            AuthResult.Loading -> TODO()
        }
    }

    // Apple
    suspend fun signInWithApple(idToken: String, nonce: String?) {
        _authState.value = AuthState.Loading
        when (val result = authRepository.signInWithApple(idToken, nonce)) {
            is AuthResult.Success -> _authState.value = AuthState.Success(result.data)
            is AuthResult.Error -> _authState.value = AuthState.Error(
                message = when (val error = result.error) {
                    is AuthError.NetworkError -> error.message
                    is AuthError.InvalidCredentials -> error.message
                    is AuthError.UserNotFound -> error.message
                    is AuthError.WeakPassword -> error.message
                    is AuthError.EmailAlreadyInUse -> error.message
                    is AuthError.InvalidEmail -> error.message
                    is AuthError.InvalidVerificationCode -> error.message
                    is AuthError.Unknown -> error.message
                },
                type = AuthErrorType.GOOGLE_SIGN_IN_FAILED
            )

            AuthResult.Loading -> TODO()
        }
    }


    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.update {
                it.copy(
                    authState = AuthStates.UNAUTHENTICATED,
                    user = null,
                    verificationId = null
                )
            }
        }
    }
}