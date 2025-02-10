package com.iyr.ultrachango.utils.ui.places

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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iyr.ultrachango.utils.ui.elements.MyTexField
import com.iyr.ultrachango.utils.ui.places.borrar.PlacesSearchService
import com.iyr.ultrachango.utils.ui.places.models.CustomPlace
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import dev.jordond.compass.Place
import dev.jordond.compass.autocomplete.Autocomplete
import dev.jordond.compass.autocomplete.mobile
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlacesAutocomplete2(
    modifier: Modifier=Modifier,
    onPlaceSelected: (place: CustomPlace) -> Unit,
    placeholderText: String? = null
) {
    val client = remember { HttpClient() }
    val placesSearchService = remember { PlacesSearchService(client) }
    val scope = rememberCoroutineScope()
    val autocomplete = remember { Autocomplete.mobile() }
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val places = remember { mutableStateListOf<CustomPlace?>() }
    var selectedPlace: CustomPlace? by remember { mutableStateOf(null) }
    var searchJob: Job? by remember { mutableStateOf(null) }
    val focusRequester = remember { FocusRequester() }

/*
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = if (selectedPlace != null)
            selectedPlace.toString()
        else "No place selected."
    )*/
    Spacer(modifier = Modifier.height(20.dp))
    ExposedDropdownMenuBox(

        modifier = modifier,
        expanded = expanded, onExpandedChange = {}) {
        MyTexField(
            modifier = modifier
                .fillMaxWidth(),
            value = searchQuery,
            placeHolderText = placeholderText ?: "Search for a place",
            onValueChange = {
                searchQuery = it

            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                searchJob?.cancel()
                searchJob = scope.launch(Dispatchers.IO) {
                    placesSearchService.searchPlacesNominatim(searchQuery).collect { result ->
                        places.clear()
                        places.addAll(result.toList())
                        expanded = !expanded
                    }
                }
            })
        )
        ExposedDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }) {
            if (places.isNotEmpty()) {
                places.forEach { selectionOption ->
                    DropdownMenuItem(onClick = {
                        triggerHapticFeedback()
                        selectedPlace = selectionOption
                        searchQuery = selectedPlace.toString()
                        expanded = false
                        onPlaceSelected(selectionOption!!)
                    }) {
                        Text(text = selectionOption?.toString() ?: "Unknown place")
                    }
                }
            }
        }
    }
}