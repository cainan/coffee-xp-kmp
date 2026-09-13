package com.cso.coffeexp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coffeexp.shared.generated.resources.Res
import coffeexp.shared.generated.resources.error_delete_failed
import coffeexp.shared.generated.resources.home_coffee_deleted
import com.cso.coffeexp.core.design_system.utils.UiText
import com.cso.coffeexp.core.error_handling.onFailure
import com.cso.coffeexp.domain.logger.CoffeeXpLogger
import com.cso.coffeexp.domain.model.Coffee
import com.cso.coffeexp.domain.repository.CoffeeRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.getScopeName

class HomeViewModel(
    private val coffeeRepository: CoffeeRepository,
    private val logger: CoffeeXpLogger,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    // Coffees removed from the list but not (yet) deleted, or already deleted while Room
    // hasn't re-emitted. Deleted ids stay here: autoGenerate ids are never reused.
    private val hiddenCoffeeIds = MutableStateFlow<Set<Long>>(emptySet())

    // Only the latest swiped coffee can be undone.
    private var pendingDeletion: Coffee? = null

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = combine(
        coffeeRepository.getCoffees(),
        searchQuery,
        hiddenCoffeeIds,
    ) { coffees, query, hiddenIds ->
        HomeState(
            isLoading = false,
            coffeeList = coffees.filter { coffee ->
                coffee.id !in hiddenIds && (query.isBlank() ||
                        coffee.name.contains(query, ignoreCase = true) ||
                        coffee.roaster.contains(query, ignoreCase = true))
            },
            searchQuery = query,
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeState(isLoading = true),
    )

    fun onAction(action: HomeAction) {
        logger.debug("${this.getScopeName()} - action received: $action")
        when (action) {
            is HomeAction.OnSearch -> {
                searchQuery.value = action.query
            }

            is HomeAction.OnCoffeeSwipedToRemove -> onCoffeeSwipedToRemove(action.coffee)
            is HomeAction.OnUndoDeleteClick -> undoDelete(action.coffeeId)
            is HomeAction.OnUndoDeleteDismissed -> confirmDelete(action.coffeeId)

            is HomeAction.OnNewCoffeeClick,
            is HomeAction.OnDetailsClick -> Unit // navigation is handled by HomeRoot
        }
    }

    private fun onCoffeeSwipedToRemove(coffee: Coffee) {
        val coffeeId = coffee.id ?: return

        // The same swipe may be reported more than once; don't restart its undo.
        if (pendingDeletion?.id == coffeeId) return

        // A new swipe confirms the previous pending deletion, if any.
        pendingDeletion?.id?.let { previousId -> deleteCoffee(previousId) }

        pendingDeletion = coffee
        hiddenCoffeeIds.update { it + coffeeId }

        viewModelScope.launch {
            eventChannel.send(
                HomeEvent.ShowUndoDelete(
                    coffeeId = coffeeId,
                    message = UiText.Resource(Res.string.home_coffee_deleted, arrayOf(coffee.name)),
                )
            )
        }
    }

    private fun undoDelete(coffeeId: Long) {
        if (pendingDeletion?.id != coffeeId) return

        pendingDeletion = null
        hiddenCoffeeIds.update { it - coffeeId }
    }

    private fun confirmDelete(coffeeId: Long) {
        if (pendingDeletion?.id != coffeeId) return

        pendingDeletion = null
        deleteCoffee(coffeeId)
    }

    private fun deleteCoffee(coffeeId: Long) {
        viewModelScope.launch {
            coffeeRepository.deleteCoffee(coffeeId)
                .onFailure { error ->
                    logger.warn("Failed to delete coffee $coffeeId: $error")
                    hiddenCoffeeIds.update { it - coffeeId }
                    eventChannel.send(
                        HomeEvent.DeleteFailed(UiText.Resource(Res.string.error_delete_failed))
                    )
                }
        }
    }
}
