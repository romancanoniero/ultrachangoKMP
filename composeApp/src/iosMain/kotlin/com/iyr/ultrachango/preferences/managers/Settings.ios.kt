package com.iyr.ultrachango.preferences.managers

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import platform.Foundation.NSUserDefaults

actual val settings: Settings
    get() {
        val delegate: NSUserDefaults = NSUserDefaults.standardUserDefaults
        val settings: Settings = NSUserDefaultsSettings(delegate)
        return settings
    }