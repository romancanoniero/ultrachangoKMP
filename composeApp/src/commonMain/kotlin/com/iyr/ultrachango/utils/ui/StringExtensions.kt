package com.iyr.ultrachango.utils.ui



fun String.capitalizeFirstLetter(): String {
    return replaceFirstChar { it.uppercase() }
}