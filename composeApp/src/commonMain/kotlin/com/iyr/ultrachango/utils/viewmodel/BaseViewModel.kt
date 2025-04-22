package com.iyr.ultrachango.utils.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Clase base para todos los ViewModels de la aplicación.
 * Proporciona funcionalidad común para el manejo de coroutines y errores.
 *
 * Esta clase implementa:
 * - Manejo de coroutines con SupervisorJob para evitar la cancelación en cascada
 * - Manejo de excepciones global
 * - Cancelación automática de coroutines cuando el ViewModel es limpiado
 *
 * @property coroutineContext Contexto de coroutine configurado con SupervisorJob y manejador de excepciones
 */
open class BaseViewModel : ViewModel() {

    /**
     * Contexto de coroutine configurado con SupervisorJob y manejador de excepciones.
     * El SupervisorJob permite que las coroutines fallen de forma independiente.
     */
    val coroutineContext = SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
        println("BaseViewModel: Error: ${throwable.message}")
        //show error message using snackbar for all errors
    }

    private var job: Job? =  null

    /**
     * Lanza una coroutine con manejo de excepciones.
     * 
     * @param block Bloque de código a ejecutar en la coroutine
     */
    fun launchWithCatchingException(block: suspend CoroutineScope.() -> Unit) {
        job = viewModelScope.launch(
            context = coroutineContext,
            block = block
        )
    }

    /**
     * Se llama cuando el ViewModel ya no se usa y será destruido.
     * Cancela todas las coroutines activas.
     */
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}