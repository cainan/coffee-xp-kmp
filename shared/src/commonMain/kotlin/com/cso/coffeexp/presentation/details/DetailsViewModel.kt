package com.cso.coffeexp.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class DetailsViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(DetailsState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                // TODO: load the coffee by id (from SavedStateHandle) via CoffeeRepository
                //  once the data layer and navigation argument passing exist.
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = DetailsState()
        )

    fun onAction(action: DetailsAction) {
        when (action) {
            DetailsAction.OnBackClick -> Unit // handled by Root
            DetailsAction.OnEditClick -> Unit // TODO: wire to navigation once it exists
        }
    }
}
