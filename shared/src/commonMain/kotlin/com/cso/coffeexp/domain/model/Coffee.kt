package com.cso.coffeexp.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

data class Coffee(
    val id: Long? = null, // Unique ID, assigned on persistence
    val imageUrl: String? = null, // Photo of the beans/brew
    val name: String = "", // Coffee name
    val roaster: String = "", // Roaster/brand, may be unknown
    val series: String? = null, // Optional collection/series name, e.g. "Ethical Harvest Series"
    val origin: String = "", // Region/country, e.g. "Gedeo Zone, Ethiopia"
    val process: String? = null, // e.g. "Washed"
    val elevation: String? = null, // e.g. "1,900-2,200 MASL"
    val roastDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val roastLevel: String = "", // Light | Medium | Medium-Dark | Dark
    val brewingMethod: String = "", // e.g. "V60 Pour-over"
    val grindSize: String? = null, // Grinder setting, in clicks; null = not specified
    val temperature: Int? = null, // Brew water temperature, °C; null = not specified
    val ratio: String? = null, // Coffee:water ratio, e.g. "1:16"
    val brewTime: String? = null, // e.g. "3:15 min"
    val rating: Double = 0.0, // Canonical 0–10 score; UI derives a /5 star display as rating / 2
    val notes: String? = null, // Optional free-text tasting notes / journal entry
    val createdAt: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val lastModifiedAt: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
)
