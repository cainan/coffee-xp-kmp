package com.cso.coffeexp.presentation.new_coffee

sealed interface NewCoffeeEvent {
    data object AddedSuccessfully : NewCoffeeEvent
}