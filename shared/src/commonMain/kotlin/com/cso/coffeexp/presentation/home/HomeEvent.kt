package com.cso.coffeexp.presentation.home

import com.cso.coffeexp.core.design_system.utils.UiText

sealed interface HomeEvent {
    data class ShowUndoDelete(val coffeeId: Long, val message: UiText) : HomeEvent
    data class DeleteFailed(val message: UiText) : HomeEvent
}
