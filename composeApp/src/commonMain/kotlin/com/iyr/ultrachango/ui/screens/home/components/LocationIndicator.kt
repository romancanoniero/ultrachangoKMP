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
import com.iyr.ultrachango.data.models.Location
import com.iyr.ultrachango.data.models.Locations
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
    locations: List<Location> = emptyList(),
    navigateToAddLocation: () -> Unit,
    focusRequester: FocusRequester
) {

    println("LocationIndicator - ubicaciones = " + Json.encodeToString(locations))


    // val waitForLocationState by mutableStateOf(vm.state.waitForLocation)
    //   val locations  by mutableStateOf(vm.knownLocations.value)  //
    var expanded by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<Location?>(null) }


    println("culo : " + locations.size)

    // Filtrar ubicaciones por tipo


    // Actualizar selección de ubicación
    LaunchedEffect(locations) {
        selectedLocation = when {
            locations.isNotEmpty() -> locations.first()

            else -> null
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }) {
        Row {
            if (fetchingLocations) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.primary
                )
            } else {

                if (selectedLocation?.locationType == Locations.CUSTOM)
                    Icon(imageVector = Icons.Filled.Home, contentDescription = null)
                else
                    Icon(imageVector = Icons.Filled.GpsFixed, contentDescription = null)

/*
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                */
            }
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .clickable {
                        expanded = true

                    },
                style = MaterialTheme.typography.titleMedium,
                text = selectedLocation?.title ?: "Select Location"
            )
        }

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            if (locations.isNotEmpty()) {
                locations.forEach { location ->
                    DropdownMenuItem(onClick = {
                        triggerHapticFeedback()
                        selectedLocation = location
                        vm.onLocationSelected(location)
                        expanded = false
                    },
                        text = {
                            LocationItem(location)
                        }
                    )
                }
            }

            if (locations.filter{ it.locationType == Locations.CURRENT_LOCATION }.isEmpty()) {
                DropdownMenuItem(onClick = {
                    triggerHapticFeedback()
                    //      selectedLocation = currentLocation
                    vm.requestCurrentLocation() // Gestionar solicitud de ubicación actual
                    expanded = false
                },
                    text = {
                        Text(
                            modifier = Modifier.menuAnchor(),
                            text = "Obtener ubicación actual"
                        )
                    })
            }

            if (locations.isEmpty()  && locations.filter{ it.locationType == Locations.CUSTOM }.isEmpty()) {
                DropdownMenuItem(onClick = {
                    triggerHapticFeedback()
                    navigateToAddLocation()
                    expanded = false
                },
                    text = {
                        Text(
                            modifier = Modifier.menuAnchor(),
                            text = "Agregar Ubicación"
                        )
                    })
            }

            if (locations.isEmpty()) {
                Text(
                    text = "No hay ubicaciones disponibles. Habilite la ubicación o cree una nueva.",
                    modifier = Modifier.padding(16.dp).menuAnchor(),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


@Composable
private fun LocationItem(location: Location) {
    Row(modifier = Modifier.fillMaxWidth())
    {
        if (location.locationType == Locations.CUSTOM)
            Icon(imageVector = Icons.Filled.Home, contentDescription = null)
        else
            Icon(imageVector = Icons.Filled.GpsFixed, contentDescription = null)

        Spacer(modifier = Modifier.width(4.dp))

        Text(
          //  modifier = Modifier.Companion.menuAnchor(),
            text = location.title
        )
    }

}
