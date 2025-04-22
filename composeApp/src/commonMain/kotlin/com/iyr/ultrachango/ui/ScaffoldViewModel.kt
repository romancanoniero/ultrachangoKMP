package com.iyr.ultrachango.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

/**
 * ViewModel que maneja el estado global del Scaffold de la aplicación.
 * 
 * Este ViewModel es responsable de:
 * - Mostrar/ocultar el loader global
 * - Actualizar el título de la pantalla
 * - Gestionar los botones de la barra de navegación
 * 
 * @property state Flujo de estado que contiene la configuración actual del Scaffold
 */
class ScaffoldViewModel : ViewModel(), KoinComponent {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    /**
     * Actualiza el estado completo del Scaffold.
     * 
     * @param newState Nuevo estado a aplicar
     */
    fun updateState(newState: UiState) {
        _state.value = newState
    }

    /**
     * Establece el título de la pantalla actual.
     * 
     * @param title Nuevo título a mostrar
     */
    fun setTitle(title: String) {
        _state.value = _state.value.copy(title = title)
    }

    /**
     * Muestra u oculta el loader global.
     * 
     * @param show true para mostrar el loader, false para ocultarlo
     */
    fun showLoader(show: Boolean) {
        _state.value = _state.value.copy(showLoader = show)
    }

    /**
     * Establece los botones de la barra de navegación.
     * 
     * @param content Composable que define los botones a mostrar
     */
    fun setButtons(content: @Composable () -> Unit) {
        _state.value = _state.value.copy(barButtons = content)
    }

    /**
     * Estado del Scaffold que contiene:
     * - showLoader: Indica si se debe mostrar el loader global
     * - title: Título actual de la pantalla
     * - barButtons: Composable que define los botones de la barra de navegación
     */
    data class UiState(
        var showLoader: Boolean = false,
        val title: String = "Titulo",
        val barButtons: @Composable () -> Unit = {}
    )
}