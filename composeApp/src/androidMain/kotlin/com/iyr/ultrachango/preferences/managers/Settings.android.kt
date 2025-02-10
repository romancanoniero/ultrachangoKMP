package com.iyr.ultrachango.preferences.managers

import android.content.Context
import android.content.SharedPreferences
import com.iyr.ultrachango.UltraChangoApp
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings



actual val settings: Settings
    get()  {
        val delegate: SharedPreferences = UltraChangoApp.contextProvider.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return SharedPreferencesSettings(delegate)
    }