@file:OptIn(ExperimentalForeignApi::class)

package com.iyr.ultrachango.voice


import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSLog
import platform.UIKit.UIApplication
import platform.UIKit.UILabel
import platform.UIKit.UIViewController

actual fun handleVoiceCommand(command: String) {
    NSLog("Procesando comando: $command")

    // Simulación de respuesta visual en un UILabel (se podría integrar con SwiftUI)
    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    var label = UILabel()
    label.text = "Comando recibido: $command"

  //  label.frame = platform.CoreGraphics.CGRectMake(50.0, 100.0, 300.0, 50.0)
    rootViewController?.view?.addSubview(label)

    // Lógica para manejar comandos específicos
    if (command.startsWith("Agrega")) {
        val partes = command.split(" a la lista ")
        if (partes.size == 2) {
            val producto = partes[0].replace("Agrega ", "")
            val lista = partes[1]
            NSLog("Agregar $producto a la lista $lista")
            // Llamar a la API o actualizar la UI
        }
    }
}