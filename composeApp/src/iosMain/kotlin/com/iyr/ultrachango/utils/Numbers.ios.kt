package com.iyr.ultrachango.utils

import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.currentLocale

actual fun formatCurrency(amount: Double, currencySymbol: String): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterCurrencyStyle
        locale = NSLocale.currentLocale
    }
    return "$currencySymbol${formatter.stringFromNumber(NSNumber(amount))?.toString()}"
}
