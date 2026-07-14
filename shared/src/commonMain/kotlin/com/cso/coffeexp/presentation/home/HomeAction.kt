package com.cso.coffeexp.presentation.home

import com.cso.coffeexp.domain.model.Coffee

sealed interface HomeAction {
    data class OnCoffeeRemoved(val coffee: Coffee) : HomeAction
    data class OnSearch(val query: String) : HomeAction
}