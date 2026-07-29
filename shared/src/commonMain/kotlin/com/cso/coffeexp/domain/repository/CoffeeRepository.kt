package com.cso.coffeexp.domain.repository

import com.cso.coffeexp.domain.model.Coffee
import kotlinx.coroutines.flow.Flow

interface CoffeeRepository {
    suspend fun getCoffeeById(id: Long): Coffee?
    fun getCoffees(): Flow<List<Coffee>>
    suspend fun upsertCoffee(coffee: Coffee) : Long
    suspend fun deleteCoffee(id: Long) : Int
}
