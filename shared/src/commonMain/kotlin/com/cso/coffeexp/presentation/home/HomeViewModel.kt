package com.cso.coffeexp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cso.coffeexp.domain.logger.CoffeeXpLogger
import com.cso.coffeexp.domain.repository.CoffeeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val coffeeRepository: CoffeeRepository,
    private val logger: CoffeeXpLogger,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val state = combine(
        coffeeRepository.getCoffees(),
        searchQuery,
    ) { coffees, query ->
        HomeState(
            isLoading = false,
            coffeeList = coffees.filter { coffee ->
                query.isBlank() ||
                    coffee.name.contains(query, ignoreCase = true) ||
                    coffee.roaster?.contains(query, ignoreCase = true) == true
            },
            searchQuery = query,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeState(isLoading = true),
    )

    fun onAction(action: HomeAction) {
        logger.debug("${this.toString()} - action received: $action")
        when (action) {
            is HomeAction.OnSearch -> searchQuery.value = action.query
            is HomeAction.OnCoffeeRemoved -> viewModelScope.launch {
                action.coffee.id?.let { coffeeRepository.deleteCoffee(it) }
            }

            is HomeAction.OnNewCoffeeClick,
            is HomeAction.OnDetailsClick -> Unit // navigation is handled by HomeRoot
        }
    }
}
