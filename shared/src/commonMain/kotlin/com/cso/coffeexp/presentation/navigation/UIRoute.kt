package com.cso.coffeexp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface UIRoute {

    @Serializable
    data object Home : UIRoute

}