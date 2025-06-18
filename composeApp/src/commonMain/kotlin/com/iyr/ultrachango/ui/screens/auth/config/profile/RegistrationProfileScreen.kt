package com.iyr.ultrachango.ui.screens.auth.config.profile

//import com.iyr.ultrachango.data.models.AppUser
//import com.iyr.ultrachango.domain.models.AppUser
//import com.iyr.ultrachango.ui.dialogs.ImageOptionDialog
//import com.iyr.ultrachango.ui.dialogs.LoadingDialog
//import com.iyr.ultrachango.ui.screens.auth.config.profile.components.GenderSelector
//import com.iyr.ultrachango.utils.ui.elements.imageselector.ImageSelector
//import com.iyr.ultrachango.utils.ui.elements.imageselector.ImageSelectorConfig
//import com.iyr.ultrachango.utils.ui.elements.imageselector.ImageShapeConfig

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.iyr.ultrachango.data.models.enums.Genders
import com.iyr.ultrachango.utils.ui.camera_gallery.rememberCameraManager
import com.iyr.ultrachango.utils.ui.camera_gallery.rememberGalleryManager
import com.iyr.ultrachango.utils.ui.elements.RegularButton
import com.iyr.ultrachango.utils.ui.elements.imageselector.CircularImageSelector
import com.iyr.ultrachango.utils.ui.elements.imageselector.ImageSelector
import com.iyr.ultrachango.utils.ui.elements.imageselector.ImageSelectorConfig
import dev.darkokoa.datetimewheelpicker.WheelDatePicker
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
import dev.icerock.moko.permissions.PermissionsController
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
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.profile_pic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationProfileScreen(
    //currentUser: AppUser? = null,
    //viewModel: RegistrationProfileViewModel<AppUser?> = koinInject {
    //    parametersOf(currentUser)
    //},
    navController: NavController? = null,
    permissionsController: PermissionsController? = null,
    modifier: Modifier = Modifier.clickable {
        //viewModel?.resetFocus()
    }
) {
    val coroutineScope = rememberCoroutineScope()
    
    // Estados del formulario
    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(Genders.UNKNOWN) }
    var birthDate by remember { mutableStateOf("") }
    var birthDateAsSnappedDateTime by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    
    // Estados de UI
    var showImagePicker by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    
    // Managers de cámara y galería
    val cameraManager = rememberCameraManager {
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.IO) {
                it?.toImageBitmap()
            }
            selectedImage = bitmap
            showImagePicker = false
        }
    }
    
    val galleryManager = rememberGalleryManager {
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.IO) {
                it?.toImageBitmap()
            }
            selectedImage = bitmap
            showImagePicker = false
        }
    }


    // Fecha actual para el picker
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    
    // Validación del formulario
    val isFormValid = firstName.isNotBlank() && 
                     lastName.isNotBlank() && 
                     gender != Genders.UNKNOWN && 
                     birthDateAsSnappedDateTime != null

    var profileImage by remember { mutableStateOf<Any?>(null) }
    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    fun handleError(error: String) {
        hasError = true
        errorMessage = error
        // onValidationChange(false) // Notificar que el formulario no es válido
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Text(
            text = "Completa tu perfil",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )




// Uso avanzado - Completamente configurable
/*
        ImageSelector(
            selectedImage = selectedImage,
            onImageSelected = { selectedImage = it as ImageBitmap? },
            config = ImageSelectorConfig(
                shape = androidx.compose.foundation.shape.CircleShape,
                size = 120.dp,
                border = BorderStroke(4.dp, MaterialTheme.colorScheme.primary),
                enableShadow = true,
                title = "Foto de Perfil",
                editIcon = Icons.Default.Edit,
                onError = { error -> 
                    // Manejar error aquí
                    println("Error en ImageSelector: $error")
                }
            )
        )*/
        CircularImageSelector(
            selectedImage = selectedImage,
            onImageSelected = { selectedImage = it as ImageBitmap? },
            size = 120.dp,
            enableShadow = true,
            title = "Foto de Perfil",
            editIcon = Icons.Default.Edit,
            onError = { error -> handleError(error) },
        )

        // Imagen de perfil circular con ícono de lápiz

        /*
        Box(
            modifier = Modifier
                .size(140.dp)
                .clickable { showImagePicker = true },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImage != null) {
                Image(
                    bitmap = selectedImage!!,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                        .border(
                            BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                        .border(
                            BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.profile_pic),
                        contentDescription = "Foto de perfil predeterminada",
                        modifier = Modifier.size(60.dp),
                        alpha = 0.6f
                    )
                }
            }

            // Ícono de lápiz en la esquina
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(40.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
                    .border(
                        BorderStroke(2.dp, MaterialTheme.colorScheme.surface),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Edit,
                    contentDescription = "Editar foto",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        */
        Spacer(modifier = Modifier.height(32.dp))
        
        // Campo Nombre
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Campo Apellido
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Selector de Género
        var expandedGender by remember { mutableStateOf(false) }
        
        ExposedDropdownMenuBox(
            expanded = expandedGender,
            onExpandedChange = { expandedGender = !expandedGender }
        ) {
            OutlinedTextField(
                value = when(gender) {
                    Genders.MALE -> "Masculino"
                    Genders.FEMALE -> "Femenino"
                    Genders.OTHER -> "Otro"
                    else -> "Seleccionar género"
                },
                onValueChange = { },
                readOnly = true,
                label = { Text("Género") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            
            ExposedDropdownMenu(
                expanded = expandedGender,
                onDismissRequest = { expandedGender = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Masculino") },
                    onClick = {
                        gender = Genders.MALE
                        expandedGender = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Femenino") },
                    onClick = {
                        gender = Genders.FEMALE
                        expandedGender = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Otro") },
                    onClick = {
                        gender = Genders.OTHER
                        expandedGender = false
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Campo Fecha de Nacimiento
        OutlinedTextField(
            value = birthDate,
            onValueChange = { },
            readOnly = true,
            label = { Text("Fecha de Nacimiento") },
            modifier = Modifier.fillMaxWidth(),
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                // Solo abrir el DatePicker, no hacer toggle
                                if (!showDatePicker) {
                                    showDatePicker = true
                                }
                            }
                        }
                    }
                },
            trailingIcon = {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha"
                )
            }
        )
        
        // Date Picker
        if (showDatePicker) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        // Cerrar el DatePicker al hacer clic en cualquier parte
                        showDatePicker = false
                    },
                contentAlignment = Alignment.Center
            ) {
                WheelDatePicker(
                    startDate = LocalDate(currentDate.year - 18, currentDate.monthNumber, currentDate.dayOfMonth),
                    yearsRange = IntRange(currentDate.year - 80, currentDate.year - 13),
                    size = DpSize(300.dp, 150.dp),
                    rowCount = 5,
                    textStyle = MaterialTheme.typography.titleSmall,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                        enabled = true,
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    )
                ) { snappedDateTime ->
                    // Actualizar la fecha seleccionada
                    val dateFormat = LocalDate.Format {
                        dayOfMonth()
                        char('/')
                        monthNumber()
                        char('/')
                        year()
                    }
                    birthDate = dateFormat.format(snappedDateTime)
                    birthDateAsSnappedDateTime = snappedDateTime
                    
                    // Cerrar el DatePicker después de seleccionar una fecha
           //         showDatePicker = false
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Botón Continuar
        RegularButton(
            text = "Continuar",
            onClick = {
                if (isFormValid) {
                    // Aquí procesarías el formulario
                    // viewModel.saveChanges(firstName, lastName, gender, birthDateAsSnappedDateTime!!)
                    navController?.navigate("home") // o la ruta que corresponda
                } else {
                    errorMessage = "Por favor completa todos los campos"
                    showErrorDialog = true
                }
            },
            enabled = isFormValid && !isLoading,
            modifier = Modifier.fillMaxWidth()
        )
    }
    
    // Diálogo de selección de imagen
    if (showImagePicker) {
        AlertDialog(
            onDismissRequest = { showImagePicker = false },
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            title = { Text("Seleccionar imagen") },
            text = {
                Column {
                    RegularButton(
                        text = "Cámara",
                        onClick = {
                            cameraManager.launch()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    RegularButton(
                        text = "Galería",
                        onClick = {
                            galleryManager.launch()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showImagePicker = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Diálogo de error
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { 
                showErrorDialog = false
                errorMessage = ""
            },
            title = { Text("Error") },
            text = { Text(errorMessage.toString()) },
            confirmButton = {
                TextButton(
                    onClick = { 
                        showErrorDialog = false
                        errorMessage = ""
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }


}
