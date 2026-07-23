package com.cso.coffeexp.presentation.new_coffee

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable

@Stable
data class NewCoffeeState(
    val photoUri: String? = null,
    val coffeeNameState: TextFieldState = TextFieldState(),
    val roasterState: TextFieldState = TextFieldState(),
    val seriesCollectionState: TextFieldState = TextFieldState(),
    val originState: TextFieldState = TextFieldState(),
    val processState: TextFieldState = TextFieldState(),
    val elevationState: TextFieldState = TextFieldState(),
    val roastDateState: TextFieldState = TextFieldState(),
    val roastLevelState: TextFieldState = TextFieldState(),
    val isBrewingMethodExpanded: Boolean = false,
    val brewingMethod: String = "",
    val brewingMethodOptions: List<String> = listOf(
        "V60 Pour-over",
        "Aeropress",
        "French Press",
        "Chemex",
        "Espresso",
        "Moka Pot",
        "Cold Brew",
    ),
    val grindSizeState: TextFieldState = TextFieldState(),
    val temperatureState: TextFieldState = TextFieldState(),
    val ratioState: TextFieldState = TextFieldState(),
    val totalTimeState: TextFieldState = TextFieldState(),
    val overallRating: Int = 0,
    val tastingNotesState: TextFieldState = TextFieldState(),
)
