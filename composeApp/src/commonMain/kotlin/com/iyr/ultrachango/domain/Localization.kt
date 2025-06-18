package com.iyr.ultrachango.domain

expect class Localization {
    fun applyLanguage(iso: String) : Unit
}