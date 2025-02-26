package com.iyr.ultrachango.ui.screens.auth.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.iyr.ultrachango.auth.AuthenticatedUser
import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.data.models.enums.AuthenticationMethods
import com.iyr.ultrachango.ui.dialogs.ErrorDialog
import com.iyr.ultrachango.ui.rootnavigation.RootRoutes
import com.iyr.ultrachango.utils.ui.ShowKeyboard
import com.iyr.ultrachango.utils.ui.elements.CustomButton
import com.iyr.ultrachango.utils.ui.elements.StyleButton
import com.iyr.ultrachango.utils.ui.elements.StyleLight
import com.iyr.ultrachango.utils.ui.elements.customShape
import com.iyr.ultrachango.utils.ui.elements.screenOuterPadding
import com.iyr.ultrachango.utils.ui.elements.textSize26
import com.iyr.ultrachango.utils.ui.elements.textSize28
import com.iyr.ultrachango.utils.ui.showLoader
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import com.iyr.ultrachango.viewmodels.UserViewModel
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.already_have_an_account
import ultrachango2.composeapp.generated.resources.create_an_account
import ultrachango2.composeapp.generated.resources.first_name
import ultrachango2.composeapp.generated.resources.hello_there
import ultrachango2.composeapp.generated.resources.identification_method
import ultrachango2.composeapp.generated.resources.last_name
import ultrachango2.composeapp.generated.resources.lets_registrate
import ultrachango2.composeapp.generated.resources.login_here
import ultrachango2.composeapp.generated.resources.logo_facebook
import ultrachango2.composeapp.generated.resources.logo_google
import ultrachango2.composeapp.generated.resources.or_register_with


@Composable
fun RegisterScreen(
    navController: NavHostController? = null,
    permissionsController: PermissionsController? = null,
    vm: RegisterViewModel = koinViewModel(),

    userViewModel: UserViewModel = koinViewModel()

) {


    var state = vm.uiState.collectAsState()

    //  var authenticationMethod by remember { mutableStateOf(AuthenticationMethods.NONE) }
    var showErrorMessage = state.value.showErrorMessage
    var errorMessage = state.value.errorMessage
    var registerButtonEnabled = state.value.registerButtonEnabled

    var firstName by remember { mutableStateOf(TextFieldValue(state.value.firstName)) }
    var lastName by remember { mutableStateOf(TextFieldValue(state.value.lastName)) }
    var emailOfPhoneNumber by remember { mutableStateOf(TextFieldValue(state.value.emailOrPhoneNumber)) }
    var password by remember { mutableStateOf(TextFieldValue(state.value.password)) }

    // Define a mutable state to hold the state
    var showPassword by remember { mutableStateOf(false) }
    var hideVirtualKeyboard by remember { mutableStateOf(false) }
    if (hideVirtualKeyboard) {
        ShowKeyboard(false)
        hideVirtualKeyboard = false
    }
    var acceptedTerms = remember { mutableStateOf(false) }


    if (state.value.loading) {
        showLoader()
    }
    if (showErrorMessage) {
        ErrorDialog(
            title = "Error",
            message = errorMessage!!,
            onDismissRequest = {
                vm.closeErrorDialogRequest()
            }
        )
    }

    Column(
        modifier = Modifier.background(Color.White).fillMaxSize().systemBarsPadding()
            .clickable {
                hideVirtualKeyboard = true
            }
            .padding(screenOuterPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Welcome message
        Text(
            text = stringResource(Res.string.hello_there),
            fontSize = textSize28,
            color = Color.Blue,
            modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp, 0.dp, 0.dp)
        )
        Text(
            text = stringResource(Res.string.create_an_account),
            fontSize = textSize26,
            color = Color.Blue,
            modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp, 0.dp, 0.dp)
        )

        Spacer(modifier = Modifier.weight(1f))


        OutlinedTextField(value = firstName,
            onValueChange = {
                firstName = it
                vm.setFirstName(it.text)
            },
            label = { Text(stringResource(Res.string.first_name)) },
            modifier = Modifier.fillMaxWidth().background(Color.White, shape = customShape),
            singleLine = true,
            shape = customShape,
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "phone")
            })

        OutlinedTextField(value = lastName,
            onValueChange = {
                lastName = it
                vm.setLastName(it.text)
            },
            label = { Text(stringResource(Res.string.last_name)) },
            modifier = Modifier.fillMaxWidth().background(Color.White, shape = customShape),
            singleLine = true,
            shape = customShape,
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "phone")
            })

        OutlinedTextField(value = emailOfPhoneNumber,
            onValueChange = {
                emailOfPhoneNumber = it
                vm.setEmailOrPassword(it)
            },
            label = {
                Text(
                    text = stringResource(Res.string.identification_method),
                    maxLines = 1,


                    )

            },
            modifier = Modifier.fillMaxWidth().background(Color.White, shape = customShape),
            singleLine = true,
            shape = customShape,
            leadingIcon = {
                val authenticationMethod = state.value.authenticationMethod
                println(authenticationMethod.toString())
                Icon(
                    imageVector = if (state.value.authenticationMethod == AuthenticationMethods.PHONE_NUMBER) Icons.Default.Phone else if (state.value.authenticationMethod == AuthenticationMethods.EMAIL) Icons.Default.Mail else Icons.Default.QuestionMark,
                    contentDescription = if (state.value.authenticationMethod == AuthenticationMethods.PHONE_NUMBER) "phone" else "email"
                )
            })

        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier.alpha(
                if (state.value.authenticationMethod.equals(
                        AuthenticationMethods.EMAIL
                    )
                ) 1f else 0f
            )
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().background(Color.White, shape = customShape)
                    .alpha(if (state.value.authenticationMethod.equals(AuthenticationMethods.EMAIL)) 1f else 0f),
                enabled = (state.value.authenticationMethod.equals(AuthenticationMethods.EMAIL)),
                maxLines = 1,
                singleLine = true,
                value = password,
                onValueChange = {
                    password = it
                    vm.setPassword(it.text)
                },
                label = { Text("Password") },
                shape = customShape,
                leadingIcon = {
                    Icon(Icons.Default.Key, contentDescription = "phone")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        triggerHapticFeedback()
                        showPassword = !showPassword
                    }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = "Show Password"
                        )
                    }
                },
                visualTransformation = if (!showPassword) PasswordVisualTransformation() else VisualTransformation.None,

                )
        }


        Spacer(modifier = Modifier.height(16.dp))


        PrivacyAndTerms(
            vm = vm,
            enableCheck = state.value.conditionsCheckEnabled,
            acceptedTerms = acceptedTerms,
            navController = navController!!
        )


        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            enabled = registerButtonEnabled,
            onClick = {
                triggerHapticFeedback()
                vm.onRegisterClick()
                /*
                              navController?.navigate(RootRoutes.HomeRoute.route) {
                                  //      popUpTo(navController.graph.startDestinationId) { inclusive = true }
                              }
              */
            },
            content = {
                Text(
                    stringResource(Res.string.lets_registrate),
                    style = StyleButton().copy(color = Color.White)
                )
            },
        )



        Spacer(modifier = Modifier.weight(1f))

        MethodDivider(stringResource(Res.string.or_register_with))


        Spacer(modifier = Modifier.height(10.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(modifier = Modifier.fillMaxWidth().weight(1f)
                .background(Color.White, shape = customShape)
                .border(1.dp, Color.LightGray, customShape),
                shape = customShape,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors().copy(
                    containerColor = Color.White, contentColor = Color.Black
                ),
                onClick = { /* Handle Google login */ }) {
                Image(
                    modifier = Modifier.height(36.dp).aspectRatio(1f / 1f).padding(end = 4.dp),
                    painter = painterResource(Res.drawable.logo_google),
                    contentDescription = "Google"
                )
                Text(
                    "Google", style = StyleButton().copy(color = Color.Black)
                )
            }

            /*
                        Row(
                            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
            */

            Spacer(modifier = Modifier.width(4.dp))
            Button(modifier = Modifier.fillMaxWidth().weight(1f)
                .background(Color.White, shape = customShape)
                .border(1.dp, Color.LightGray, customShape),
                shape = customShape,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors().copy(
                    containerColor = Color.White, contentColor = Color.Black
                ),

                onClick = { /* Handle Facebook login */ }) {
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
            //           }
        }

        Spacer(
            modifier = Modifier.fillMaxWidth().height(16.dp)
        )

        Row {
            Text(stringResource(Res.string.already_have_an_account), style = StyleLight())

            Text(
                modifier = Modifier.clickable { navController?.popBackStack() },
                text = stringResource(Res.string.login_here),
                color = Color.Blue
            )

        }

        Spacer(
            modifier = Modifier.fillMaxWidth().height(10.dp)
        )
    }

    LaunchedEffect(Unit) {
/*
        userViewModel.loginUser(

            User(
                id = "333333333",
                nick = "Chancleta",
                email = "",
                phoneNumber = "",
            )
        )
*/

        delay(500)


        //      navController?.navigate(RootRoutes.HomeRoute.route) {
        //    popUpTo(navController.graph.startDestinationId) { inclusive = true }
        //   }
    }
}

@Composable
fun MethodDivider(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier.weight(1f).height(1.dp).background(Color.Gray)
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp), text = text
        )
        Divider(
            modifier = Modifier.weight(1f).height(1.dp).background(Color.Gray)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PrivacyAndTerms(
    vm: RegisterViewModel,
    acceptedTerms: MutableState<Boolean>,
    enableCheck: Boolean,
    navController: NavHostController,
    onClick: (Boolean) -> Unit = {}
) {

    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = acceptedTerms.value,
            onCheckedChange = {
                acceptedTerms.value = it
                vm.setAcceptance(it)
                //    onClick(it)
            }, enabled = enableCheck
        )
        Spacer(modifier = Modifier.width(8.dp).fillMaxWidth().weight(1f))
        FlowRow() {
            Text(
                text = "Continuando aceptas nuestra ", style = StyleLight()
            )
            Text(text = "política de privacidad",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    // Navegar a la página de política de privacidad
                    navController?.navigate("privacy_policy_route")
                })
            Text(
                text = " y los ", style = StyleLight()
            )
            Text(text = "términos y condiciones",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    // Navegar a la página de términos y condiciones
                    navController?.navigate("terms_conditions_route")
                })
        }

    }
}
