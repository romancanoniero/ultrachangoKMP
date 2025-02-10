package com.iyr.ultrachango.ui.screens.auth.forgot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.iyr.ultrachango.ui.rootnavigation.RootRoutes
import com.iyr.ultrachango.utils.ui.elements.CustomButton
import com.iyr.ultrachango.utils.ui.elements.StyleButton
import com.iyr.ultrachango.utils.ui.elements.customShape
import com.iyr.ultrachango.utils.ui.elements.screenOuterPadding
import com.iyr.ultrachango.utils.ui.elements.textSize20
import com.iyr.ultrachango.utils.ui.elements.textSize28
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import com.iyr.ultrachango.viewmodels.UserViewModel
import dev.icerock.moko.permissions.PermissionsController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.back_to_login
import ultrachango2.composeapp.generated.resources.continuar
import ultrachango2.composeapp.generated.resources.correo_electronico


@Composable
fun ForgotPasswordScreen(
    navController: NavHostController? = null,
    permissionsController: PermissionsController? = null,
    userViewModel: UserViewModel = koinViewModel()

) {
    var email by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier.background(Color.White).fillMaxSize().systemBarsPadding()
            .padding(screenOuterPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Welcome message
        Column(
            modifier = Modifier.fillMaxWidth()

        ) {

            Text(
                text = "Forgot Password",
                fontSize = textSize28,
                color = Color.Blue,
                modifier = Modifier.fillMaxWidth().padding(0.dp, 50.dp, 0.dp, 0.dp)
            )
            //  Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Enter your email acount to reset your password",
                fontSize = textSize20,
                modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp, 0.dp, 0.dp)

            )

        }


        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(value = email,
            onValueChange = { email = it },
            label = { Text(text = stringResource(Res.string.correo_electronico)) },
            modifier = Modifier.fillMaxWidth().background(Color.White, shape = customShape),
            shape = customShape,
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "phone")
            })


        Spacer(modifier = Modifier.weight(1f))


        //       Spacer(modifier = Modifier.height(16.dp))


        Column(modifier = Modifier.fillMaxWidth()) {
            CustomButton(modifier = Modifier.fillMaxWidth(), onClick = {
                triggerHapticFeedback()
                navController?.navigate(RootRoutes.HomeRoute.route) {
              
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }

            }, content = {
                Text(
                    text = stringResource(Res.string.continuar),
                    style = StyleButton().copy(color = Color.White)
                )
            },
            )

            Spacer(modifier = Modifier.height(6.dp))

            CustomButton(modifier = Modifier.fillMaxWidth(), onClick = {
                triggerHapticFeedback()
                navController?.popBackStack()
            }, content = {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "back")
                Text(
                    text = stringResource(Res.string.back_to_login),
                    style = StyleButton().copy(color = Color.Black),
                )
            },
            )
        }


        //      Spacer(modifier = Modifier.weight(1f))


    }


}