package com.iyr.ultrachango.utils.shared.firebase

import AppContext.context
import com.google.firebase.FirebaseApp

actual class FirebaseConfig {
    actual fun initialize() {
        // Código de inicialización de Firebase para Android
        FirebaseApp.initializeApp(context)
    }
}