@file:OptIn(ExperimentalMaterial3Api::class)

package com.iyr.ultrachango.ui.screens.auth.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.iyr.ultrachango.authModule
import com.iyr.ultrachango.data.models.enums.toGender
import com.iyr.ultrachango.domain.auth.exceptions.UserDataNotFoundException
import com.iyr.ultrachango.domain.auth.models.AppUser
import com.iyr.ultrachango.presentation.auth.AuthErrorType
import com.iyr.ultrachango.ui.dialogs.ErrorDialog
import com.iyr.ultrachango.ui.rootnavigation.RootRoutes
import com.iyr.ultrachango.ui.screens.auth.otp.state.OtpEvent
import com.iyr.ultrachango.ui.screens.auth.otp.state.OtpState
import com.iyr.ultrachango.ui.screens.navigation.AppRoutes
import com.iyr.ultrachango.utils.ui.otp.OtpInputField
import com.iyr.ultrachango.utils.ui.otp.pxToDp
import com.iyr.ultrachango.validateForm
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OtpScreen(
    navController: NavHostController? = null,
    verificationId: String,
    phoneNumber: String,
    onNavigateToHome: (user: AppUser?) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: OtpViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var isResendEnabled by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(60) }
    val otpValue = remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        viewModel.initialize(verificationId, phoneNumber)
    }

    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1000)
            countdown--
        }
        isResendEnabled = true
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Verificación") }, navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            })
        }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (state) {
                is OtpState.CodeSent -> {
                    Text(
                        text = "Ingresa el código enviado al número ${(state as OtpState.CodeSent).phoneNumber}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    OtpTextField(
                        value = otpValue.value,
                        onValueChange = { if (it.length <= 6) otpValue.value = it },
                        modifier = Modifier.fillMaxWidth().height(200.dp).background(Color.Green)
                    )
//------
                    OtpInputField(
                        otp = otpValue,
                        count = 6,
                        textColor = Color.DarkGray,
                        otpBoxModifier = Modifier.border(
                            7.pxToDp(), Color(0xFF277F51), shape = RoundedCornerShape(12.pxToDp())
                        )
                    )

                    if (otpValue.value.length >= 6) {

                        viewModel.onEvent(OtpEvent.VerifyCode(otpValue.value))
                        // vm.onOTPCodeEntered(otpValue.value)
                    }
                    //---------
                    Button(
                        onClick = {
                            viewModel.onEvent(OtpEvent.VerifyCode(otpValue.value))
                        }, enabled = otpValue.value.length == 6, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Verificar")
                    }

                    if (!isResendEnabled) {
                        Text("Reenviar código en $countdown segundos")
                    } else {
                        TextButton(
                            onClick = {
                                viewModel.onEvent(OtpEvent.ResendCode)
                                isResendEnabled = false
                                countdown = 60
                            }) {
                            Text("Reenviar código")
                        }
                    }
                }

                is OtpState.Success -> {
                    LaunchedEffect(Unit) {
                        val user = (state as OtpState.Success).user
                        //       onNavigateToHome(user)

                        val isProfileComplete = validateForm(
                            validateImage = false,
                            firstName = user.firstName,
                            lastName = user.lastName,
                            imageProfile = user.profilePicturePath,
                            gender = user.gender.toGender().name.toString(),
                            birthDate = user.birthDate,
                        )
                        if (isProfileComplete) navController?.navigate(RootRoutes.HomeRoute.route) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                        else navController?.navigate(RootRoutes.SetupProfileRoute.createRoute(user)) {
                            popUpTo(0) {
                                inclusive = true
                            }

                        }
                    }
                }

                is OtpState.Error -> {
                    val errorType = (state as OtpState.Error).errorType
                    when (errorType) {
                        AuthErrorType.USER_DATA_NOT_FOUND -> {
                            LaunchedEffect(Unit) {

                                val newUser = AppUser(
                                    uid = authModule.id,
                                )
                                val route = RootRoutes.SetupProfileRoute.createRoute(newUser)
                                navController?.navigate(route) {
                                    popUpTo(0) {
                                        inclusive = true
                                    }
                                }
                            }
                        }

                        else -> {
                            ErrorDialog(
                                title = "Error",
                                message = (state as OtpState.Error).message,
                                onDismissRequest = {
                                    viewModel.onErrorDialogRequest()
                                })
                        }
                    }
                }

                is OtpState.Loading -> {
                    CircularProgressIndicator()
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun OtpTextField(
    value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value, onValueChange = onValueChange, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done
        ), decorationBox = { innerTextField ->
            Row(
                modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(6) { index ->
                    OtpCell(
                        value = value.getOrNull(index)?.toString() ?: "",
                        isFocused = value.length == index
                    )
                }
            }
            innerTextField()
        }, modifier = Modifier.width(0.dp)
    )
}

@Composable
private fun OtpCell(
    value: String, isFocused: Boolean, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(48.dp).border(
            width = 2.dp, color = if (isFocused) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(8.dp)
        ), contentAlignment = Alignment.Center
    ) {
        Text(
            text = value, style = MaterialTheme.typography.headlineMedium
        )
    }
}