package com.iyr.ultrachango.utils.ui.places.borrar


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue
import com.iyr.ultrachango.utils.ui.places.google.model.Result
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchableDropdown(
    vm: PlacesSearchViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    //state: PlacesSearchViewModel.UiState? = null,
) {
    var forceClose  by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var suggestions by remember { mutableStateOf<List<Result>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val focusRequester = remember { FocusRequester() }

    val state = vm.state
//PlaceAutoCompleteTextField()
    forceClose = false
    Column(modifier = modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                if (it.text.isEmpty()) {
                    expanded = false
                    suggestions = emptyList()
                } else
                    if (it.text.length > 4) {
                        //   onSearch(it.text)
                        forceClose = true
                        vm.searchPlaces(it.text)
               //
                    }
            },

            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    triggerHapticFeedback()
                   forceClose = true
                })
                .focusRequester(focusRequester)

        )

        expanded = (state.searchResults?.isNotEmpty() ?: false) && forceClose == false

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            state.searchResults?.forEach { suggestion ->
                forceClose = false
                DropdownMenuItem(onClick = {
                    triggerHapticFeedback()
                    text = TextFieldValue(suggestion.formatted_address.toString())
                    expanded = false
                }) {
                    Text(text = suggestion.formatted_address.toString())
                }
            }
        }
    }
}


@Composable
fun DropdownMenuItem(onClick: () -> Unit, content: @Composable () -> Unit) {
    DropdownMenuItem(
        onClick = onClick,
        text = content
    )
}
