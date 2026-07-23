package com.cso.coffeexp.presentation.new_coffee

sealed interface NewCoffeeAction {
    data object OnBackClick : NewCoffeeAction
    data object OnPhotoClick : NewCoffeeAction
    data class OnBrewingMethodExpandedChange(val expanded: Boolean) : NewCoffeeAction
    data class OnBrewingMethodSelected(val value: String) : NewCoffeeAction
    data class OnRatingChange(val rating: Int) : NewCoffeeAction
    data object OnSaveClick : NewCoffeeAction
}
