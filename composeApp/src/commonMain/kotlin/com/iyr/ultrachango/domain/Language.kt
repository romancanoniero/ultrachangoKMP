package com.iyr.ultrachango.domain

enum class Language (val iso: String) {
    ENGLISH("en"),
    SPANISH("es"),
    FRENCH("fr"),
    GERMAN("de"),
    ITALIAN("it"),
    PORTUGUESE("pt"),
    RUSSIAN("ru"),
    CHINESE("zh"),
    JAPANESE("ja"),
    KOREAN("ko");

    companion object {
        fun fromIso(iso: String): Language? {
            return values().find { it.iso == iso }
        }
    }
}