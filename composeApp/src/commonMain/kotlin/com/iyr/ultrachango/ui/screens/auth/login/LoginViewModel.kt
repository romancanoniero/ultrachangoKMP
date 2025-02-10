package com.iyr.ultrachango.ui.screens.auth.login

import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.auth.AuthRepository
import com.iyr.ultrachango.auth.AuthRepositoryImpl
import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.data.models.enums.AuthenticationMethods
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.utils.extensions.isEmail
import com.iyr.ultrachango.utils.extensions.isValidMobileNumber
import com.iyr.ultrachango.utils.viewmodel.BaseViewModel
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.app
import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class LoginViewModel(
    //   private val authenticationRespository: ShoppingListRepository,
    //   private val userViewModel: UserViewModel,
    private val authService: AuthRepositoryImpl,
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


    init {
/*
        launchWithCatchingException {
            authService.currentUser.collect {
                _currentUser.value = it
            }
        }
*/
        //     var firebaseAuth = Firebase.auth(Firebase.initialize(AppContext.getContext()!!)!!).currentUser
//val pepe = firebaseAuth?.displayName
        viewModelScope.launch {


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
            password = text, loginButtonEnabled = isLoggeable()
        )

    }

    private fun isLoggeable(): Boolean {
        return _uiState.value.emailOrPhoneNumber.isNotEmpty() && (_uiState.value.emailOrPhoneNumber.isValidMobileNumber() || _uiState.value.emailOrPhoneNumber.isEmail()) && (_uiState.value.authenticationMethod == AuthenticationMethods.PHONE_NUMBER || (_uiState.value.authenticationMethod == AuthenticationMethods.EMAIL && _uiState.value.password.isNotEmpty()))
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

            println("llegue a onGoogleAuthenticated")

            println("idToken = " + idToken)
            //               val authResult = Firebase.auth.signInWithCustomToken(idToken ?: "")

            println(" cumpli!!!!!")



            val accessToken = "1077576417175-egmdo9fomdo9865csjbckdv6tqcldk0t.apps.googleusercontent.com"

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

            println("-3")

        }

    }


    data class UiState(

        val loading: Boolean = false,
        val errorMessage: String? = null,
        val showErrorMessage: Boolean = false,
        val emailOrPhoneNumber: String = "",
        val password: String = "",
        val authenticationMethod: AuthenticationMethods = AuthenticationMethods.NONE,
        val loginButtonEnabled: Boolean = false,
    )
}
