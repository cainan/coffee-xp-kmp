package com.cso.coffeexp.data.coffee

import com.cso.coffeexp.domain.mock.mockCoffeeList
import com.cso.coffeexp.domain.model.Coffee
import com.cso.coffeexp.domain.repository.CoffeeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemoryCoffeeRepository : CoffeeRepository {

    private val coffees = MutableStateFlow(mockCoffeeList)

    override fun getCoffees(): Flow<List<Coffee>> = coffees.asStateFlow()

    override suspend fun deleteCoffee(id: Long) {
        coffees.update { list -> list.filterNot { it.id == id } }
    }
}
