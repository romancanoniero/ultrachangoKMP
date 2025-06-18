package com.iyr.ultrachango.ui.screens.auth.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyr.fbauthentication.common.AuthUser
import com.iyr.ultrachango.data.repository.AuthRepositoryImpl
import com.iyr.ultrachango.domain.auth.AuthRepository
import com.iyr.ultrachango.domain.auth.exceptions.ErrorSendingVerificationCodeException
import com.iyr.ultrachango.domain.auth.exceptions.UserDataNotFoundException
import com.iyr.ultrachango.domain.auth.models.AppUser
import com.iyr.ultrachango.presentation.auth.AuthErrorType
import com.iyr.ultrachango.ui.screens.auth.otp.state.OtpEvent
import com.iyr.ultrachango.ui.screens.auth.otp.state.OtpState
import com.iyr.ultrachango.utils.coroutines.Resource
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

    @Throws(UserDataNotFoundException::class)
    fun onEvent(event: OtpEvent) {
        when (event) {
            is OtpEvent.VerifyCode -> {
                verifyCode(event.code)

            }

            is OtpEvent.ResendCode -> resendCode()
            is OtpEvent.NavigateBack -> {} // Manejar navegación
        }
    }

    private fun verifyCode(code: String) {
        viewModelScope.launch {
            _state.value = OtpState.Loading
            val result = authRepository.verifyPhoneNumber(verificationId, code)

            when (result) {
                is Resource.Success -> {
                    result.data?.let { user ->
                        _state.value = OtpState.Success(user)
                    } ?: run {
                        _state.value = OtpState.Error(
                            "Error: datos de usuario no disponibles",
                            UserDataNotFoundException("Usuario autenticado pero requiere completar perfil")
                        )
                    }
                }

                is Resource.Error -> {

                    val errorType = result.errorType ?: AuthErrorType.UNKNOWN
                    when (errorType) {
                        AuthErrorType.USER_DATA_NOT_FOUND -> {
                            _state.value = OtpState.Error(
                                "Usuario autenticado pero requiere completar perfil",
                                UserDataNotFoundException("Usuario autenticado pero requiere completar perfil"),
                                errorType
                            )
                        }
                        else -> {
                            _state.value = OtpState.Error(
                                result.message ?: "Error desconocido",
                                Exception(result.message)
                            )

                        }
                    }
                }

                is Resource.Loading -> {
                    _state.value = OtpState.Loading
                }
            }
        }
    }

    private fun resendCode() {
        viewModelScope.launch {
            _state.value = OtpState.Loading
            when (val result = authRepository.signInWithPhone(phoneNumber)) {
                is Resource.Success -> {
                    verificationId = result.data.toString()
                    _state.value = OtpState.CodeSent(result.data.toString(), phoneNumber)
                }

                is Resource.Error -> _state.value = OtpState.Error(
                    message = result.message ?: "Error al reenviar código",
                    exception = ErrorSendingVerificationCodeException()
                )

                is Resource.Loading -> _state.value = OtpState.Loading
            }
        }
    }

    fun onErrorDialogRequest() {
        _state.value = OtpState.Initial
    }
}