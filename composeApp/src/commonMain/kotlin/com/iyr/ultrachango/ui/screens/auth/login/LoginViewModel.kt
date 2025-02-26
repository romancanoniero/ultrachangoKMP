package com.iyr.ultrachango.ui.screens.auth.login

import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.auth.AuthRepository

import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.data.models.enums.AuthenticationMethods
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.utils.extensions.isEmail
import com.iyr.ultrachango.utils.extensions.isValidMobileNumber
import com.iyr.ultrachango.utils.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class LoginViewModel(
    //   private val authenticationRespository: ShoppingListRepository,
    //   private val userViewModel: UserViewModel,
    private val authRepository: AuthRepository,
    private val scaffoldVM: ScaffoldViewModel,
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


    init {/*
                launchWithCatchingException {
                    authService.currentUser.collect {
                        _currentUser.value = it
                    }
                }
        */
        //     var firebaseAuth = Firebase.auth(Firebase.initialize(AppContext.getContext()!!)!!).currentUser
//val pepe = firebaseAuth?.displayName
        viewModelScope.launch {

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
            password = text, loginButtonEnabled = isLoggeable()
        )

    }

    private fun isLoggeable(): Boolean {
        return _uiState.value.emailOrPhoneNumber.isNotEmpty() && (_uiState.value.emailOrPhoneNumber.isValidMobileNumber() || _uiState.value.emailOrPhoneNumber.isEmail()) && (_uiState.value.authenticationMethod == AuthenticationMethods.PHONE_NUMBER || (_uiState.value.authenticationMethod == AuthenticationMethods.EMAIL && _uiState.value.password.isNotEmpty()))
    }


    fun onSignInClick() {

//        _uiState.value.emailOrPhoneNumber.isEmail()
        _isProcessing.value = true
        viewModelScope.launch {
            //val result = authService.createUser(_uiState.value.email, _uiState.value.password)
            if (_uiState.value.emailOrPhoneNumber.isEmail()) {
                val call = authRepository.signInWithEmail(
                    _uiState.value.emailOrPhoneNumber,
                    _uiState.value.password
                )
                _isAuthenticated.value = call?.success ?: false
            } else {
                authRepository.signInWithPhoneNumber(
                    _uiState.value.emailOrPhoneNumber,
                    onSuccess = {
                        _isAuthenticated.value = it.success
                    },
                    onFailure = {
                        _isAuthenticated.value = false
                    },
                    scope = this
                )
                _uiState.value = _uiState.value.copy(
                    showOTP = true
                )
            }

            _isProcessing.value = false
            //  _isAuthenticated.value = call?.success ?:false

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
        viewModelScope.launch(Dispatchers.Main) {
            authRepository.signInWithGoogle(scope = this, onSuccess = { authResult ->
                if (authResult.success) {
                    authRepository.storeUser(authResult.user!!)
                    // aca hay que asegurarse que los datos del usuario esten cargados.
                    _isAuthenticated.value = true
                }
            }, onFailure = {
                println("onFailure")
            })
        }

    }

    fun onOTPCodeEntered(code: String) {
        authRepository.onOTPCodeEntered(code,
            onSuccess = {
                _isAuthenticated.value = it.success
            },
            onFailure = {
                _isAuthenticated.value = false
            },

        )
    }


    data class UiState(

        val loading: Boolean = false,
        val errorMessage: String? = null,
        val showErrorMessage: Boolean = false,
        val emailOrPhoneNumber: String = "+5491161274148",
        val password: String = "",
        val authenticationMethod: AuthenticationMethods = AuthenticationMethods.PHONE_NUMBER,
        val loginButtonEnabled: Boolean = false,
        val showOTP: Boolean = false
    )
}
