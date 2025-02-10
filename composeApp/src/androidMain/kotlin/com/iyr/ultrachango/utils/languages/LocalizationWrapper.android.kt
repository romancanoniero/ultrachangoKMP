package com.iyr.ultrachango.utils.languages

import java.util.Locale

actual fun changeLang(lang: String) {
    val locale = Locale(lang)
    Locale.setDefault(locale)
}