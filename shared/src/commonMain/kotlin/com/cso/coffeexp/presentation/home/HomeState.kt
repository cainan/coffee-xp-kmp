package com.cso.coffeexp.presentation.home

import androidx.compose.runtime.Stable
import com.cso.coffeexp.domain.model.Coffee

@Stable
data class HomeState(
    val isLoading: Boolean = false,
    val coffeeList: List<Coffee>? = null,
    val searchQuery: String = "",
)
