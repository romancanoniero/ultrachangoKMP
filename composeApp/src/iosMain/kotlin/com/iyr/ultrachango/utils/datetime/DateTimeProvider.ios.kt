package com.iyr.ultrachango.utils.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

actual class DateTimeProvider {

    actual companion object {
        actual fun getCurrentTimeMillis(): Long =
            Clock.System.now().toEpochMilliseconds()

        actual fun getCurrentDate(): LocalDate {
            return Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
        }
    }
}