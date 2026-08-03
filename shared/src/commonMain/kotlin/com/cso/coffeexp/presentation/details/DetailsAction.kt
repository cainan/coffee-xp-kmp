package com.cso.coffeexp.presentation.details

sealed interface DetailsAction {

    data class OnCoffeeIdSelected(val coffeeId: Long) : DetailsAction
    data object OnBackClick : DetailsAction
    data class OnEditClick(val coffeeId: Long) : DetailsAction
}
