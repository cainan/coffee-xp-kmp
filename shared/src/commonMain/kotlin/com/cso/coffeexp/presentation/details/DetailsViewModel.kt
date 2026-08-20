package com.cso.coffeexp.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cso.coffeexp.core.error_handling.onFailure
import com.cso.coffeexp.core.error_handling.onSuccess
import com.cso.coffeexp.domain.logger.CoffeeXpLogger
import com.cso.coffeexp.domain.repository.CoffeeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val coffeeRepository: CoffeeRepository,
    private val logger: CoffeeXpLogger
) : ViewModel() {

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
            is DetailsAction.OnCoffeeIdSelected -> {
                loadCoffeeDetails(action.coffeeId)
            }

            DetailsAction.OnBackClick -> Unit // handled by View
            is DetailsAction.OnEditClick -> Unit // Handled by View
        }
    }

    private fun loadCoffeeDetails(selectedCoffeeId: Long) {

        _state.update {
            it.copy(
                isLoading = true
            )
        }

        viewModelScope.launch {

            coffeeRepository.getCoffeeById(selectedCoffeeId)
                .onSuccess { details ->

                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    details?.let { coffee ->
                        _state.update {
                            it.copy(
                                coffee = coffee
                            )
                        }
                    }
                }
                .onFailure {

                    // TODO must show an error

                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }


        }
    }
}
