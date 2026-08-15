package com.cso.coffeexp.presentation.new_coffee

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
                observeIfCanSave()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = NewCoffeeState()
        )

    private fun observeIfCanSave() {
        snapshotFlow {
            val current = _state.value
            current.coffeeNameState.text.isNotBlank() &&
                    current.roasterState.text.isNotBlank() &&
                    current.originState.text.isNotBlank() &&
                    current.roastLevelState.text.isNotBlank() &&
                    current.brewingMethod.text.isNotBlank()
        }
            .distinctUntilChanged()
            .onEach { canSave ->
                _state.update {
                    it.copy(
                        isSaveEnabled = canSave
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun onAction(action: NewCoffeeAction) {
        when (action) {

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
                val current = _state.value

                current.coffeeNameState.replaceText(coffeeToEdit.name)
                current.roasterState.replaceText(coffeeToEdit.roaster)
                current.seriesCollectionState.replaceText(coffeeToEdit.series.orEmpty())
                current.originState.replaceText(coffeeToEdit.origin)
                current.processState.replaceText(coffeeToEdit.process.orEmpty())
                current.elevationState.replaceText(coffeeToEdit.elevation.orEmpty())
                current.roastLevelState.replaceText(coffeeToEdit.roastLevel)
                current.brewingMethod.replaceText(coffeeToEdit.brewingMethod)
                current.grindSizeState.replaceText(coffeeToEdit.grindSize.orEmpty())
                current.temperatureState.replaceText(coffeeToEdit.temperature.orEmpty())
                current.ratioState.replaceText(coffeeToEdit.ratio.orEmpty())
                current.brewDuration.replaceText(coffeeToEdit.brewTime.orEmpty())
                current.tastingNotesState.replaceText(coffeeToEdit.notes.orEmpty())

                _state.update {
                    it.copy(
                        coffeeId = coffeeToEdit.id,
                        photoUri = coffeeToEdit.imageUrl,
                        roastDate = coffeeToEdit.roastDate,
                        overallRating = coffeeToEdit.rating,
                        createdAt = coffeeToEdit.createdAt,
                        lastModifiedAt = coffeeToEdit.lastModifiedAt
                    )
                }

            }
        }
    }

    private fun TextFieldState.replaceText(value: String) {
        edit {
            replace(0, length, value)
        }
    }

    private fun saveCoffee() {
        viewModelScope.launch {

            // TODO validate fields

            coffeeRepository.upsertCoffee(
                coffee = Coffee(
                    id = _state.value.coffeeId,
                    imageUrl = _state.value.photoUri,
                    name = _state.value.coffeeNameState.text.toString().trim(),
                    roaster = _state.value.roasterState.text.toString().trim(),
                    series = _state.value.seriesCollectionState.text.toString().trim(),
                    origin = _state.value.originState.text.toString().trim(),
                    process = _state.value.processState.text.toString().trim(),
                    elevation = _state.value.elevationState.text.toString().trim(),
                    roastDate = _state.value.roastDate,
                    roastLevel = _state.value.roastLevelState.text.toString().trim(),
                    brewingMethod = _state.value.brewingMethod.text.toString().trim(),
                    grindSize = _state.value.grindSizeState.text.toString().trim(),
                    temperature = _state.value.temperatureState.text.toString().trim(),
                    ratio = _state.value.ratioState.text.toString().trim(),
                    brewTime = _state.value.brewDuration.text.toString().trim(),
                    rating = _state.value.overallRating,
                    notes = _state.value.tastingNotesState.text.toString().trim(),
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
