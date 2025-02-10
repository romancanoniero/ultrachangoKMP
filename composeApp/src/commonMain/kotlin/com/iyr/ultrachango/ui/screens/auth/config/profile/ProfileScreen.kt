package com.iyr.ultrachango.ui.screens.auth.config.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.Uri
import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.data.models.enums.Genders
import com.iyr.ultrachango.ui.dialogs.ErrorDialog
import com.iyr.ultrachango.utils.ui.LoadingDialog
import com.iyr.ultrachango.utils.ui.camera_gallery.rememberCameraManager
import com.iyr.ultrachango.utils.ui.camera_gallery.rememberGalleryManager
import com.iyr.ultrachango.utils.ui.component.ImageOptionDialog
import com.iyr.ultrachango.utils.ui.elements.GenderSelector
import com.iyr.ultrachango.utils.ui.elements.RegularButton
import com.iyr.ultrachango.utils.ui.elements.StyleTextBig
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import com.mohamedrejeb.calf.picker.toImageBitmap

import dev.darkokoa.datetimewheelpicker.WheelDatePicker
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
import dev.darkokoa.datetimewheelpicker.core.format.MonthDisplayStyle
import dev.darkokoa.datetimewheelpicker.core.format.dateFormatter
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.gender_female
import ultrachango2.composeapp.generated.resources.gender_male
import ultrachango2.composeapp.generated.resources.gender_other
import ultrachango2.composeapp.generated.resources.profile_pic


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    currentUser: User? = null,
    navController: NavController? = null,
    permissionsController: PermissionsController? = null,
    viewModel: ProfileViewModel = koinViewModel()
) {
    //  val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var launchCamera by remember { mutableStateOf(value = false) }
    var launchGallery by remember { mutableStateOf(value = false) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val profileImageBitmap by viewModel.imageProfile.collectAsState()



    var nickname by remember { mutableStateOf(currentUser?.nick) }
    var firstName by remember { mutableStateOf(currentUser?.firstName) }
    var lastName by remember { mutableStateOf(currentUser?.lastName) }
    var gender by remember {
        mutableStateOf<Genders?>(
            Genders.entries.get(
                currentUser?.gender ?: 0
            )
        )
    }
    var birthDate by remember { mutableStateOf(currentUser?.birthDate) }


    val uiState by viewModel.uiState.collectAsState()

    var currentDateArray: Array<Int>? = null
    val currentDate = Clock.System.now().toLocalDateTime(
        TimeZone.currentSystemDefault()
    )
    currentDateArray = arrayOf(
        currentDate.year, currentDate.monthNumber, currentDate.dayOfMonth
    )


    var birthDateAsSnappedDateTime by remember {

        val birthDateParts = birthDate?.split("-")
        val birthYear = birthDateParts?.getOrNull(0)?.toIntOrNull()
        val birthMonth = birthDateParts?.getOrNull(1)?.toIntOrNull()
        val birthDay = birthDateParts?.getOrNull(2)?.toIntOrNull()

        mutableStateOf<kotlinx.datetime.LocalDate?>(
            LocalDate(
                year = birthYear ?: currentDate.year,
                monthNumber = birthMonth ?: currentDate.monthNumber,
                dayOfMonth = birthDay ?: currentDate.dayOfMonth
            )
        )
    }


    var showDatePicker by remember { mutableStateOf(false) }


    /*
        viewModel.getMe()?.let { it ->
            it?.let {
                nickname = it.nick
                firstName = it.firstName.toString()
                lastName = it.lastName.toString()
                birthDate = it.birthDate.toString()
                gender = it.gender.toString()
            }
        }
    */

    permissionsController?.let { it ->
        BindEffect(it)

    }



    val showImagePicker by viewModel.showImagePicker.collectAsState()/*
        val cameraManager = rememberCameraManager {
            coroutineScope.launch {
                val bitmap = withContext(Dispatchers.Default) {
                    it?.toImageBitmap()
                }
                imageBitmap = bitmap
            }
        }

        val galleryManager = rememberGalleryManager {
            coroutineScope.launch {
                val bitmap = withContext(Dispatchers.Default) {
                    it?.toImageBitmap()
                }
                imageBitmap = bitmap
            }
        }

    */
    if (showImagePicker) {
        ImageOptionDialog(onDismissRequest = {
            viewModel.onImagePickerCloseRequest()
        }, onGalleryRequest = {
            launchGallery = true
        }, onCameraRequest = {
            launchCamera = true
        })

    }

    if (launchCamera || launchGallery) {
        if (launchCamera) {
            val cameraManager = rememberCameraManager {
                coroutineScope.launch {
                    val bitmap = withContext(Dispatchers.Default) {
                        it?.toImageBitmap()
                    }
                    imageBitmap = bitmap
                    viewModel.onImagePickedResult(it?.toByteArray())
                    viewModel.hideImagePicker()
                    launchGallery = false
                    launchCamera = false
                }

            }
            LaunchedEffect(Unit) {
                cameraManager.launch()
            }
        } else if (launchGallery) {
            val galleryManager = rememberGalleryManager {
                coroutineScope.launch {
                    val bitmap = withContext(Dispatchers.Default) {
                        it?.toImageBitmap()
                    }
                    imageBitmap = bitmap
                    viewModel.onImagePickedResult(it?.toByteArray())
                    viewModel.hideImagePicker()
                    launchGallery = false
                    launchCamera = false
                }
            }
            LaunchedEffect(Unit) {
                galleryManager.launch()
            }


        }
    } else {

        if (uiState.showErrorMessage) {
            ErrorDialog(
                title = "Error",
                message = uiState.errorMessage.toString(),
                onDismissRequest = {
                    viewModel.closeErrorDialogRequest()
                }
            )
        }

        if (uiState.loading) {
            LoadingDialog()
        }


        Column(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(
                top = 40.dp, bottom = 20.dp
            ).padding(horizontal = 16.dp)
        ) {
            Box(modifier = Modifier.size(120.dp).background(Color.Gray, shape = CircleShape)
                .clip(CircleShape).align(androidx.compose.ui.Alignment.CenterHorizontally)
                .clickable {
                    coroutineScope.launch(Dispatchers.IO) {
                        //      var cameraPermission = false
                        //      var galleryPermission = false
                        var isAnyDenied = false

                        permissionsController?.let { permissionsController ->
                            var cameraPermission =
                                permissionsController.isPermissionGranted(Permission.CAMERA)
                            if (!cameraPermission) {
                                try {
                                    permissionsController?.providePermission(Permission.CAMERA)
                                    // Permission has been granted successfully.
                                    cameraPermission = true
                                } catch (deniedAlways: DeniedAlwaysException) {
                                    // Permission is always denied.
                                } catch (denied: DeniedException) {
                                    // Permission was denied.
                                }
                            }

                            var galleryPermission =
                                permissionsController.isPermissionGranted(Permission.GALLERY)
                            if (!galleryPermission) {
                                try {
                                    permissionsController?.providePermission(Permission.GALLERY)
                                    // Permission has been granted successfully.
                                    galleryPermission = true
                                } catch (deniedAlways: DeniedAlwaysException) {
                                    // Permission is always denied.
                                } catch (denied: DeniedException) {
                                    // Permission was denied.
                                }
                            }
                            viewModel.onPemissionsUpdate(cameraPermission, galleryPermission)
                            if (cameraPermission || galleryPermission) {
                                viewModel.onImagePickerRequest()
                            }
                        }
                    }
                }) {
                profileImageBitmap?.let {
                    Image(
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        bitmap = it.toImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                } ?: run {
                    Image(
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        painter = painterResource(Res.drawable.profile_pic),
                        contentDescription = null,
                        contentScale = ContentScale.Crop

                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))/*
                        OutlinedTextField(value = nickname ?: "", onValueChange = {
                            nickname = it
                            viewModel.onNicknameChange(it)
                        }, label = { Text("Apodo") }, modifier = Modifier.fillMaxWidth()
                        )
            */
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = firstName ?: "", onValueChange = {
                firstName = it
                viewModel.onFirstNameChange(it)
            }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = lastName ?: "", onValueChange = {
                lastName = it
                viewModel.onLastNameChange(it)
            }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))/*
                        OutlinedTextField(
                            value = gender,
                            onValueChange = { gender = it },
                            label = { Text("Género (masculino, femenino, otro)") },
                            modifier = Modifier.fillMaxWidth()
                        )
            */
            GenderSelector(
                value = gender ?: Genders.MALE,
                onGenderSelected = { value ->
                    gender = Genders.values().get(value)
               viewModel.onGenderChange(value)
                }
            )


            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(enabled = false,
                value = birthDate.toString(),
                onValueChange = { birthDate = it },

                label = { Text("Fecha de Nacimiento") },
                modifier = Modifier.fillMaxWidth().clickable {
                    showDatePicker = !showDatePicker
                })


            if (showDatePicker) {


                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
/*

                    if (birthDateAsSnappedDateTime == null) {

                    } else {
                        val splity = viewModel.getBirthDate().split("-")
                        currentDateArray = arrayOf(
                            splity!![0].toInt(), splity[1].toInt(), splity[2].toInt()
                        )

                    }
*/


                    WheelDatePicker(
                        modifier = Modifier.fillMaxWidth(), dateFormatter = dateFormatter(
                            locale = Locale.current, monthDisplayStyle = MonthDisplayStyle.FULL
                        ),
                        startDate = birthDateAsSnappedDateTime!!,

                        minDate = LocalDate(
                            year = 1900,
                            monthNumber = 1,
                            dayOfMonth = 1,
                        ), maxDate = LocalDate(
                            year = currentDateArray!![0],
                            monthNumber = currentDateArray!![1],
                            dayOfMonth = currentDateArray!![2],
                        ), size = DpSize(200.dp, 100.dp), rowCount = 5, textStyle = StyleTextBig(),
                        // textColor = Color(0xFFffc300),
                        selectorProperties = WheelPickerDefaults.selectorProperties(
                            enabled = true,
                            shape = RoundedCornerShape(0.dp),
                            color = Color.Black.copy(alpha = 0.2f),
                            border = BorderStroke(2.dp, Color(0xFFf1faee))
                        )
                    ) { snappedDateTime ->

                        val year = snappedDateTime.year
                        val month = snappedDateTime.monthNumber
                        val day = snappedDateTime.dayOfMonth


                        val dateFormat = LocalDate.Format {
                            dayOfMonth()
                            char('/')
                            monthNumber()
                            char('/')
                            year()
                        }
                        birthDate = dateFormat.format(snappedDateTime)
                        birthDateAsSnappedDateTime = snappedDateTime
//                        viewModel.onBirthDateChange(snappedDateTime.toString())
                    }
                }

            }

            Spacer(modifier = Modifier.weight(1f))

            RegularButton(
                text = "Continuar", onClick = {
                    /* Handle continue action *//*
                      val user = User(
                          id = Firebase.auth.currentUser?.uid ?: "",
                          nick = nickname ?: "",
                          firstName = firstName,
                          lastName = lastName,
                          email = Firebase.auth.currentUser?.email,
                          phoneNumber = Firebase.auth.currentUser?.phoneNumber,
                          isAnonymous = false
                      )


                      viewModel.updateProfile(
                          user
                      )
  */
                    showDatePicker = false

                    viewModel.saveChanges(
                        firstName = firstName.toString(),
                        lastName = lastName.toString(),
                        gender = gender!!,
                        birthDate = birthDateAsSnappedDateTime!!,

                        )
                    //      navController?.navigate("home")
                },
                   enabled = viewModel.uiState.value.loginButtonEnabled,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}


