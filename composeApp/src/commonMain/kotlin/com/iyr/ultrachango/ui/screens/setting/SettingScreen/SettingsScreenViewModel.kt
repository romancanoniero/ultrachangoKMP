package com.iyr.ultrachango.ui.screens.setting.SettingScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.auth.AuthRepository
import com.iyr.ultrachango.data.database.repositories.ImagesRepository
import com.iyr.ultrachango.utils.helpers.getProfileImageURL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class SettingsScreenViewModel(
    private val authRepository: AuthRepository,
    private val imagesRepository: ImagesRepository
) : ViewModel(), KoinComponent {
    private val _imageUri = MutableStateFlow<String?>(null)
    val imageUri = _imageUri.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing = _isProcessing.asStateFlow()
    /*
        var state by mutableStateOf(UiState())
            private set
      */

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        val user = authRepository.getCurrentUser()
        user.let { it ->
            _imageUri.value = getProfileImageURL(it!!.uid.toString(), it.photoUrl.toString())

        }
    }

    fun hideDialogs() {
        _uiState.value = _uiState.value.copy(
            showLogoutConfirmationDialog = false,
            showErrorMessage = false
        )
    }

    fun onLogoutClick() {
        _uiState.value = _uiState.value.copy(showLogoutConfirmationDialog = true)
    }

    fun saveProfilePicture(bytes: ByteArray) {

            viewModelScope.launch(Dispatchers.IO) {
                _isProcessing.value = true
                try {
                    imagesRepository.postProfileImage(bytes)
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        showErrorMessage = true,
                        errorMessage = e.message
                    )
                } finally {
                    _isProcessing.value = false
                }

            }


    }

    fun onImageUriChange(uri: String) {
        _imageUri.value = uri
    }

    data class UiState(
        val showLogoutConfirmationDialog: Boolean = false,
        val showErrorMessage: Boolean = false,
        val errorMessage: String? = null
    )


}


