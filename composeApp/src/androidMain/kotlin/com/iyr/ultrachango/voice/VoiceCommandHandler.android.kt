package com.iyr.ultrachango.voice


import android.content.Context
import android.util.Log
import android.widget.Toast

actual fun handleVoiceCommand(command: String) {
    Log.d("VoiceCommand", "Procesando comando: $command")

    // Simulación de respuesta visual para pruebas (se podría integrar con ViewModel)
    val context = getApplicationContext()
    Toast.makeText(context, "Comando recibido: $command", Toast.LENGTH_LONG).show()

    // Aquí puedes agregar la lógica para manejar comandos específicos
    if (command.startsWith("Agrega")) {
        val partes = command.split(" a la lista ")
        if (partes.size == 2) {
            val producto = partes[0].replace("Agrega ", "")
            val lista = partes[1]
            Log.d("VoiceCommand", "Agregar $producto a la lista $lista")
            // Llamar a la API o actualizar el estado de la UI
        }
    }
}

// Función para obtener el contexto global de la aplicación
fun getApplicationContext(): Context {
    return android.app.Application().applicationContext
}