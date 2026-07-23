package com.cso.coffeexp.presentation.new_coffee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class NewCoffeeViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(NewCoffeeState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = NewCoffeeState()
        )

    fun onAction(action: NewCoffeeAction) {
        when (action) {
            is NewCoffeeAction.OnBrewingMethodExpandedChange -> _state.update { it.copy(isBrewingMethodExpanded = action.expanded) }
            is NewCoffeeAction.OnBrewingMethodSelected -> _state.update {
                it.copy(brewingMethod = action.value, isBrewingMethodExpanded = false)
            }
            is NewCoffeeAction.OnRatingChange -> _state.update { it.copy(overallRating = action.rating) }
            NewCoffeeAction.OnPhotoClick -> {
                // TODO: wire to platform image picker once the data layer exists.
            }
            NewCoffeeAction.OnSaveClick -> {
                // TODO: wire to CoffeeRepository once the data layer exists, e.g.:
                //  val coffee = Coffee(name = state.value.coffeeNameState.text.toString(), ...)
            }
            NewCoffeeAction.OnBackClick -> Unit // handled by Root
        }
    }
}
