package com.iyr.ultrachango.utils

actual fun formatCurrency(amount: Double, currencySymbol: String): String {
    return java.text.NumberFormat.getCurrencyInstance().apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
        isGroupingUsed = true
    }.format(amount).replace("$", currencySymbol+" ")
}