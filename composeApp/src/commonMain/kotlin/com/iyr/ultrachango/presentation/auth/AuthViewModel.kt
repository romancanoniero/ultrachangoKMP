package com.iyr.ultrachango.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyr.fbauthentication.common.AuthUser
import com.iyr.ultrachango.config.BuildConfig
import com.iyr.ultrachango.data.preferences.UserPreferences
import com.iyr.ultrachango.domain.auth.AuthRepository
import com.iyr.ultrachango.domain.auth.models.AppUser
import com.iyr.ultrachango.presentation.auth.AuthState.*
import com.iyr.ultrachango.utils.coroutines.Resource
import com.iyr.ultrachango.utils.network.RetryPolicy
import com.iyr.ultrachango.utils.network.isRetriableError

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock.System
import org.koin.core.component.KoinComponent

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel(), KoinComponent {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Estados centralizados para loading y errores
    private val _loadingState = MutableStateFlow(LoadingState())
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _errorState = MutableStateFlow(ErrorState())
    val errorState: StateFlow<ErrorState> = _errorState.asStateFlow()

    // Eventos one-shot para la UI
    private val _authEvents = MutableSharedFlow<AuthEvent>()
    val authEvents: SharedFlow<AuthEvent> = _authEvents.asSharedFlow()

    // Retry policy para operaciones de autenticación
    private val retryPolicy = RetryPolicy(maxRetries = 3, initialDelayMs = 1000L)

    init {
        checkAuthState()
        observeAuthState()
    }

    private fun observeAuthState() {
        /*
        viewModelScope.launch {
            authRepository.observeAuthState().collect { user ->
                user?.let {
                    _authState.value = AuthState.Success(it)
                }
            }
        }
        */
    }

    // Métodos auxiliares para manejo de estados
    private suspend fun showLoading(message: String = "Autenticando...", canCancel: Boolean = false) {
        _loadingState.value = LoadingState(isLoading = true, message = message, canCancel = canCancel)
        _authEvents.emit(AuthEvent.ShowLoading)
    }

    private suspend fun hideLoading() {
        _loadingState.value = LoadingState()
        _authEvents.emit(AuthEvent.HideLoading)
    }

    private suspend fun showError(
        message: String,
        type: AuthErrorType,
        canRetry: Boolean = false,
        retryAttempt: Int = 0
    ) {
        _errorState.value = ErrorState(
            hasError = true,
            message = message,
            type = type,
            canRetry = canRetry,
            retryAttempt = retryAttempt
        )
        _authEvents.emit(AuthEvent.ShowError(message, type, false, retryAttempt))
    }

    private suspend fun hideError() {
        _errorState.value = ErrorState()
        _authEvents.emit(AuthEvent.HideError)
    }

    // Método genérico para cualquier tipo de Resource
    private suspend fun <T> executeAuthOperationGeneric(
        operation: suspend () -> Resource<T>,
        operationType: String,
        onSuccess: suspend (T) -> Unit,
        retryableOperation: (suspend () -> Unit)? = null
    ) {
        try {
            showLoading("Autenticando con $operationType...")
            
            val result = retryPolicy.executeWithRetry(
                operation = { operation() },
                shouldRetry = { error -> 
                    error.isRetriableError() || error.message?.contains("network", ignoreCase = true) == true
                }
            )
            
            when (result) {
                is Resource.Success -> {
                    hideLoading()
                    hideError()
                    result.data?.let {
                        onSuccess(it) 
                    }
                }

                is Resource.Error -> {
                    hideLoading()
                    val errorType = mapErrorToAuthErrorType(result.message ?: "")
                    
                    // Manejar el caso especial de perfil incompleto
                    if (errorType == AuthErrorType.USER_DATA_NOT_FOUND) {
                        showError("Datos de usuario incompletos. Complete su perfil.", errorType)
                        _authState.value = Error("Datos de usuario incompletos", errorType)
                        _authEvents.emit(AuthEvent.RequireProfileCompletion())
                        return
                    }
                    
                    val canRetry = result.message?.let { 
                        it.contains("network", ignoreCase = true) || 
                        it.contains("timeout", ignoreCase = true) 
                    } ?: false
                    
                    if (canRetry && retryableOperation != null) {
                        _authEvents.emit(AuthEvent.ShowRetryDialog(
                            message = "Error de conexión. ¿Deseas reintentar?",
                            onRetry = { 
                                viewModelScope.launch { 
                                    retryableOperation() 
                                }
                            },
                            onCancel = { 
                                viewModelScope.launch { 
                                    hideError() 
                                }
                            }
                        ))
                    } else {
                        showError(result.message ?: "Error desconocido", errorType)
                        _authState.value = Error(result.message ?: "Error desconocido", errorType)
                    }
                }

                is Resource.Loading -> {
                    _authState.value = Loading
                }
            }
        } catch (e: Exception) {
            hideLoading()
            val errorType = mapErrorToAuthErrorType(e.message ?: "")
            showError(e.message ?: "Error inesperado", errorType)
            _authState.value = Error(e.message ?: "Error inesperado", errorType)
        }
    }

    private fun mapErrorToAuthErrorType(errorMessage: String): AuthErrorType {
        return when {
            errorMessage.contains("requiere completar perfil", ignoreCase = true) -> AuthErrorType.USER_DATA_NOT_FOUND
            errorMessage.contains("UserDataNotFoundException", ignoreCase = true) -> AuthErrorType.USER_DATA_NOT_FOUND
            errorMessage.contains("Usuario no encontrado", ignoreCase = true) -> AuthErrorType.USER_DATA_NOT_FOUND
            errorMessage.contains("Datos incompletos", ignoreCase = true) -> AuthErrorType.USER_DATA_NOT_FOUND
            errorMessage.contains("Datos del usuario no encontrados", ignoreCase = true) -> AuthErrorType.USER_DATA_NOT_FOUND
            errorMessage.contains("network", ignoreCase = true) -> AuthErrorType.NETWORK_ERROR
            errorMessage.contains("timeout", ignoreCase = true) -> AuthErrorType.TIMEOUT_ERROR
            errorMessage.contains("backend", ignoreCase = true) -> AuthErrorType.BACKEND_ERROR
            errorMessage.contains("password", ignoreCase = true) -> AuthErrorType.WRONG_PASSWORD
            errorMessage.contains("email", ignoreCase = true) -> AuthErrorType.INVALID_EMAIL
            errorMessage.contains("user not found", ignoreCase = true) -> AuthErrorType.USER_NOT_FOUND
            else -> AuthErrorType.UNKNOWN
        }
    }

    // Método específico para AppUser (refactorizado para usar el genérico)
    private suspend fun executeAuthOperation(
        operation: suspend () -> Resource<AppUser>,
        operationType: String,
        retryableOperation: (suspend () -> Unit)? = null
    ) {
        executeAuthOperationGeneric(
            operation = operation,
            operationType = operationType,
            onSuccess = { user: AppUser ->
                _authState.value = Success(user)
                _authEvents.emit(AuthEvent.ShowSuccess("Autenticación exitosa"))
            },
            retryableOperation = retryableOperation
        )
    }

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            executeAuthOperation(
                operation = { 
                    val result = authRepository.signInWithEmail(email, password)
                    if (result is Resource.Success) {
                        userPreferences.saveLastLoginMethod("EMAIL")
                        userPreferences.saveLastLoginTimestamp(System.now().toEpochMilliseconds())
                    }
                    result
                },
                operationType = "Email",
                retryableOperation = { signInWithEmail(email, password) }
            )
        }
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            executeAuthOperation(
                operation = { 
                    val result = authRepository.signInWithGoogle()
                    if (result is Resource.Success) {
                        userPreferences.saveLastLoginMethod("GOOGLE")
                        userPreferences.saveLastLoginTimestamp(System.now().toEpochMilliseconds())
                    }
                    result
                },
                operationType = "Google",
                retryableOperation = { signInWithGoogle() }
            )
        }
    }

    fun signInWithFacebook() {
        viewModelScope.launch {
            executeAuthOperation(
                operation = { 
                    val result = authRepository.signInWithFacebook(BuildConfig.FACEBOOK_CLIENT_TOKEN)
                    if (result is Resource.Success) {
                        userPreferences.saveLastLoginMethod("FACEBOOK")
                        userPreferences.saveLastLoginTimestamp(System.now().toEpochMilliseconds())
                    }
                    result
                },
                operationType = "Facebook",
                retryableOperation = { signInWithFacebook() }
            )
        }
    }

    fun signInWithApple(idToken: String, nonce: String? = null) {
        viewModelScope.launch {
            executeAuthOperation(
                operation = { 
                    val result = authRepository.signInWithApple(idToken, nonce)
                    if (result is Resource.Success) {
                        userPreferences.saveLastLoginMethod("APPLE")
                        userPreferences.saveLastLoginTimestamp(System.now().toEpochMilliseconds())
                    }
                    result
                },
                operationType = "Apple",
                retryableOperation = { signInWithApple(idToken, nonce) }
            )
        }
    }

    fun signInWithTwitter(token: String, secret: String) {
        viewModelScope.launch {
            executeAuthOperation(
                operation = { 
                    val result = authRepository.signInWithTwitter(token, secret)
                    if (result is Resource.Success) {
                        userPreferences.saveLastLoginMethod("TWITTER")
                        userPreferences.saveLastLoginTimestamp(System.now().toEpochMilliseconds())
                    }
                    result
                },
                operationType = "Twitter",
                retryableOperation = { signInWithTwitter(token, secret) }
            )
        }
    }

    fun signInWithPhone(phoneNumber: String) {
        viewModelScope.launch {
            executeAuthOperationGeneric(
                operation = { authRepository.signInWithPhone(phoneNumber) },
                operationType = "Teléfono",
                onSuccess = { verificationId: String? ->
                    _authState.value = PhoneVerificationSent(
                        verificationId = verificationId ?: "",
                        phoneNumber = phoneNumber
                    )
                    _authEvents.emit(AuthEvent.ShowSuccess("Código SMS enviado"))
                },
                retryableOperation = { signInWithPhone(phoneNumber) }
            )
        }
    }

    fun verifyPhoneNumber(verificationId: String, code: String) {
        viewModelScope.launch {
            executeAuthOperation(
                operation = { 
                    val result = authRepository.verifyPhoneNumber(verificationId, code)
                    if (result is Resource.Success) {
                        userPreferences.saveLastLoginMethod("PHONE")
                        userPreferences.saveLastLoginTimestamp(System.now().toEpochMilliseconds())
                    }
                    result
                },
                operationType = "Teléfono",
                retryableOperation = { verifyPhoneNumber(verificationId, code) }
            )
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                showLoading("Cerrando sesión...")
                authRepository.signOut()
                hideLoading()
                _authState.value = AuthState.Initial
                _authEvents.emit(AuthEvent.ShowSuccess("Sesión cerrada"))
            } catch (e: Exception) {
                hideLoading()
                showError(e.message ?: "Error al cerrar sesión", AuthErrorType.UNKNOWN)
            }
        }
    }

    fun dismissError() {
        viewModelScope.launch {
            hideError()
        }
    }

    fun checkAuthState() {
        viewModelScope.launch {
            try {
                if (authRepository.isUserSignedIn()) {
                    authRepository.getCurrentUser()?.let { user ->
                        _authState.value = Success(user)
                    }
                }
            } catch (e: Exception) {
                // Si hay error al verificar estado, mantener estado inicial
                _authState.value = AuthState.Initial
            }
        }
    }

    fun getCurrentUser(): AppUser? {
        return authRepository.getCurrentUser()
    }
} 