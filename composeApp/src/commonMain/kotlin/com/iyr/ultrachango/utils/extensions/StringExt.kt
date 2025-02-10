package com.iyr.ultrachango.utils.extensions

fun String.isDigitsOnly(): Boolean {
    return this.all { it.isDigit() }
}


fun String.isEmail(): Boolean {
    return Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(this)
}


fun String.isValidMobileNumber(): Boolean {
    return Regex("^\\+?[1-9]\\d{1,14}\$").matches(this)
}