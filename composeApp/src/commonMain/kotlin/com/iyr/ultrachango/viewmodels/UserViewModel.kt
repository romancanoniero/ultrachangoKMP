package com.iyr.ultrachango.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.auth.AuthRepository
import com.iyr.ultrachango.auth.AuthRepositoryImpl
import com.iyr.ultrachango.data.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class UserViewModel(
    val authRepository: AuthRepositoryImpl
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    fun loginUser(user: User) {
        viewModelScope.launch {
            _user.value = user
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            _user.value = null
        }
    }

    fun setUserKey(key: String) {
        _user.value = _user.value?.copy(id = key)
    }

    fun getUserKey():String
    {
        return authRepository.currentUserId
   //     return _user.value?.id.toString()
    }
}