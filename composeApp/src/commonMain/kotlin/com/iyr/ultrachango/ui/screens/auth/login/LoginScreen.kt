package com.iyr.ultrachango.ui.screens.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.iyr.ultrachango.data.models.enums.AuthenticationMethods
import com.iyr.ultrachango.ui.rootnavigation.RootRoutes
import com.iyr.ultrachango.ui.screens.auth.registration.MethodDivider
import com.iyr.ultrachango.utils.extensions.isEmail
import com.iyr.ultrachango.utils.extensions.isValidMobileNumber
import com.iyr.ultrachango.utils.firebaseauth.AuthViewModel
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
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.KoinApplication.Companion.init
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


) {

    val uiState by vm.uiState.collectAsState()
    val emailError by vm.emailError.collectAsState()
    val passwordError by vm.passwordError.collectAsState()
    val isProcessing by vm.isProcessing.collectAsState()
    val isButtonEnabled by vm.isProcessing.collectAsState()
    val currentUser by vm.currentUser.collectAsState()

    val isAuthenticated by vm.isAuthenticated.collectAsState()


//    MutableState<String>
    val otpValue = remember { mutableStateOf("") }

    var emailOrPhone by remember { mutableStateOf(TextFieldValue("romancanoniero@gmail.com")) }
    var password by remember { mutableStateOf(TextFieldValue("123456")) }

    // Define a mutable state to hold the state
    var showPassword by remember { mutableStateOf(false) }

    if (isAuthenticated) {
        navController?.navigate(RootRoutes.HomeRoute.route)
    } else {


        if (uiState.loading) {
            println("Muestro Loader")
            showLoader()
        }

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

            InputSection(uiState, vm, showPassword, navController, otpValue)

            Spacer(
                modifier = Modifier.fillMaxWidth()
                    .requiredHeight(20.dp)
            )


            OtherLoginOptions(
                modifier = Modifier.fillMaxWidth().weight(1f), vm
            )


            Spacer(
                modifier = Modifier.fillMaxWidth()
                    .requiredHeight(20.dp)
                    .fillMaxHeight()

            )


            LinkToRegister(
                modifier = Modifier.fillMaxWidth(), vm, navController
            )

            /*
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                        )
            */


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
    modifier: Modifier = Modifier, vm: LoginViewModel
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom
    ) {
        //   Box(modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Yellow))

        //Google Sign-In with Custom Button and authentication with Firebase
        MethodDivider(text = stringResource(Res.string.or_login_with))

        Spacer(modifier = Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = customShape)
            .border(1.dp, Color.LightGray, customShape),
            shape = customShape,
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = Color.White, contentColor = Color.Black
            ),
            onClick = {
                triggerHapticFeedback()
                vm.onSignInWithGoogle()
            }) {
            Image(
                modifier = Modifier.height(36.dp).aspectRatio(1f / 1f).padding(end = 4.dp),
                painter = painterResource(Res.drawable.logo_google),
                contentDescription = "Google"
            )
            Text(
                "Google", style = StyleButton().copy(color = Color.Black)
            )
        }



        Spacer(modifier = Modifier.height(8.dp))

        //Apple Sign-In with Custom Button and authentication with Firebase
        //       AppleButtonUiContainer(onResult = onFirebaseResult, linkAccount = false) {

        Button(modifier = Modifier.fillMaxWidth().background(Color.White, shape = customShape)
            .border(1.dp, Color.LightGray, customShape),
            shape = customShape,
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = Color.White, contentColor = Color.Black
            ),
            onClick = {
                //    this.onClick()

            }) {
            Image(
                modifier = Modifier.height(36.dp).aspectRatio(1f / 1f).padding(end = 4.dp),
                painter = painterResource(

                    Res.drawable.logo_facebook
                ),
                contentDescription = "Facebook"
            )
            Text(
                "Facebook", style = StyleButton().copy(color = Color.Black)
            )
        }
        //         }
    }
}

@Composable
private fun InputSection(
    uiState: LoginViewModel.UiState,
    vm: LoginViewModel,
    showPassword: Boolean,
    navController: NavHostController?,
    otpValue: MutableState<String>
) {
    var showPassword1 = showPassword
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        OutlinedTextField(value = uiState.emailOrPhoneNumber,
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
            modifier = Modifier.fillMaxWidth().background(Color.White, shape = customShape),
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
                        showPassword1 = !showPassword1
                    }) {
                        Icon(
                            imageVector = if (showPassword1) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = "Show Password"
                        )
                    }
                },
                visualTransformation = if (!showPassword1) PasswordVisualTransformation() else VisualTransformation.None,

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
                    text = stringResource(Res.string.enter_otp_code)
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
                    vm.onSignInClick()/*
                            authViewModel.signUp(emailOrPhone.text, password.text)
                            {
                                val message = if (it) "SignUp Successful" else "SignUp Failed"
                                println(message)
                            }

                            authViewModel.signIn(emailOrPhone.text, password.text) { success ->
                                val message = if (success) "Login Successful" else "Login Failed"
                                println(message)
                            }
                            */
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