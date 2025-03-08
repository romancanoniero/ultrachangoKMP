package com.iyr.ultrachango.utils.datetime

import kotlinx.datetime.LocalDate

expect class DateTimeProvider {


    companion object {
        fun getCurrentTimeMillis(): Long
        fun getCurrentDate(): LocalDate
    }
}
