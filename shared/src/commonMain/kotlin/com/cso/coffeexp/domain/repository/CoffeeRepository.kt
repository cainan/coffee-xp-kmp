package com.cso.coffeexp.domain.repository

import com.cso.coffeexp.core.error_handling.DataError
import com.cso.coffeexp.core.error_handling.Result
import com.cso.coffeexp.domain.model.Coffee
import kotlinx.coroutines.flow.Flow

interface CoffeeRepository {
    suspend fun getCoffeeById(id: Long): Result<Coffee?, DataError.Local>
    fun getCoffees(): Flow<List<Coffee>>
    suspend fun upsertCoffee(coffee: Coffee): Result<Long, DataError.Local>
    suspend fun deleteCoffee(id: Long): Int
}
