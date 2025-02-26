package com.iyr.ultrachango.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.PhoneAuthProvider
import com.iyr.ultrachango.ui.screens.setting.SettingScreen.SettingsScreenViewModel.UiState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent

class PhoneVerificationViewModel(
//    private val repository: DeepLinkRepository
): ViewModel(), KoinComponent {

    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken


    private var _resentToken = MutableStateFlow<PhoneAuthProvider.ForceResendingToken?>(null)
    val resentToken= _resentToken.asStateFlow()

    fun saveResentToken(token: PhoneAuthProvider.ForceResendingToken) {
        _resentToken.value = token
    }
}