package com.cso.coffeexp.presentation.details

import androidx.compose.runtime.Stable
import com.cso.coffeexp.domain.model.Coffee

@Stable
data class DetailsState(
    val coffee: Coffee = Coffee(),
)
