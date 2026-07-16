package com.cso.coffeexp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface UIRoute {

    @Serializable
    data object Home : UIRoute

    @Serializable
    data object NewCoffee : UIRoute

    @Serializable
    data class Details(val coffeeId: Long) : UIRoute
}