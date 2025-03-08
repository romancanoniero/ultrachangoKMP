package com.iyr.ultrachango.utils.datetime

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

actual class DateTimeProvider {

    actual companion object {
        actual fun getCurrentTimeMillis(): Long = System.currentTimeMillis()

        actual fun getCurrentDate(): LocalDate {
            return Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
        }
    }
}