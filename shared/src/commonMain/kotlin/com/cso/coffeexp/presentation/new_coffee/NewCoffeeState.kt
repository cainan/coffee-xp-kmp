package com.cso.coffeexp.presentation.new_coffee

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.cso.coffeexp.core.utils.LocalDate
import kotlinx.datetime.LocalDate

@Stable
data class NewCoffeeState(
    val coffeeId: Long? = null,
    val photoUri: String? = null,
    val coffeeNameState: TextFieldState = TextFieldState(),
    val roasterState: TextFieldState = TextFieldState(),
    val seriesCollectionState: TextFieldState = TextFieldState(),
    val originState: TextFieldState = TextFieldState(),
    val processState: TextFieldState = TextFieldState(),
    val elevationState: TextFieldState = TextFieldState(),
    val roastDate: LocalDate = LocalDate(),
    val roastLevelState: TextFieldState = TextFieldState(),
    val brewingMethod: TextFieldState = TextFieldState(),
    val grindSizeState: TextFieldState = TextFieldState(),
    val temperatureState: TextFieldState = TextFieldState(),
    val ratioState: TextFieldState = TextFieldState(),
    val brewDuration: TextFieldState = TextFieldState(),
    val overallRating: Double = 0.0,
    val tastingNotesState: TextFieldState = TextFieldState(),

    val createdAt: LocalDate? = null,
    val lastModifiedAt: LocalDate? = null,
    val isSaveEnabled: Boolean = false,
    val isSaving: Boolean = false
)
