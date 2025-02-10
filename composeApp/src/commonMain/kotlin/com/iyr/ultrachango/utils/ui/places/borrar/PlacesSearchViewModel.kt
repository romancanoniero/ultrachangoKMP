package com.iyr.ultrachango.utils.ui.places.borrar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.utils.ui.places.google.model.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class PlacesSearchViewModel(
    private val placesSearchService: PlacesSearchService
) : ViewModel(), KoinComponent {

       var state by mutableStateOf(UiState())
        private set

    var searchJob : Job? = null

    fun searchPlaces(query: String) {

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            placesSearchService.searchPlaces(query).collect { result ->
                state = state.copy(
                    loading = false,
                    searchResults = result.results
                )
            }
        }
    }


    data class UiState(
        val loading: Boolean = false,
        val showKeyboard: Boolean = false,
        val searchResults: List<Result> = emptyList(),
        val refresh : Boolean = true
        )
}

