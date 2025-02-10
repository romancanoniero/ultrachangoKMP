package com.iyr.ultrachango.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

class ScaffoldViewModel : ViewModel(), KoinComponent {


    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    fun updateState(newState: UiState) {
        _state.value = newState
    }

    fun setTitle(title: String) {
        _state.value = _state.value.copy(title = title)
    }

    fun showLoader(show: Boolean) {
        _state.value = _state.value.copy(showLoader = show)
    }

    fun setButtons(content: @Composable () -> Unit) {
        _state.value = _state.value.copy(barButtons =  content)
    }

    data class UiState(
        var showLoader : Boolean = false,
        val title: String = "Titulo",
        val barButtons : @Composable () -> Unit = {}
    )
}