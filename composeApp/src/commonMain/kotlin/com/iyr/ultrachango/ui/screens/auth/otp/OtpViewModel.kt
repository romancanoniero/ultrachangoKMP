package com.iyr.ultrachango.ui.screens.auth.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.ui.screens.auth.otp.state.OtpEvent
import com.iyr.ultrachango.ui.screens.auth.otp.state.OtpState
import com.iyr.ultrachango.utils.auth_by_cursor.models.AuthResult
import com.iyr.ultrachango.utils.auth_by_cursor.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OtpViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow<OtpState>(OtpState.Initial)
    val state = _state.asStateFlow()

    private var verificationId: String = ""
    private var phoneNumber: String = ""

    fun initialize(verificationId: String, phoneNumber: String) {
        this.verificationId = verificationId
        this.phoneNumber = phoneNumber
        _state.value = OtpState.CodeSent(verificationId, phoneNumber)
    }

    fun onEvent(event: OtpEvent) {
        when (event) {
            is OtpEvent.VerifyCode -> verifyCode(event.code)
            is OtpEvent.ResendCode -> resendCode()
            is OtpEvent.NavigateBack -> {} // Manejar navegación
        }
    }

    private fun verifyCode(code: String) {
        viewModelScope.launch {
            _state.value = OtpState.Loading
            when (val result = authRepository.signInWithPhoneNumber(verificationId, code)) {
                is AuthResult.Success -> _state.value = OtpState.Success(result.data)
                is AuthResult.Error -> _state.value = OtpState.Error(result.error.toString())
                is AuthResult.Loading -> _state.value = OtpState.Loading
            }
        }
    }

    private fun resendCode() {
        viewModelScope.launch {
            _state.value = OtpState.Loading
            when (val result = authRepository.verifyPhoneNumber(phoneNumber)) {
                is AuthResult.Success -> {
                    verificationId = result.data
                    _state.value = OtpState.CodeSent(result.data, phoneNumber)
                }
                is AuthResult.Error -> _state.value = OtpState.Error(result.error.toString())
                is AuthResult.Loading -> _state.value = OtpState.Loading
            }
        }
    }
}