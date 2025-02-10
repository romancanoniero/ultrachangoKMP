package com.iyr.ultrachango.utils.ui.places

import androidx.compose.ui.text.intl.Locale
import dev.jordond.compass.Place

class PlaceExtensions

fun Place.formatAddress(locale: Locale): String {
    return when (locale.language) {
        "es" -> "${this.street}  , ${this.locality}, ${this.country}"
        "en" -> "${this.street}, ${this.locality}, ${this.country}"
        else -> "${this.street}, ${this.locality}, ${this.country}"
    }
}