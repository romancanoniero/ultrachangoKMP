package com.iyr.ultrachango.ui.screens.auth.login

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.data.models.enums.AuthenticationMethods
import com.iyr.ultrachango.ui.ScaffoldViewModel

import com.iyr.ultrachango.utils.extensions.isEmail
import com.iyr.ultrachango.utils.extensions.isValidMobileNumber
import com.iyr.ultrachango.utils.firebase.AuthResult
import com.iyr.ultrachango.utils.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import com.iyr.fbauthentication.platform.*
import com.iyr.ultrachango.domain.auth.AuthRepository
import com.iyr.ultrachango.presentation.auth.AuthState
import com.iyr.ultrachango.presentation.auth.AuthViewModel
import kotlinx.coroutines.delay


class LoginViewModel(
    private val authRepository: AuthRepository,
    private val scaffoldVM: ScaffoldViewModel,
    private val authViewModel: AuthViewModel,
    private val firebaseAuthPlatform: FirebaseAuthPlatform
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

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()


    init {
        println("LoginViewModel init")
        firebaseAuthPlatform.initialize()
        println("firebaseAuthPlatform initialized")

        val initialEmail = "pirineorodriguez1@gmail.com"

        val detectedMethod = if (initialEmail.isEmail()) AuthenticationMethods.EMAIL
                             else
                                 if (initialEmail.isValidMobileNumber()) AuthenticationMethods.PHONE_NUMBER
                                    else AuthenticationMethods.NONE

        _uiState.value = _uiState.value.copy(
            emailOrPhoneNumber = initialEmail,
            authenticationMethod = detectedMethod,
            loginButtonEnabled = isLoggeable()
        )
        _uiState.value = _uiState.value.copy(
            loginButtonEnabled = isLoggeable()
        )
  /*
        viewModelScope.launch {
            authViewModel.authState.collect { state ->
                when (state) {
                    is AuthState.Success -> {
                        println("AuthState.Success in LoginViewModel init")
                        _isAuthenticated.value = true
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            showErrorMessage = false,
                            errorMessage = null
                        )
                    }

                    is AuthState.Error -> {
                        println("AuthState.Error in LoginViewModel init")
                        _isAuthenticated.value = false
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            showErrorMessage = true,
                            errorMessage = state.message
                        )
                    }

                    else -> {
                        println("AuthState.Default in LoginViewModel init")
                        _isAuthenticated.value = false
                    }
                }
            }
        }

*/

        println("viewModelScope.launch")
        viewModelScope.launch {
            authViewModel.authState.collect { authState ->
                when (authState) {
                    is AuthState.Loading -> {

                        println("AuthState.Loading")
                        _uiState.value = _uiState.value.copy(
                            loading = true,
                            showErrorMessage = false,
                            errorMessage = null
                        )
                    }

                    is AuthState.Success -> {
                        println("AuthState.Success")
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            showErrorMessage = false,
                            errorMessage = null
                        )
                        _isAuthenticated.value = false
                    }

                    is AuthState.Error -> {
                        println("AuthState.Error")
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            showErrorMessage = true,
                            errorMessage = authState.message
                        )
                        _isAuthenticated.value = false
                    }

                    is AuthState.PhoneVerificationSent -> {
                        println("AuthState.PhoneVerificationSent")
                        delay(2000)
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            showOTP = true
                        )
                    }

                    else -> {
                        println("AuthState.Default")
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            showErrorMessage = false,
                            errorMessage = null
                        )
                    }
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

    fun getEmailOrPhoneNumber(): String {
        return _uiState.value.emailOrPhoneNumber
    }

    fun setPassword(text: String) {
        _uiState.value = _uiState.value.copy(
            password = text, loginButtonEnabled = isLoggeable()
        )
    }

    fun getPassword(): String {
        return _uiState.value.password
    }

    private fun isLoggeable(): Boolean {
        return _uiState.value.emailOrPhoneNumber.isNotEmpty() &&
                (_uiState.value.emailOrPhoneNumber.isValidMobileNumber() || _uiState.value.emailOrPhoneNumber.isEmail()) && (_uiState.value.authenticationMethod == AuthenticationMethods.PHONE_NUMBER || (_uiState.value.authenticationMethod == AuthenticationMethods.EMAIL && _uiState.value.password.isNotEmpty()))
    }


    fun onSignInClick() {

//        _uiState.value.emailOrPhoneNumber.isEmail()
        _isProcessing.value = true
        _uiState.value = _uiState.value.copy(
            loading = true,
            showErrorMessage = false
        )
        viewModelScope.launch {
            //val result = authService.createUser(_uiState.value.email, _uiState.value.password)
            if (_uiState.value.emailOrPhoneNumber.isEmail()) {
                println("signInWithEmail")
                authViewModel.signInWithEmail(
                    _uiState.value.emailOrPhoneNumber,
                    _uiState.value.password
                )
            } else {
                val phoneNumber = _uiState.value.emailOrPhoneNumber
            }
            _isProcessing.value = false
        }

    }

    fun onGoogleAuthenticated(idToken: String?, signedInUserName: String) {
        viewModelScope.launch {

            println("llegue a onGoogleAuthenticated")

            println("idToken = " + idToken)
            //               val authResult = Firebase.auth.signInWithCustomToken(idToken ?: "")

            println(" cumpli!!!!!")


            val accessToken =
                "1077576417175-egmdo9fomdo9865csjbckdv6tqcldk0t.apps.googleusercontent.com"/*aca
                        val authCredential =
                            dev.gitlive.firebase.auth.GoogleAuthProvider.credential(idToken ?: "", accessToken)

                        println("-1")

                        val authResult = Firebase.auth.signInWithCredential(authCredential)

                        println("-2")


                        authService.saveSession(
                            userId = Firebase.auth.currentUser?.uid ?: "",
                            token = idToken ?: "",
                            userName = signedInUserName
                        )
            */
            println("-3")

        }

    }

    fun onSignInWithGoogle() {
        /*
                viewModelScope.launch(Dispatchers.Main) {
              //      authViewModel.signInWithEmailAndPassword("","")
                    authRepository.signInWithGoogle(scope = this, onSuccess = { response ->
                        when(response){
                            is AuthResult.Success -> {
                                authRepository.storeUser(response.user!!)
                                _isAuthenticated.value = true
                            }
                            is AuthResult.Error -> {
                                println(response.message)
                            }
                        }
        /*
                        if (authResult.success) {
                            authRepository.storeUser(authResult.user!!)
                            // aca hay que asegurarse que los datos del usuario esten cargados.
                            _isAuthenticated.value = true
                        }

         */
                    }, onFailure = {
                        println("onFailure")
                    })
                }
        */
    }

    fun onOTPCodeEntered(code: String) {

        /*
              authRepository.onOTPCodeEntered(code,
                  onSuccess = {
                      _isAuthenticated.value = it.success
                  },
                  onFailure = {
                      _isAuthenticated.value = false
                  },

              )
*/

    }

    fun closeErrorDialogRequest() {
        _uiState.value = _uiState.value.copy(
            showErrorMessage = false,
        )
    }


    data class UiState(

        val loading: Boolean = false,
        val errorMessage: String? = null,
        val showErrorMessage: Boolean = false,
        val emailOrPhoneNumber: String = "",
        val password: String = "",
        val authenticationMethod: AuthenticationMethods = AuthenticationMethods.PHONE_NUMBER,
        val loginButtonEnabled: Boolean = emailOrPhoneNumber.isNotEmpty() &&
                (emailOrPhoneNumber.isValidMobileNumber() || emailOrPhoneNumber.isEmail()) && (authenticationMethod == AuthenticationMethods.PHONE_NUMBER || (authenticationMethod == AuthenticationMethods.EMAIL && password.isNotEmpty()))
        ,
        val showOTP: Boolean = false
    )
}
