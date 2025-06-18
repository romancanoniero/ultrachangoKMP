package com.iyr.ultrachango.domain

import platform.Foundation.NSUserDefaults
actual class Localization {
    actual fun applyLanguage(iso: String) : Unit
    {
        NSUserDefaults.standardUserDefaults.setObject(
            arrayListOf(iso), "AppleLanguages"
        )

    }
}