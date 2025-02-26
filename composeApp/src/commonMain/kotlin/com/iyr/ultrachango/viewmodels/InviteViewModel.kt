package com.iyr.ultrachango.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent

class InviteViewModel(
//    private val repository: DeepLinkRepository
): ViewModel(), KoinComponent {

    private val _inviteLink = MutableStateFlow("")
    val inviteLink: StateFlow<String> get() = _inviteLink

    fun generateInviteLink(referralId: String) {
        CoroutineScope(Dispatchers.IO).launch {
         //   _inviteLink.value = repository.generateInviteLink(referralId)
        }
    }
}