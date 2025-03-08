@file:OptIn(ExperimentalForeignApi::class)

package com.iyr.ultrachango.utils.auth_by_cursor.auth

import cocoapods.FirebaseCore.FIRApp
import kotlinx.cinterop.ExperimentalForeignApi

actual class FirebaseInit {
    actual fun initialize() {
        FIRApp.configure()
    }
}