package com.iyr.ultrachango.ui.screens.auth.registration

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.auth.AuthRepository

import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.data.models.enums.AuthenticationMethods
import com.iyr.ultrachango.preferences.managers.settings
import com.iyr.ultrachango.storeUserLocally
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.ui.screens.auth.login.LoginViewModel.UiState
import com.iyr.ultrachango.utils.extensions.isEmail
import com.iyr.ultrachango.utils.extensions.isValidMobileNumber
import com.iyr.ultrachango.utils.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent

class RegisterViewModel(
    private val authService: AuthRepository,
    private val scaffoldVM: ScaffoldViewModel,
) : BaseViewModel(), KoinComponent {


    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()


    private val _emailError = MutableStateFlow(false)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow(false)
    val passwordError = _passwordError.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing = _isProcessing.asStateFlow()

    private val isButtonEnabled: StateFlow<Boolean> = combine(uiState) { states ->
        val state = states.first()
        state.firstName.isNotEmpty() && state.firstName.length >= 3 &&
                state.lastName.isNotEmpty() && state.lastName.length >= 3 &&
                state.emailOrPhoneNumber.isNotEmpty() &&
                (state.emailOrPhoneNumber.isValidMobileNumber() || state.emailOrPhoneNumber.isEmail())
                &&
                (state.authenticationMethod == AuthenticationMethods.PHONE_NUMBER || (state.authenticationMethod == AuthenticationMethods.EMAIL && state.password.isNotEmpty()))
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), false
    )


    init {
        /*
                launchWithCatchingException {
                    authService.currentUser.collect {
                        _currentUser.value = it
                    }
                }
                */
    }


    fun setMailOrPhone(authenticationMethod: AuthenticationMethods, text: String) {
        _uiState.value = _uiState.value.copy(
            authenticationMethod = authenticationMethod,
            emailOrPhoneNumber = text,
            conditionsCheckEnabled = isCompleted()
        )
        if (authenticationMethod == AuthenticationMethods.EMAIL && text.isNotBlank()) _emailError.value =
            false
    }

    fun setPassword(text: String) {
        _uiState.value = _uiState.value.copy(
            password = text,
            conditionsCheckEnabled = isCompleted()
        )
        if (text.isNotBlank()) _passwordError.value = false
    }

    private fun isCompleted(): Boolean {
        var isCompleted =
            _uiState.value.firstName.isNotEmpty() && _uiState.value.firstName.length >= 3 &&
                    _uiState.value.lastName.isNotEmpty() && _uiState.value.lastName.length >= 3 &&
                    _uiState.value.emailOrPhoneNumber.isNotEmpty() &&
                    (_uiState.value.emailOrPhoneNumber.isValidMobileNumber() || _uiState.value.emailOrPhoneNumber.isEmail())
                    &&
                    (_uiState.value.authenticationMethod == AuthenticationMethods.PHONE_NUMBER || (_uiState.value.authenticationMethod == AuthenticationMethods.EMAIL && _uiState.value.password.isNotEmpty()))


        if (!isCompleted) {
            _uiState.value = _uiState.value.copy(
                termsAccepted = false,
                conditionsCheckEnabled = false,
                registerButtonEnabled = false
            )
        } else {
            _uiState.value = _uiState.value.copy(
                conditionsCheckEnabled = true,

                )
        }
        return isCompleted
    }


    fun setFirstName(text: String) {
        _uiState.value = _uiState.value.copy(
            firstName = text,
        )
        isCompleted()
    }


    fun setLastName(text: String) {
        _uiState.value = _uiState.value.copy(
            lastName = text,
        )
        isCompleted()
    }

    fun setAuthenticationMethod(authenticationMethod: AuthenticationMethods) {
        _uiState.value = _uiState.value.copy(
            authenticationMethod = authenticationMethod,
        )
        isCompleted()
    }

    fun setEmailOrPassword(textFieldValue: TextFieldValue) {
        var authenticationMethod: AuthenticationMethods
        if (textFieldValue.text.isValidMobileNumber()) {
            authenticationMethod = AuthenticationMethods.PHONE_NUMBER
        } else
            if (textFieldValue.text.isEmail()) {
                authenticationMethod = AuthenticationMethods.EMAIL
            } else authenticationMethod = AuthenticationMethods.NONE


        _uiState.value = _uiState.value.copy(
            emailOrPhoneNumber = textFieldValue.text,
            authenticationMethod = authenticationMethod,
            conditionsCheckEnabled = isCompleted()
            //    registerButtonEnabled = isRegistrable()

        )
        isCompleted()
    }

    fun setEmailOrPhoneNumber(text: String) {
        _uiState.value = _uiState.value.copy(
            emailOrPhoneNumber = text
        )
        isCompleted()
    }


    fun onRegisterClick() {

        if (_uiState.value.emailOrPhoneNumber.isEmpty()) {
            _emailError.value = true
            return
        }

        if (_uiState.value.password.isEmpty()) {
            _emailError.value = true
            return
        }

        launchWithCatchingException {
            _uiState.value = _uiState.value.copy(
                loading = false
            )


            when (_uiState.value.authenticationMethod) {
                AuthenticationMethods.EMAIL -> {

                    try {
                        authService.signUpWithEmail(
                            firstName = _uiState.value.firstName,
                            lastName = _uiState.value.lastName,
                            authenticationMethod = AuthenticationMethods.EMAIL.toString(),
                            email = _uiState.value.emailOrPhoneNumber,
                            password = _uiState.value.password,
                            onFailure = {
                                _uiState.value = _uiState.value.copy(
                                    errorMessage = it.message,
                                    showErrorMessage = true,
                                    loading = false
                                )
                            }
                        )
                        {
                            // Aca gestionar lo que sucede desdepues de haberse registrado
                            val pp = 33
                            settings.storeUserLocally(it)
                            _uiState.value = _uiState.value.copy(
                                loading = false
                            )
                        }

                    } catch (ex: Exception) {

                        val message = when (ex.message) {
                            "The email address is already in use by another account." -> "El email ya está en uso"
                            else -> ex.message
                        }

                        _uiState.value = _uiState.value.copy(
                            errorMessage = message ?: "",
                            showErrorMessage = true
                        )
                    }

                }

                AuthenticationMethods.PHONE_NUMBER -> authService.signUpWithEmail(
                    _uiState.value.emailOrPhoneNumber

                )

                else -> {
                }

            }
            _isProcessing.value = false
        }

    }

    fun setAcceptance(checked: Boolean) {
        _uiState.value = _uiState.value.copy(
            termsAccepted = checked,
            registerButtonEnabled = checked
        )
    }

    fun closeErrorDialogRequest() {
        _uiState.value = _uiState.value.copy(
            showErrorMessage = false
        )
    }


    data class UiState(
        val loading: Boolean = false,
        val errorMessage: String? = null,
        val showErrorMessage: Boolean = false,
        val firstName: String = "pirineo",
        val lastName: String = "rodriguez",
        val emailOrPhoneNumber: String = "pirineorodriguez@gmail.com",
        val password: String = "123456",
        val authenticationMethod: AuthenticationMethods = AuthenticationMethods.NONE,
        val termsAccepted: Boolean = false,
        val conditionsCheckEnabled: Boolean = false,
        val registerButtonEnabled: Boolean = false,
    )
}
