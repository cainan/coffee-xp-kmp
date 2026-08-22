package com.cso.coffeexp.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coffeexp.shared.generated.resources.Res
import coffeexp.shared.generated.resources.message_load_empty
import coffeexp.shared.generated.resources.message_load_error
import com.cso.coffeexp.core.design_system.utils.UiText
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

    private var selectedCoffeeId: Long? = null

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

            is DetailsAction.OnRetryClick -> {
                retryLoad()
            }

            DetailsAction.OnBackClick -> Unit // handled by View
            is DetailsAction.OnEditClick -> Unit // Handled by View
        }
    }

    private fun retryLoad() {
        selectedCoffeeId?.let { loadCoffeeDetails(it) }
    }

    private fun loadCoffeeDetails(coffeeId: Long) {

        selectedCoffeeId = coffeeId

        _state.update {
            it.copy(
                errorMessage = null,
                isLoading = true,
            )
        }

        viewModelScope.launch {

            coffeeRepository.getCoffeeById(coffeeId)
                .onSuccess { details ->

                    if (details == null) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = UiText.Resource(Res.string.message_load_empty)
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                coffee = details
                            )
                        }
                    }
                }
                .onFailure {

                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = UiText.Resource(Res.string.message_load_error)
                        )
                    }
                }

        }
    }
}
