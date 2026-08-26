package com.cso.coffeexp.presentation.new_coffee

import kotlinx.datetime.LocalDate

sealed interface NewCoffeeAction {

    data class OnCoffeeToEditSelected(val coffeeId: Long) : NewCoffeeAction
    data object OnBackClick : NewCoffeeAction
    data object OnPhotoClick : NewCoffeeAction
    data object OnDismissPhotoPickerSheet : NewCoffeeAction
    data class OnPhotoBytesSelected(val bytes: ByteArray) : NewCoffeeAction
    data class OnPhotoSelected(val uri: String?) : NewCoffeeAction
    data object OnRemovePhotoClick : NewCoffeeAction
    data class OnRatingChange(val rating: Double) : NewCoffeeAction
    data class OnRoastDateSelected(val date: LocalDate) : NewCoffeeAction
    data object OnSaveClick : NewCoffeeAction
}
