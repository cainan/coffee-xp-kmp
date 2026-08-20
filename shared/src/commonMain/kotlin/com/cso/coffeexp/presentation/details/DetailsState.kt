package com.cso.coffeexp.presentation.details

import androidx.compose.runtime.Stable
import com.cso.coffeexp.domain.model.Coffee

@Stable
data class DetailsState(
    val isLoading : Boolean = false,
    val coffee: Coffee? = null,
)
