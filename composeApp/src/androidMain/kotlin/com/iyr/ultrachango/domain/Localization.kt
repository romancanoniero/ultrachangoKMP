package com.iyr.ultrachango.domain

import android.content.Context
import java.util.Locale

actual class Localization(
    private val context: Context
) {
    actual fun applyLanguage(iso: String): Unit {
        val locale = Locale(iso)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        // Optionally, you can also save the selected language in SharedPreferences
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("selected_language", iso).apply()
    }
}