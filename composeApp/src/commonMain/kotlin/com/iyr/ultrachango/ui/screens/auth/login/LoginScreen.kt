package com.iyr.ultrachango.ui.screens.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.iyr.fbauthentication.components.social.AppleAuthButton
import com.iyr.fbauthentication.components.social.FacebookAuthButton
import com.iyr.fbauthentication.components.social.GoogleAuthButton
import com.iyr.fbauthentication.platform.FirebaseAuthPlatform
import com.iyr.ultrachango.config.BuildConfig
import com.iyr.ultrachango.data.models.enums.AuthenticationMethods
import com.iyr.ultrachango.domain.auth.AuthRepository
import com.iyr.ultrachango.ui.rootnavigation.RootRoutes
import com.iyr.ultrachango.ui.screens.auth.registration.MethodDivider
import com.iyr.ultrachango.domain.auth.models.AppUser
import com.iyr.ultrachango.presentation.auth.AuthState
import com.iyr.ultrachango.presentation.auth.AuthViewModel
import com.iyr.ultrachango.ui.dialogs.ErrorDialog
import com.iyr.ultrachango.utils.extensions.isEmail
import com.iyr.ultrachango.utils.extensions.isValidMobileNumber
import com.iyr.ultrachango.utils.ui.elements.Body1Text
import com.iyr.ultrachango.utils.ui.elements.CustomButton
import com.iyr.ultrachango.utils.ui.elements.StyleButton
import com.iyr.ultrachango.utils.ui.elements.StyleLight
import com.iyr.ultrachango.utils.ui.elements.customShape
import com.iyr.ultrachango.utils.ui.elements.screenOuterPadding
import com.iyr.ultrachango.utils.ui.elements.textSize28
import com.iyr.ultrachango.utils.ui.otp.OtpInputField
import com.iyr.ultrachango.utils.ui.otp.pxToDp
import com.iyr.ultrachango.utils.ui.showLoader
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import com.iyr.ultrachango.viewmodels.UserViewModel

import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.ncgroup.kscan.getPlatformName
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.enter_otp_code
import ultrachango2.composeapp.generated.resources.logo_facebook
import ultrachango2.composeapp.generated.resources.logo_google
import ultrachango2.composeapp.generated.resources.or_login_with


@Composable
fun LoginScreen(
    navController: NavHostController? = null,
    permissionsController: PermissionsController? = null,
    vm: LoginViewModel = koinViewModel(),
    userViewModel: UserViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel(),
    onAuthenticated: (AppUser) -> Unit = {}
) {

    val uiState by vm.uiState.collectAsState()
    val isAuthenticated by vm.isAuthenticated.collectAsState()

    // Estados del AuthViewModel
    val authState = authViewModel.authState.collectAsState()
    val loadingState = authViewModel.loadingState.collectAsState()

    // Escuchar eventos de autenticación para manejar casos especiales
    LaunchedEffect(Unit) {
        authViewModel.authEvents.collect { event ->
            when (event) {
                is com.iyr.ultrachango.presentation.auth.AuthEvent.RequireProfileCompletion -> {
                    // Obtener el usuario actual y redirigir a completar perfil
                    val currentUser = authViewModel.getCurrentUser()
                    navController?.navigate(
                        com.iyr.ultrachango.ui.rootnavigation.RootRoutes.SetupProfileRoute.createRoute(currentUser)
                    )
                }
                // ... otros eventos pueden manejarse aquí en el futuro
                else -> { /* No hacer nada para otros eventos */ }
            }
        }
    }

//    MutableState<String>
    val otpValue = remember { mutableStateOf("") }

    var emailOrPhone by remember { mutableStateOf(TextFieldValue("romancanoniero@gmail.com")) }
    var password by remember { mutableStateOf(TextFieldValue("123456")) }

    // Define a mutable state to hold the state
    var showPassword by remember { mutableStateOf(false) }


    if (uiState.showErrorMessage) {
        ErrorDialog(
            title = "Error",
            message = uiState.errorMessage.toString(),
            onDismissRequest = {
                vm.closeErrorDialogRequest()
            }
        )
    }


    if (isAuthenticated) {
        navController?.navigate(RootRoutes.HomeRoute.route)
    } else {
        when (val result = authState.value) {
            is AuthState.PhoneVerificationSent -> {
                LaunchedEffect(result) {
                    navController?.navigate(
                        RootRoutes.OtpVerificationRoute.createRoute(
                            verificationId = result.verificationId,
                            phoneNumber = result.phoneNumber
                        )
                    )
                }
            }

            // ... otros estados
            is AuthState.Error -> {
                val implementar = 33
            }

            AuthState.Initial -> {}
            AuthState.Loading -> {
                // El loading se maneja ahora con loadingState
            }

            is AuthState.Success -> {
                LaunchedEffect(Unit) {
                    onAuthenticated(result.user)
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
        )
        {


            Column(
                modifier = Modifier.background(Color.White).fillMaxSize().systemBarsPadding()
                    .padding(screenOuterPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Welcome message
                Header()

                Spacer(
                    modifier = Modifier.fillMaxWidth()
                        .requiredHeight(30.dp)
                        .fillMaxHeight()
                )

                InputSection(uiState, vm, authViewModel, showPassword, navController, otpValue)

                Spacer(
                    modifier = Modifier.fillMaxWidth()
                        .requiredHeight(20.dp)
                )


                OtherLoginOptions(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    vm = vm,
                    authViewModel = authViewModel
                )


                Spacer(
                    modifier = Modifier.fillMaxWidth()
                        .requiredHeight(20.dp)
                        .fillMaxHeight()

                )


                LinkToRegister(
                    modifier = Modifier.fillMaxWidth(), vm, navController
                )
            }


            // Mostrar loader basado en el loadingState del AuthViewModel
            if (loadingState.value.isLoading) {
                println("Muestro Loader: ${loadingState.value.message}")
                showLoader()
            }
            // También mantener el loading del LoginViewModel para compatibilidad
            else if (uiState.loading) {
                println("Muestro Loader del LoginViewModel")
                showLoader()
            }
        }
        LaunchedEffect(Unit) {
            //   delay(500)
        }
    }
}

@Composable
fun LinkToRegister(
    modifier: Modifier = Modifier, viewModel: LoginViewModel, navController: NavHostController?
) {
    Row(modifier = modifier.fillMaxWidth().background(Color.Green)) {
        Text("No tienes tu cuenta aun?")

        Text(
            modifier = Modifier.clickable { navController?.navigate(RootRoutes.RegisterRoute.route) },
            text = "Registrate aqui",
            color = Color.Blue
        )

    }

}


@Composable
private fun OtherLoginOptions(
    modifier: Modifier = Modifier,
    vm: LoginViewModel,
    authViewModel: AuthViewModel,
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom
    ) {
        //   Box(modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Yellow))

        //Google Sign-In with Custom Button and authentication with Firebase
        MethodDivider(text = stringResource(Res.string.or_login_with))

        Spacer(modifier = Modifier.height(10.dp))

        GoogleAuthButton(
            onClick = {
                triggerHapticFeedback()
                // vm.onSignInWithGoogle()
                scope.launch {
                    println("Google Sign-In clicked")
                    authViewModel.signInWithGoogle()
                }
            },
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 4.dp),
            shape = RoundedCornerShape(4.dp),
            enabled = true
        )

        Spacer(modifier = Modifier.height(4.dp))

        /*
                FacebookAuthButton(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    onClick = {
                        triggerHapticFeedback()
                        // vm.onSignInWithFacebook()
                        scope.launch {
                            authViewModel.signInWithFacebook()
                        }
                    },
                    shape = RoundedCornerShape(4.dp),
                    icon = painterResource(Res.drawable.logo_facebook),
                    enabled = true
                )
        */
        Spacer(modifier = Modifier.height(4.dp))

        if (getPlatformName().equals("iOS")) {
            AppleAuthButton(
                modifier = Modifier.fillMaxWidth().background(Color.White)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                    .padding(bottom = 4.dp),

                onClick = {
                    triggerHapticFeedback()
                    // vm.onSignInWithApple()
                    scope.launch {
                        // authViewModel.signInWithApple()
                    }
                },
                enabled = true
            )
        }

    }
}


@Composable
private fun InputSection(
    uiState: LoginViewModel.UiState,
    vm: LoginViewModel,
    authViewModel: AuthViewModel,
    showPassword: Boolean,
    navController: NavHostController?,
    otpValue: MutableState<String>
) {
    var showPasswordState by remember { mutableStateOf(showPassword) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        OutlinedTextField(
            value = uiState.emailOrPhoneNumber,
            onValueChange = {
                var authenticationMethod = AuthenticationMethods.NONE
                //  emailOrPhone = it
                if (it.isValidMobileNumber()) {
                    authenticationMethod = AuthenticationMethods.PHONE_NUMBER
                } else if (it.isEmail()) {
                    authenticationMethod = AuthenticationMethods.EMAIL
                } else authenticationMethod = AuthenticationMethods.NONE

                vm.setMailOrPhone(authenticationMethod, it)
            },
            label = { Text("Email or Phone") },
            modifier = Modifier

                        .fillMaxWidth()
                        .background(Color.White, shape = customShape),
            singleLine = true,
            shape = customShape,
            leadingIcon = {
                //    Icon(Icons.Default.Person, contentDescription = "phone")

                val authenticationMethod = uiState.authenticationMethod
                Icon(
                    imageVector = if (authenticationMethod == AuthenticationMethods.PHONE_NUMBER) {
                        Icons.Default.Phone
                    } else if (authenticationMethod == AuthenticationMethods.EMAIL) {
                        Icons.Default.Mail
                    } else {
                        Icons.Default.QuestionMark
                    },
                    contentDescription = if (authenticationMethod == AuthenticationMethods.PHONE_NUMBER) "phone" else "email"
                )

                Icon(
                    imageVector = if (authenticationMethod == AuthenticationMethods.PHONE_NUMBER) {
                        Icons.Default.Phone
                    } else if (authenticationMethod == AuthenticationMethods.EMAIL) {
                        Icons.Default.Mail
                    } else {
                        Icons.Default.QuestionMark
                    },
                    contentDescription = if (authenticationMethod == AuthenticationMethods.PHONE_NUMBER) "phone" else "email"
                )

            })

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.alpha(
                if (uiState.authenticationMethod.equals(
                        AuthenticationMethods.EMAIL
                    )
                ) 1f else 0f
            )
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().background(Color.White, shape = customShape),
                enabled = (uiState.authenticationMethod.equals(AuthenticationMethods.EMAIL)),
                singleLine = true,
                value = uiState.password,
                onValueChange = {
                    //   password = it
                    vm.setPassword(it)
                },
                label = { Text("Password") },
                shape = customShape,
                leadingIcon = {
                    Icon(Icons.Default.Key, contentDescription = "passord")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        triggerHapticFeedback()
                        showPasswordState = !showPasswordState
                    }) {
                        Icon(
                            imageVector = if (showPasswordState) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = "Show Password"
                        )
                    }
                },
                visualTransformation = if (!showPasswordState) PasswordVisualTransformation() else VisualTransformation.None,

                )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 2.dp).align(Alignment.End),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    modifier = Modifier.clickable { navController?.navigate(RootRoutes.ForgotPasswordRoute.route) },
                    text = "Forgot Password?",
                    style = StyleLight(),
                )
            }
        }

        if (uiState.showOTP) {
            Column {
                Body1Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.enter_otp_code),
                    fontWeight = FontWeight.ExtraBold
                )

                OtpInputField(
                    otp = otpValue,
                    count = 6,
                    textColor = Color.DarkGray,
                    otpBoxModifier = Modifier.border(
                        7.pxToDp(), Color(0xFF277F51), shape = RoundedCornerShape(12.pxToDp())
                    )
                )
            }
            if (otpValue.value.length >= 6) {
                vm.onOTPCodeEntered(otpValue.value)
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.loginButtonEnabled,
                onClick = {
                    triggerHapticFeedback()

                    val emailOrPhone = vm.getEmailOrPhoneNumber()

                    if (emailOrPhone.isValidMobileNumber()) {
                        authViewModel.signInWithPhone(emailOrPhone)
                    } else
                        if (emailOrPhone.isEmail()) {
                            //   authViewModel.signInWithEmailAndPassword(emailOrPhone, vm.getPassword())
                            authViewModel.signInWithEmail(emailOrPhone, vm.getPassword())
                        }


                },
                content = {
                    Text(
                        "Login", style = StyleButton().copy(color = Color.White)
                    )
                },

                )

        }
    }
}

@Composable
private fun Header(modifier: Modifier = Modifier) {
    Text(

        text = "Hello,\nWelcome to the login page",
        fontSize = textSize28,
        color = Color.LightGray,
        modifier = modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 0.dp)
    )
}