package com.iyr.ultrachango.ui.screens.setting.SettingScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.GroupWork
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.iyr.ultrachango.auth.AuthRepository

import com.iyr.ultrachango.ui.dialogs.ConfirmationDialog
import com.iyr.ultrachango.ui.rootnavigation.RootRoutes
import com.iyr.ultrachango.ui.screens.navigation.AppRoutes
import com.iyr.ultrachango.utils.ui.device.getScreenHeight
import com.iyr.ultrachango.utils.ui.elements.MenuItem
import com.iyr.ultrachango.utils.ui.elements.MenuTitle
import com.iyr.ultrachango.utils.ui.elements.ProfilePicturePicker
import com.iyr.ultrachango.utils.ui.elements.screenOuterPadding
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import com.mohamedrejeb.calf.core.LocalPlatformContext
import com.mohamedrejeb.calf.io.readByteArray
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavController,
    permissionController: PermissionsController,
    viewModel: SettingsScreenViewModel,
    authRepository: AuthRepository
) {

    val coroutineScope = rememberCoroutineScope()/*
        coroutineScope.launch {
            Firebase.auth.authStateChanged.collect() { user ->
                if (user != null) {
                    // User is signed in
                    println("User is signed in")
                } else {
                    // No user is signed in
                    println("No user is signed in")

                }
            }
        }
    */
//    val showLogoutDialog by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()


    if (uiState.showLogoutConfirmationDialog) {
        ConfirmationDialog(title = "Cerrar Sesion",
            message = "Estas seguro que deseas cerrar sesion?",
            onAccept = {
                viewModel.hideDialogs()
                coroutineScope.launch {
                    authRepository.signOut()
                    withContext(Dispatchers.Main) {
                        navController?.navigate(RootRoutes.LoginRoute.route)
                    }
                }
            },
            onDismiss = {})

    }



    Column(
        modifier = Modifier.fillMaxSize().padding(screenOuterPadding)
            .verticalScroll(rememberScrollState()),
    ) {


        val deviceHeight = getScreenHeight()

        val scope = rememberCoroutineScope()
        val context = LocalPlatformContext.current
        var imageUri =  viewModel.imageUri.collectAsState()
        var byteArray by remember { mutableStateOf<ByteArray?>(null) }
        val pickerLauncher = rememberFilePickerLauncher(type = FilePickerFileType.Image,
            selectionMode = FilePickerSelectionMode.Single,
            onResult = { files ->
                // Handle the result
                scope.launch {
                    files.firstOrNull()?.let {
                        it
                        byteArray = it.readByteArray(context)
                    }
                }
            })


        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier.fillMaxWidth().height(deviceHeight * .30f),
            contentAlignment = Center

        ) {

            Box(
                modifier = Modifier.fillMaxHeight().aspectRatio(1f),
                contentAlignment = Center
            ) {
                ProfilePicturePicker(
                    modifier = Modifier.fillMaxSize(),
                    scope = coroutineScope,
                    permissionController,
                    onImagePicked = { sharedImage ->
                        viewModel.onImageUriChange(sharedImage?.getUri()!!)
                        viewModel.saveProfilePicture(sharedImage.toByteArray()!!)
                    },
                    image = imageUri.value,
                )
                /*
                                ProfilePicture(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .clip(CircleShape)
                                        .border(2.dp, Color.Black, CircleShape),
                                    imageByteArray = byteArray
                                )
                */
            }

        }






        Card(
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            MenuItem(icon = Icons.Filled.VerifiedUser, text = "Perfil", onClick = {}) {
                triggerHapticFeedback()
                navController.navigate(RootRoutes.ProfileScreenRoute.route)
            }
        }



        Card(
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {

            MenuItem(icon = Icons.Filled.LocationCity, text = "Ubicaciones", onClick = {
                triggerHapticFeedback()
                navController.navigate(AppRoutes.LocationRoute.route)
            })

            MenuItem(icon = Icons.Filled.GroupWork, text = "Grupo Familiar", onClick = {
                triggerHapticFeedback()
                navController.navigate(RootRoutes.MembersRoute.route)

            })

            MenuItem(icon = Icons.Filled.CardGiftcard, text = "Descuentos", onClick = {
                triggerHapticFeedback()
                navController.navigate(AppRoutes.FidelizationRoute.route)

            }) {}
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            MenuTitle(
                text = "Configuraciones",


                )
            MenuItem(icon = Icons.Filled.GpsFixed, text = "Geo Localizacion", onClick = {}) {}


        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {

            MenuTitle(
                text = "Cuenta"
            )

            MenuItem(icon = Icons.AutoMirrored.Filled.Logout, text = "Cerrar Sesion", onClick = {
                triggerHapticFeedback()
                viewModel.onLogoutClick()


            }) {}

            MenuItem(icon = Icons.Outlined.ManageAccounts,
                text = "Eliminar Cuenta",
                onClick = {}) {}

            MenuItem(icon = Icons.Outlined.Info, text = "Acerca de...", onClick = {}) {}


        }


    }
}