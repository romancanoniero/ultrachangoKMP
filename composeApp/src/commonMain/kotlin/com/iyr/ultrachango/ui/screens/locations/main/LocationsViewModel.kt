package com.iyr.ultrachango.ui.screens.locations.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyr.ultrachango.auth.AuthRepositoryImpl
import com.iyr.ultrachango.data.database.repositories.UserLocationsRepository
import com.iyr.ultrachango.data.models.Location
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.utils.ui.places.borrar.PlacesSearchService
import com.iyr.ultrachango.utils.ui.places.google.model.Result
import com.iyr.ultrachango.utils.ui.places.models.CustomPlace
import com.iyr.ultrachango.utils.ui.places.models.toLocation
import com.iyr.ultrachango.viewmodels.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class LocationsViewModel(
    private val scaffoldVM: ScaffoldViewModel,
    private val userViewModel: UserViewModel,
    private val placesSearchService: PlacesSearchService,
    private val locationRepository: UserLocationsRepository,
    private val   authRepository: AuthRepositoryImpl
) : ViewModel(), KoinComponent {



    var state by mutableStateOf(UiState())
        private set

    var currentUserKey : String by mutableStateOf("")

    var searchJob : Job? = null


    init {
        scaffoldVM.setTitle("Ubicaciones")
        scaffoldVM.showLoader(true)
        viewModelScope.launch(Dispatchers.IO) {
            val userId = authRepository.getUserKey().toString()
            fetchLists(userId)
        }

    }


     suspend fun fetchLists() {
       val userKey = authRepository.getUserKey()!!
        fetchLists(userKey)

    }

    private suspend fun fetchLists(userId : String) {
        currentUserKey = userId
        scaffoldVM.showLoader(true)
        try {
            locationRepository.fetchLists(userId).collect { result ->
                state = state.copy(list = result)
                scaffoldVM.showLoader(false)
            }
        } catch (e: Exception) {
            scaffoldVM.showLoader(false)

            state = state.copy(
                loading = false,
                errorMessage = e.message,
                showErrorMessage = true
            )
        }
    }



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

    fun closeErrorDialogRequest() {
        state = state.copy(
            showErrorMessage = false
        )
    }

    fun onAddLocationRequested() {
        state = state.copy(
            showLocationSearchDialog = true
        )
    }

    fun closeLocationSearchDialogRequest() {
        state = state.copy(
            showLocationSearchDialog = false
        )
    }

    fun onLocationUpdateRequested(title: String, customPlace: CustomPlace) {
        val userKey = userViewModel.getUserKey()
        val location = customPlace.toLocation(userKey, title)
        try {
            viewModelScope.launch(Dispatchers.IO) {
                locationRepository.save(location)
                closeDialogs()
                fetchLists()
            }
        }
        catch (exception: Exception) {
            state = state.copy(
                showErrorMessage = true,
                errorMessage = exception.message
            )
        }

    }

    fun onDeleteRequested(locationID: Int) {

        val paramMap = HashMap<String,Any>()
        paramMap.put("ID",locationID)
        state = state.copy(
            showDeletionDialog = paramMap
        )
    }

    fun deleteLocation(locationID: Int)
    {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val userKey = userViewModel.getUserKey()
                locationRepository.delete(userKey, locationID.toInt())
                fetchLists()
            }
        }
        catch (exception: Exception) {
            state = state.copy(
                showErrorMessage = true,
                errorMessage = exception.message
            )
        }

    }

    fun onRefreshRequested() {

        try {
            viewModelScope.launch(Dispatchers.IO) {

                fetchLists()
            }
        }
        catch (exception: Exception) {
            state = state.copy(
                showErrorMessage = true,
                errorMessage = exception.message
            )
        }
    }

    fun closeDialogs() {
        state = state.copy(
            showDeletionDialog = null,
            showErrorMessage = false,
            showLocationSearchDialog = false
        )
    }

    data class UiState(
        val loading: Boolean = false,
        val showKeyboard: Boolean = false,
        val searchResults: List<Result> = emptyList(),
        val list: List< Location> = emptyList(),
        val refresh: Boolean = true,
        val showErrorMessage: Boolean = false,
        val errorMessage: String? = null,
        val showLocationSearchDialog: Boolean = false,
        val showDeletionDialog: Any? = null
    )
}

