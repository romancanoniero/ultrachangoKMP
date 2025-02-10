package com.iyr.ultrachango.ui.screens.auth.registration

import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.auth.AuthRepository
import com.iyr.ultrachango.auth.AuthRepositoryImpl
import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.data.models.enums.AuthenticationMethods
import com.iyr.ultrachango.ui.ScaffoldViewModel
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
    private val authService: AuthRepositoryImpl,
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
            registerButtonEnabled = isRegistrable()
        )
        if (authenticationMethod== AuthenticationMethods.EMAIL && text.isNotBlank()) _emailError.value = false
    }

    fun setPassword(text: String) {
        _uiState.value = _uiState.value.copy(
            password = text,
            registerButtonEnabled = isRegistrable()
        )
        if (text.isNotBlank()) _passwordError.value = false
    }

    private fun isRegistrable(): Boolean {
        return _uiState.value.firstName.isNotEmpty() && _uiState.value.firstName.length >= 3 &&
                _uiState.value.lastName.isNotEmpty() && _uiState.value.lastName.length >= 3 &&
                _uiState.value.emailOrPhoneNumber.isNotEmpty() &&
                (_uiState.value.emailOrPhoneNumber.isValidMobileNumber() || _uiState.value.emailOrPhoneNumber.isEmail())
                &&
                (_uiState.value.authenticationMethod == AuthenticationMethods.PHONE_NUMBER || (_uiState.value.authenticationMethod == AuthenticationMethods.EMAIL && _uiState.value.password.isNotEmpty()))
    }

    fun setFirstName(text: String) {
        _uiState.value = _uiState.value.copy(
            firstName = text,
        )
    }


    fun setLastName(text: String) {
        _uiState.value = _uiState.value.copy(
            lastName = text,
        )
    }

    fun setAuthenticationMethod(authenticationMethod: AuthenticationMethods) {
        _uiState.value = _uiState.value.copy(
            authenticationMethod = authenticationMethod,
        )
    }

    fun setEmailOrPassword(text: String) {
        var authenticationMethod: AuthenticationMethods
        if (text.isValidMobileNumber()) {
            authenticationMethod = AuthenticationMethods.PHONE_NUMBER
        } else
            if (text.isEmail()) {
                authenticationMethod = AuthenticationMethods.EMAIL
            } else authenticationMethod = AuthenticationMethods.NONE


        _uiState.value = _uiState.value.copy(
            emailOrPhoneNumber = text,
            authenticationMethod = authenticationMethod,
            registerButtonEnabled = isRegistrable()
        )
    }

    fun setEmailOrPhoneNumber(text: String) {
        _uiState.value = _uiState.value.copy(
            emailOrPhoneNumber = text
        )
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
            _isProcessing.value = true
           when (_uiState.value.authenticationMethod) {
               AuthenticationMethods.EMAIL -> authService.createUser(
                   _uiState.value.emailOrPhoneNumber,
                   _uiState.value.password
               )

               AuthenticationMethods.PHONE_NUMBER -> authService.createUser(
                   _uiState.value.emailOrPhoneNumber

               )

               else -> {
               }

           }
            val result = authService.createUser(_uiState.value.emailOrPhoneNumber, _uiState.value.password)
        //    authService.authenticate(_uiState.value.email, _uiState.value.password)
            _isProcessing.value = false
        }

    }



    data class UiState(
        val loading: Boolean = false,
        val errorMessage: String? = null,
        val showErrorMessage: Boolean = false,
        val firstName: String = "",
        val lastName: String = "",
        val emailOrPhoneNumber: String = "",
        val password: String = "",
        val authenticationMethod: AuthenticationMethods = AuthenticationMethods.NONE,
        val registerButtonEnabled: Boolean = false,
    )
}
