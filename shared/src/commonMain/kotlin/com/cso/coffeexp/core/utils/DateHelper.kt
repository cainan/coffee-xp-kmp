package com.cso.coffeexp.core.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
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