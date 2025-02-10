package com.iyr.ultrachango.ui.screens.setting.profile

import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.auth.AuthRepositoryImpl
import com.iyr.ultrachango.data.database.repositories.ImagesRepository
import com.iyr.ultrachango.data.database.repositories.UserRepositoryImpl
import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.data.models.enums.AuthenticationMethods
import com.iyr.ultrachango.data.models.enums.Genders
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.utils.extensions.isEmail
import com.iyr.ultrachango.utils.extensions.isValidMobileNumber
import com.iyr.ultrachango.utils.viewmodel.BaseViewModel
import com.iyr.ultrachango.validateForm

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent

class ProfileViewModel(
    private val authService: AuthRepositoryImpl,
    private val usersRepository: UserRepositoryImpl,
    private val imagesRepository: ImagesRepository,
    private val scaffoldVM: ScaffoldViewModel,
) : BaseViewModel(), KoinComponent
{


    private val _originalUser = MutableStateFlow<User?>(null)
    val originalUser = _originalUser.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _showImagePicker = MutableStateFlow(false)
    val showImagePicker = _showImagePicker.asStateFlow()

    private val _emailError = MutableStateFlow(false)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow(false)
    val passwordError = _passwordError.asStateFlow()

//    val userStored = authService.getUser()


    private val _isProcessing = MutableStateFlow(false)
    val isProcessing = _isProcessing.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    private val _imageProfile = MutableStateFlow<ByteArray?>(null)
    val imageProfile = _imageProfile.asStateFlow()


    init {
        val me = authService.getCurrentUser()
        _originalUser.value = me?.copy()
        _currentUser.value = me


        //     var firebaseAuth = Firebase.auth(Firebase.initialize(AppContext.getContext()!!)!!).currentUser
//val pepe = firebaseAuth?.displayName
        viewModelScope.launch {
            imagesRepository.getProfileImageURL(me?.id ?: "xxxx")?.let {
                _imageProfile.value = it
                _uiState.value = UiState(loginButtonEnabled = validate())
            }
            Firebase.auth.authStateChanged.collect() { user ->
                if (user != null) {
                    // User is signed in
                    println("User is signed in")
                    _isAuthenticated.value = true
                } else {
                    // No user is signed in
                    println("No user is signed in")

                    _isAuthenticated.value = false
                }
            }
        }
    }

    fun setMailOrPhone(authenticationMethod: AuthenticationMethods, text: String) {
        _uiState.value = _uiState.value.copy(
            authenticationMethod = authenticationMethod,
            emailOrPhoneNumber = text,
            loginButtonEnabled = isLoggeable()
        )
    }

    fun setPassword(text: String) {
        _uiState.value = _uiState.value.copy(
            password = text,
            loginButtonEnabled = isLoggeable()
        )

    }

    private fun isLoggeable(): Boolean {
        return _uiState.value.emailOrPhoneNumber.isNotEmpty() &&
                (_uiState.value.emailOrPhoneNumber.isValidMobileNumber() || _uiState.value.emailOrPhoneNumber.isEmail())
                &&
                (_uiState.value.authenticationMethod == AuthenticationMethods.PHONE_NUMBER || (_uiState.value.authenticationMethod == AuthenticationMethods.EMAIL && _uiState.value.password.isNotEmpty()))
    }


    fun onSignInClick() {

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
            //val result = authService.createUser(_uiState.value.email, _uiState.value.password)
            authService.authenticate(_uiState.value.emailOrPhoneNumber, _uiState.value.password)
            _isProcessing.value = false
        }

    }

    fun onGoogleAuthenticated(idToken: String?, signedInUserName: String) {
        viewModelScope.launch {

            val authCredential =
                dev.gitlive.firebase.auth.GoogleAuthProvider.credential(idToken ?: "", null)
            val authResult = Firebase.auth.signInWithCredential(authCredential)

            authService.saveSession(
                userId = Firebase.auth.currentUser?.uid ?: "",
                token = idToken ?: "",
                userName = signedInUserName
            )

        }

    }

    fun saveProfile(user: User) {
        var pp = 33
        viewModelScope.launch {
            try {
                usersRepository.saveUser(user)
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = exception.message,
                    showErrorMessage = true
                )
            }
        }

        // TODO("Not yet implemented")
    }

    fun onPemissionsUpdate(cameraPermission: Boolean, galleryPermission: Boolean) {
        _uiState.value = _uiState.value.copy(
            haveCameraPermission = cameraPermission,
            haveGalleryPermission = galleryPermission
        )
    }

    fun onImagePickerRequest() {
        _showImagePicker.value = true
    }

    fun onImagePickerCloseRequest() {
        _showImagePicker.value = false
    }


    fun onImagePickedResult(imageByteArray: ByteArray?) {
        _imageProfile.value = imageByteArray
    }

    fun hideImagePicker() {
        _showImagePicker.value = false
        println("cierro la camara")
    }

    fun updateProfile(user: User) {

        if (user.id.isNullOrEmpty()) {
            user.id = Firebase.auth.currentUser?.uid.toString()
        }
        viewModelScope.launch {
            try {
                usersRepository.updateUser(user, _imageProfile.value)
                _uiState.value = _uiState.value.copy(
                    loading = false
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    errorMessage = exception.message,
                    showErrorMessage = true
                )
            }
        }
    }

    fun onNicknameChange(text: String) {
        currentUser.value?.nick = text

    }

    fun onFirstNameChange(text: String) {
        currentUser.value?.firstName = text
        _uiState.value = UiState(loginButtonEnabled = validate())

    }

    fun onLastNameChange(text: String) {
        currentUser.value?.lastName = text
        _uiState.value = UiState(loginButtonEnabled = validate())

    }

    fun onBirthDateChange(text: String) {
        currentUser.value?.birthDate = text
        _uiState.value = UiState(loginButtonEnabled = validate())

    }


    fun onGenderChange(value: Int) {
        currentUser.value?.gender = value
        _uiState.value = UiState(loginButtonEnabled = validate())
    }


    fun saveChanges(
        firstName: String,
        lastName: String,
        gender: Genders,
        birthDate: LocalDate,
    ) {
        _uiState.value = _uiState.value.copy(
            loading = true,

            )


        var auxUser = currentUser.value?.copy(
            firstName = firstName,
            lastName = lastName,
            gender = gender.ordinal,
            birthDate = birthDate.toString(),

            )

        updateProfile(auxUser!!)

    }

    fun getMe(): User? {
        return currentUser.value
    }

    fun getBirthDate(): String {
        return currentUser.value?.birthDate ?: ""
    }

    fun closeErrorDialogRequest() {
        _uiState.value = _uiState.value.copy(
            showErrorMessage = false,
            errorMessage = ""
        )
    }

    fun validate(): Boolean {
        return  validateForm(
            imageProfile = _imageProfile.value,
            firstName = _currentUser.value?.firstName,
            lastName = _currentUser.value?.lastName,
            gender = _currentUser.value?.gender,
            birthDate = _currentUser.value?.birthDate
        )

    }


    data class UiState(
        val loading: Boolean = false,
        val errorMessage: String? = null,
        val showErrorMessage: Boolean = false,
        val emailOrPhoneNumber: String = "",
        val password: String = "",
        val authenticationMethod: AuthenticationMethods = AuthenticationMethods.NONE,
        val loginButtonEnabled: Boolean = false,
        val haveCameraPermission: Boolean = false,
        val haveGalleryPermission: Boolean = false,
    )
}
