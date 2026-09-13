package com.cso.coffeexp.presentation.home

import com.cso.coffeexp.domain.model.Coffee

sealed interface HomeAction {
    data object OnNewCoffeeClick : HomeAction
    data class OnDetailsClick(val coffeeId: Long) : HomeAction

    data class OnCoffeeSwipedToRemove(val coffee: Coffee) : HomeAction
    data class OnUndoDeleteClick(val coffeeId: Long) : HomeAction
    data class OnUndoDeleteDismissed(val coffeeId: Long) : HomeAction
    data class OnSearch(val query: String) : HomeAction
}
