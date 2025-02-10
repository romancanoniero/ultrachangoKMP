package com.iyr.ultrachango.utils.ui.places

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import dev.jordond.compass.Place
import dev.jordond.compass.autocomplete.Autocomplete
import dev.jordond.compass.autocomplete.mobile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlacesAutocomplete(

) {
    val scope = rememberCoroutineScope()
    val autocomplete = remember { Autocomplete.mobile() }
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val places = remember { mutableStateListOf<Place?>() }
    var selectedPlace: Place? by remember { mutableStateOf(null) }
    var searchJob: Job? by remember { mutableStateOf(null) }
    val focusRequester = remember { FocusRequester() }


    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = if (selectedPlace != null) "${selectedPlace?.locality} (${selectedPlace?.country})\n" + "LAT: ${selectedPlace?.coordinates?.latitude}\n" + "LNG: ${selectedPlace?.coordinates?.longitude}"
        else "No place selected."
    )
    Spacer(modifier = Modifier.height(20.dp))
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {}) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it

            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {


                searchJob?.cancel()
                searchJob = scope.launch(Dispatchers.IO) {
                    autocomplete.search(searchQuery).getOrNull().let {
                        places.clear()
                        places.addAll(it?.toList() ?: emptyList())

                    }
                }
                expanded = !expanded
            })
        )
        ExposedDropdownMenu(
            modifier = Modifier.fillMaxWidth()
            ,
            expanded = expanded,
            onDismissRequest = {
            expanded = false
        }) {
            if (places.isNotEmpty()) {
                places.forEach { selectionOption ->
                    DropdownMenuItem(onClick = {
                        triggerHapticFeedback()
                        selectedPlace = selectionOption
                        expanded = false
                    }) {
                        Text(text = selectionOption?.toString() ?: "Unknown place")
                    }

                }
            }
        }
    }
}