package com.cso.coffeexp.presentation.new_coffee

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cso.coffeexp.core.error_handling.onFailure
import com.cso.coffeexp.core.error_handling.onSuccess
import com.cso.coffeexp.core.utils.LocalDate
import com.cso.coffeexp.domain.logger.CoffeeXpLogger
import com.cso.coffeexp.domain.model.Coffee
import com.cso.coffeexp.domain.repository.CoffeeRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewCoffeeViewModel(
    private val logger: CoffeeXpLogger,
    private val coffeeRepository: CoffeeRepository,
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<NewCoffeeEvent>()
    val events = eventChannel.receiveAsFlow()

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

            is NewCoffeeAction.OnRoastDateSelected -> _state.update { it.copy(roastDate = action.date) }

            NewCoffeeAction.OnPhotoClick -> {
                // TODO: wire to platform image picker once the data layer exists.
            }

            is NewCoffeeAction.OnPhotoSelected -> _state.update {
                it.copy(photoUri = action.uri)
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
                        roastDate = coffeeToEdit.roastDate,
                        roastLevelState = TextFieldState(coffeeToEdit.roastLevel),
                        brewingMethod = coffeeToEdit.brewingMethod,
                        grindSizeState = TextFieldState(coffeeToEdit.grindSize ?: ""),
                        temperatureState = TextFieldState(coffeeToEdit.temperature ?: ""),
                        ratioState = TextFieldState(coffeeToEdit.ratio ?: ""),
                        brewDuration = TextFieldState(coffeeToEdit.brewTime ?: ""),
                        overallRating = coffeeToEdit.rating,
                        tastingNotesState = TextFieldState(coffeeToEdit.notes ?: ""),
                        createdAt = coffeeToEdit.createdAt,
                        lastModifiedAt = coffeeToEdit.lastModifiedAt
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
                    roastDate = _state.value.roastDate,
                    roastLevel = _state.value.roastLevelState.text.toString(),
                    brewingMethod = _state.value.brewingMethod,
                    grindSize = _state.value.grindSizeState.text.toString(),
                    temperature = _state.value.temperatureState.text.toString(),
                    ratio = _state.value.ratioState.text.toString(),
                    brewTime = _state.value.brewDuration.text.toString(),
                    rating = _state.value.overallRating,
                    notes = _state.value.tastingNotesState.text.toString(),
                    createdAt = _state.value.createdAt ?: LocalDate(),
                    lastModifiedAt = LocalDate()
                )
            ).onSuccess {
                logger.debug("Successfully upserted a coffee")
                eventChannel.send(NewCoffeeEvent.AddedSuccessfully)
            }.onFailure {
                logger.debug("Fail to upsert a coffee")
            }

            // TODO check db transaction result
            // TODO implement back action when success
        }
    }
}
