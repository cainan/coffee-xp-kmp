package com.cso.coffeexp.presentation.home

import com.cso.coffeexp.domain.model.Coffee

data class HomeState(
    val isLoading: Boolean = false,
    val coffeeList: List<Coffee>? = null,
)