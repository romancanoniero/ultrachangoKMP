package com.iyr.ultrachango.utils.auth_by_cursor.auth

import AppContext.context
import com.google.firebase.Firebase
import com.google.firebase.initialize

actual class FirebaseInit {
    actual fun initialize() {
        Firebase.initialize(context)
    }
}