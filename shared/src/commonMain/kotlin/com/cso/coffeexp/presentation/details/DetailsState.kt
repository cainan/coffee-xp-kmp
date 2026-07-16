package com.cso.coffeexp.presentation.details

data class DetailsState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)