package com.cso.coffeexp.domain.model

import kotlinx.datetime.LocalDate

data class Coffee(
    val id: Long? = null, // Unique ID, assigned on persistence
    val imageUrl: String? = null, // Optional photo of the beans/brew
    val name: String, // Required
    val roaster: String? = null, // Optional — roaster/brand, may be unknown
    val series: String? = null, // Optional collection/series name, e.g. "Ethical Harvest Series"
    val origin: String? = null, // Region/country, e.g. "Gedeo Zone, Ethiopia"
    val process: String? = null, // e.g. "Washed"
    val elevation: String? = null, // e.g. "1,900-2,200 MASL"
    val roastDate: LocalDate? = null,
    val roastLevel: String = "Medium", // Light | Medium | Medium-Dark | Dark
    val brewingMethod: String, // Required, e.g. "V60 Pour-over"
    val grindSize: Int? = null, // Grinder setting, in clicks; null = not specified
    val temperature: Int? = null, // Brew water temperature, °C; null = not specified
    val ratio: String? = null, // Coffee:water ratio, e.g. "1:16"
    val brewTime: String? = null, // e.g. "3:15 min"
    val rating: Double = 0.0, // Canonical 0–10 score; UI derives a /5 star display as rating / 2
    val notes: String? = null, // Optional free-text tasting notes / journal entry
)
