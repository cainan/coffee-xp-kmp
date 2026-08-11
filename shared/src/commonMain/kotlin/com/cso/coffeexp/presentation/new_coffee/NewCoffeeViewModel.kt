package com.cso.coffeexp.presentation.new_coffee

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cso.coffeexp.core.utils.LocalDate
import com.cso.coffeexp.core.utils.toEpochMillisString
import com.cso.coffeexp.domain.logger.CoffeeXpLogger
import com.cso.coffeexp.domain.model.Coffee
import com.cso.coffeexp.domain.repository.CoffeeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewCoffeeViewModel(
    private val logger: CoffeeXpLogger,
    private val coffeeRepository: CoffeeRepository,
) : ViewModel() {

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
            is NewCoffeeAction.OnBrewingMethodExpandedChange -> _state.update {
                it.copy(
                    isBrewingMethodExpanded = action.expanded
                )
            }

            is NewCoffeeAction.OnBrewingMethodSelected -> _state.update {
                it.copy(brewingMethod = action.value, isBrewingMethodExpanded = false)
            }

            is NewCoffeeAction.OnRatingChange -> _state.update { it.copy(overallRating = action.rating) }
            NewCoffeeAction.OnPhotoClick -> {
                // TODO: wire to platform image picker once the data layer exists.
            }

            NewCoffeeAction.OnSaveClick -> {
                // TODO validate coffee fields before save
                saveCoffee()
            }

            is NewCoffeeAction.OnCoffeeToEditSelected -> {
                onCoffeeToEditSelected(action.coffeeId)
            }

            NewCoffeeAction.OnBackClick -> Unit // handled by Root

        }
    }

    private fun onCoffeeToEditSelected(coffeeId: Long) {
        viewModelScope.launch {
            coffeeRepository.getCoffeeById(coffeeId)?.let { coffeeToEdit ->
                _state.update {
                    it.copy(
                        coffeeId = coffeeToEdit.id,
                        photoUri = coffeeToEdit.imageUrl,
                        coffeeNameState = TextFieldState(coffeeToEdit.name),
                        roasterState = TextFieldState(coffeeToEdit.roaster),
                        seriesCollectionState = TextFieldState(coffeeToEdit.series ?: ""),
                        originState = TextFieldState(coffeeToEdit.origin),
                        processState = TextFieldState(coffeeToEdit.process ?: ""),
                        elevationState = TextFieldState(coffeeToEdit.elevation ?: ""),
                        roastDateState = TextFieldState(coffeeToEdit.roastDate.toEpochMillisString()),
                        roastLevelState = TextFieldState(coffeeToEdit.roastLevel),
                        brewingMethod = coffeeToEdit.brewingMethod,
                        grindSizeState = TextFieldState(coffeeToEdit.grindSize ?: ""),
                        temperatureState = TextFieldState(coffeeToEdit.temperature ?: ""),
                        ratioState = TextFieldState(coffeeToEdit.ratio ?: ""),
                        brewDuration = TextFieldState(coffeeToEdit.brewTime ?: ""),
                        overallRating = coffeeToEdit.rating,
                        tastingNotesState = TextFieldState(coffeeToEdit.notes ?: "")
                    )
                }

            }
        }
    }

    private fun saveCoffee() {
        viewModelScope.launch {

            // TODO validate fields

            coffeeRepository.upsertCoffee(
                coffee = Coffee(
                    id = _state.value.coffeeId,
                    imageUrl = _state.value.photoUri,
                    name = _state.value.coffeeNameState.text.toString(),
                    roaster = _state.value.roasterState.text.toString(),
                    series = _state.value.seriesCollectionState.text.toString(),
                    origin = _state.value.originState.text.toString(),
                    process = _state.value.processState.text.toString(),
                    elevation = _state.value.elevationState.text.toString(),
                    roastDate = LocalDate(), // TODO Fix when apply final date picker
                    roastLevel = _state.value.roastLevelState.text.toString(),
                    brewingMethod = _state.value.brewingMethod,
                    grindSize = _state.value.grindSizeState.text.toString(),
                    temperature = _state.value.temperatureState.text.toString(),
                    ratio = _state.value.ratioState.text.toString(),
                    brewTime = _state.value.brewDuration.text.toString(),
                    rating = _state.value.overallRating,
                    notes = _state.value.tastingNotesState.text.toString(),
                    createdAt = LocalDate(),
                    lastModifiedAt = LocalDate()
                )
            )

            // TODO check db transaction result
            // TODO implement back action when success
        }
    }
}
