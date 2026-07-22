package com.cso.coffeexp.domain.repository

import com.cso.coffeexp.domain.model.Coffee
import kotlinx.coroutines.flow.Flow

interface CoffeeRepository {
    fun getCoffees(): Flow<List<Coffee>>
    suspend fun deleteCoffee(id: Long)
}
