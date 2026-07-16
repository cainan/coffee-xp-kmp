package com.cso.coffeexp.presentation.home

import com.cso.coffeexp.domain.model.Coffee

sealed interface HomeAction {
    data object OnNewCoffeeClick : HomeAction
    data class OnDetailsClick(val coffeeId: Long) : HomeAction

    data class OnCoffeeRemoved(val coffee: Coffee) : HomeAction
    data class OnSearch(val query: String) : HomeAction
}