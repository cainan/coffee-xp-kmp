package com.cso.coffeexp.presentation.details

sealed interface DetailsAction {
    data object OnBackClick : DetailsAction
    data object OnEditClick : DetailsAction
}
