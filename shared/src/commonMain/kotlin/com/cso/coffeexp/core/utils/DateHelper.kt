package com.cso.coffeexp.core.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

fun LocalDate(): LocalDate {
    return Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
}

fun LocalDate.toEpochMillisString(): String {
    return atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds().toString()
}

fun LocalDate(epochMillis: Long): LocalDate {
    return Instant
        .fromEpochMilliseconds(epochMillis)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
}

fun LocalDate.toFormattedDateString(): String {
    val dayStr = day.toString().padStart(2, '0')
    val monthStr = month.number.toString().padStart(2, '0')
    return "$dayStr/$monthStr/$year" // Formato "15/08/2026"
}