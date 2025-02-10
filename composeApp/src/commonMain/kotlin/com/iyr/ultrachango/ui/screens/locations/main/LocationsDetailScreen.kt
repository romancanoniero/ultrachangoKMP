package com.iyr.ultrachango.ui.screens.locations.main


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.iyr.ultrachango.utils.ui.elements.CustomButton
import com.iyr.ultrachango.utils.ui.elements.screenOuterPadding
import com.iyr.ultrachango.utils.ui.places.borrar.DropdownMenuItem
import com.iyr.ultrachango.utils.ui.places.borrar.PlacesSearchService
import com.iyr.ultrachango.utils.ui.places.google.model.Result
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


@Composable
fun LocationsDetailsScreen(
    // vm: LocationsDetailsViewModel,
    onSaveClick2: () -> Unit,
    onSaveClick: () -> Unit
) {

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                explicitNulls = false

            })
        }
    }

    val service = PlacesSearchService(client)


    var vm = remember {
        LocationsDetailsViewModel(
            service
        )
    }

    var state = vm.state

    val scope = rememberCoroutineScope()

    val locationText = remember { mutableStateOf("Click to search location") }

    var text by remember { mutableStateOf(TextFieldValue("")) }
    var suggestions by remember { mutableStateOf<List<Result>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()




    Column(modifier = Modifier
        .padding(screenOuterPadding)) {

        if (state.refresh) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        if (it.text.isEmpty()) {
                            expanded = false
                            suggestions = emptyList()
                        } else
                            if (it.text.length >= 4) {
                                vm.searchPlaces(it.text)
                            }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                if (state.searchResults.isNotEmpty()) {
                    expanded = true
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        offset = DpOffset(x = 0.dp, y = 8.dp)
                    ) {
                        state.searchResults.forEach { suggestion ->
                            DropdownMenuItem(onClick = {
                                text = TextFieldValue(suggestion.formatted_address.toString())
                                expanded = false
                            }) {
                                Text(text = suggestion.formatted_address.toString())
                            }
                        }
                    }

                }
            }

        }


        //-----------


        CustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onClick = onSaveClick,
            content = {
                Text("Save")
            },

        )
    }
    return
}

