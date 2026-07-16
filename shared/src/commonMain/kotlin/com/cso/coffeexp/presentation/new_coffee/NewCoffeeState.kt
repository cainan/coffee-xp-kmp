package com.cso.coffeexp.presentation.new_coffee

data class NewCoffeeState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)