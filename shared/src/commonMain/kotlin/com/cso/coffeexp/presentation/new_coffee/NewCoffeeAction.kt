package com.cso.coffeexp.presentation.new_coffee

sealed interface NewCoffeeAction {

    data class OnCoffeeToEditSelected(val coffeeId: Long) : NewCoffeeAction
    data object OnBackClick : NewCoffeeAction
    data object OnPhotoClick : NewCoffeeAction
    data class OnBrewingMethodExpandedChange(val expanded: Boolean) : NewCoffeeAction
    data class OnBrewingMethodSelected(val value: String) : NewCoffeeAction
    data class OnRatingChange(val rating: Double) : NewCoffeeAction
    data object OnSaveClick : NewCoffeeAction
}
