package com.iyr.ultrachango.ui.screens.auth.config.profile


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.data.database.repositories.ImagesRepository
import com.iyr.ultrachango.data.database.repositories.UserRepositoryImpl
import com.iyr.ultrachango.data.models.enums.AuthenticationMethods
import com.iyr.ultrachango.data.models.enums.Genders
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.utils.auth_by_cursor.models.AppUser
import com.iyr.ultrachango.utils.auth_by_cursor.repository.AuthRepository
import com.iyr.ultrachango.utils.extensions.isEmail
import com.iyr.ultrachango.utils.extensions.isValidMobileNumber
import com.iyr.ultrachango.utils.viewmodel.BaseViewModel
import com.iyr.ultrachango.validateForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent

class RegistrationProfileViewModel<T>(
    private val initialData: AppUser?,
    private val authRepository: AuthRepository,
    private val usersRepository: UserRepositoryImpl,
    private val imagesRepository: ImagesRepository,
    private val scaffoldVM: ScaffoldViewModel,
) : BaseViewModel(), KoinComponent {

    private val _uiState = MutableStateFlow(
        UiState(
            initialData,
            initialData ?: AppUser(authRepository.getUserKey()!!)
        )
    )
    val uiState = _uiState.asStateFlow()


    private val _originalUser = MutableStateFlow<AppUser?>(null)
    val originalUser = _originalUser.asStateFlow()

    private val _currentUser = MutableStateFlow<AppUser?>(null)
    val currentUser = _currentUser.asStateFlow()


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

        initializeWithUser()

        val me = authRepository.getCurrentUser()
        //       _originalUser.value = me?.copy()
        //      _currentUser.value = me


        //     var firebaseAuth = Firebase.auth(Firebase.initialize(AppContext.getContext()!!)!!).currentUser
//val pepe = firebaseAuth?.displayName
        try {
            //         viewModelScope.launch {

            me?.uid?.let { userKey: String ->
                viewModelScope.launch {
                    imagesRepository.getProfileImageURL(userKey)?.let {
                        _imageProfile.value = it
                        //    _uiState.value = UiState(loginButtonEnabled = validate())
                        updateField(Fields.PROFILE_IMAGE_PATH, "IMAGE")


                    }
                }


            }
            /*aca
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

                 */
            //          }
        } catch (e: Exception) {
            println("Error al cargar la imagen")
        }


    }


    private fun initializeWithUser() {
        _uiState.value = _uiState.value.copy(
            originalData = initialData,
            currentData = initialData ?: AppUser(authRepository.getUserKey()!!)
        )

        val pp = 3
        // _uiState.value
        /*
         currentUser?.let { user ->
             _uiState.update { state ->

                 state.copy(
                     name = user.name,
                     email = user.email,
                     // ... otros campos
                 )
             }
         }

         */
    }

    enum class Fields {
        FIRST_NAME,
        LAST_NAME,
        GENDER,
        BIRTH_DATE,
        PROFILE_IMAGE_PATH
    }


    fun updateField(field: Fields, value: Any?) {

        var modifiedData = _uiState.value.copy().currentData
        when (field) {
            Fields.FIRST_NAME -> modifiedData.firstName = value as String
            Fields.LAST_NAME -> modifiedData.familyName = value as String
            Fields.GENDER -> modifiedData.gender = value as Genders
            Fields.BIRTH_DATE -> modifiedData.birthDate = value as String?
            Fields.PROFILE_IMAGE_PATH -> modifiedData.profilePictureUrl as String
        }


       viewModelScope.launch {
           _uiState.emit(_uiState.value.copy(
               currentData = modifiedData,
               isDirty = modifiedData != _uiState.value.originalData,
               isValid = validateData(modifiedData)
           ))

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

    /*
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
               authRepository.signUpWithEmail(_uiState.value.emailOrPhoneNumber, _uiState.value.password)
               _isProcessing.value = false
           }

       }
    */
    fun onGoogleAuthenticated(idToken: String?, signedInUserName: String) {
        viewModelScope.launch {
/*aca
           val authCredential =
               dev.gitlive.firebase.auth.GoogleAuthProvider.credential(idToken ?: "", null)
           val authResult = Firebase.auth.signInWithCredential(authCredential)

           authService.saveSession(
               userId = Firebase.auth.currentUser?.uid ?: "",
               token = idToken ?: "",
               userName = signedInUserName
           )


 */
        }

    }

    fun saveProfile(user: AppUser) {
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

    fun updateProfile(user: AppUser) {

        if (user.uid.isNullOrEmpty()) {

            user.uid =
                authRepository.getUserKey()!! //"" /*aca Firebase.auth.currentUser?.uid.toString() */

        }
        viewModelScope.launch {
            try {
                usersRepository.updateUser(user, _imageProfile.value)
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    isComplete = true
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
        currentUser.value?.displayName = text

    }

    fun onFirstNameChange(text: String) {
//        currentUser.value?.firstName = text
//        _uiState.value = UiState(loginButtonEnabled = validate())
        updateField(Fields.FIRST_NAME, text)
    }

    fun onLastNameChange(text: String) {
        //      currentUser.value?.familyName = text
        //     _uiState.value = UiState(loginButtonEnabled = validate())
        updateField(Fields.LAST_NAME, text)
    }

    fun onBirthDateChange(text: String) {
//        currentUser.value?.birthDate = text
//        _uiState.value = UiState(loginButtonEnabled = validate())
        updateField(Fields.BIRTH_DATE, text)
    }


    fun onGenderChange(gender: Genders) {
//        currentUser.value?.gender = gender
//        _uiState.value = UiState(loginButtonEnabled = validate())
        updateField(Fields.GENDER, gender)

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
            familyName = lastName,
            gender = gender,
            birthDate = birthDate.toString(),

            )

        updateProfile(auxUser!!)

    }

    fun getMe(): AppUser? {
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

    fun validateData(data: AppUser): Boolean {

        val errors = mutableMapOf<String, String>()

        return validateForm(
            imageProfile = "_imageProfile.value",
            firstName = _currentUser.value?.firstName,
            lastName = _currentUser.value?.familyName,
            gender = _currentUser.value?.gender,
            birthDate = _currentUser.value?.birthDate
        )

        if ((data.firstName ?: "").isBlank()) {
            errors["name"] = "El nombre es requerido"
        }

        if ((data.familyName ?: "").isBlank()) {
            errors["familyName"] = "El apellido es requerido"
        }

        if (data.gender == null) {
            errors["gender"] = "El genero es requerido"
        }

        if (data.birthDate == null) {
            errors["birthDate"] = "Fecha de Nacimiento inválido"
        }

        _uiState.update { it.copy(errors = errors) }
        return errors.isEmpty()


    }

    fun resetFocus() {
        _uiState.update {
            it.copy(
                showDatePicker = false
            )
        }
    }

    fun toggleDatePicker() {
        _uiState.update {
            it.copy(
                showDatePicker = !it.showDatePicker
            )
        }
    }


    data class UiState(
        val originalData: AppUser?,
        val currentData: AppUser,
        val isValid: Boolean = false,
        val isDirty: Boolean = false,
        val errors: Map<String, String> = emptyMap(),
        val loading: Boolean = false,
        val errorMessage: String? = null,
        val showErrorMessage: Boolean = false,
        val emailOrPhoneNumber: String = "",
        val password: String = "",
        val authenticationMethod: AuthenticationMethods = AuthenticationMethods.NONE,
        val loginButtonEnabled: Boolean = false,
        val haveCameraPermission: Boolean = false,
        val haveGalleryPermission: Boolean = false,
        val isComplete: Boolean = false,
        val showDatePicker: Boolean = false

    )

}

