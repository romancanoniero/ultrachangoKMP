package com.iyr.ultrachango.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.utils.auth_by_cursor.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class UserViewModel(
    val authRepository: AuthRepository
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
        _user.value = _user.value?.copy(uid = key)
    }

    fun getUserKey():String?
    {
        return authRepository.getUserKey()
    }
}