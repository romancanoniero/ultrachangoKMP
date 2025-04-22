package com.iyr.ultrachango.ui.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.iyr.ultrachango.data.models.Locations
import com.iyr.ultrachango.data.models.UserAddress
import com.iyr.ultrachango.ui.screens.home.HomeScreenViewModel
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LocationIndicator(
    vm: HomeScreenViewModel,
    uiState: HomeScreenViewModel.UiState,
    fetchingLocations: Boolean,
    UserAddresses: List<UserAddress> = emptyList(),
    navigateToAddLocation: () -> Unit,
    focusRequester: FocusRequester
) {
    println("LocationIndicator - ubicaciones = " + Json.encodeToString(UserAddresses))

    var expanded by remember { mutableStateOf(false) }
    var selectedUserAddress by remember { mutableStateOf<UserAddress?>(null) }

    // Actualizar selección de ubicación
    LaunchedEffect(UserAddresses) {
        selectedUserAddress = when {
            UserAddresses.isNotEmpty() -> UserAddresses.first()
            else -> null
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        Row {
            if (fetchingLocations) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                if (selectedUserAddress?.locationType == Locations.CUSTOM)
                    Icon(imageVector = Icons.Filled.Home, contentDescription = null)
                else
                    Icon(imageVector = Icons.Filled.GpsFixed, contentDescription = null)
            }
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .clickable { expanded = true },
                style = MaterialTheme.typography.titleMedium,
                text = selectedUserAddress?.title ?: "Seleccionar ubicación"
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Mostrar ubicaciones guardadas si existen
            if (UserAddresses.isNotEmpty()) {
                UserAddresses.forEach { location ->
                    if (location.locationType != Locations.ENABLE_LOCATION) {
                        DropdownMenuItem(
                            onClick = {
                                triggerHapticFeedback()
                                selectedUserAddress = location
                                vm.onLocationSelected(location)
                                expanded = false
                            },
                            text = { LocationItem(location) }
                        )
                    }
                }
            }

            // Si hay una opción de habilitar ubicación, mostrarla
            UserAddresses.find { it.locationType == Locations.ENABLE_LOCATION }?.let { enableLocation ->
                DropdownMenuItem(
                    onClick = {
                        triggerHapticFeedback()
                        vm.onLocationSelected(enableLocation)
                        expanded = false
                    },
                    text = {
                        Row {
                            Icon(
                                imageVector = Icons.Filled.GpsFixed,
                                contentDescription = "Habilitar ubicación"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(enableLocation.title)
                        }
                    }
                )
            }

            // Si no hay ubicaciones guardadas y no hay GPS, sugerir agregar ubicaciones manualmente
            if (UserAddresses.isEmpty() || (UserAddresses.size == 1 && UserAddresses[0].locationType == Locations.ENABLE_LOCATION)) {
                DropdownMenuItem(
                    onClick = {
                        triggerHapticFeedback()
                        navigateToAddLocation()
                        expanded = false
                    },
                    text = {
                        Row {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = "Agregar ubicación"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Agregar ubicación manualmente")
                        }
                    }
                )
                
                // Mostrar mensaje informativo
                Text(
                    text = "No hay ubicaciones disponibles. Agregue una ubicación manualmente.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun LocationItem(UserAddress: UserAddress) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Icon(
            imageVector = when (UserAddress.locationType) {
                Locations.CURRENT_LOCATION -> Icons.Filled.GpsFixed
                Locations.CUSTOM -> Icons.Filled.Home
                Locations.ENABLE_LOCATION -> Icons.Filled.GpsFixed
                else -> Icons.Filled.LocationOff
            },
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = UserAddress.title)
    }
}
