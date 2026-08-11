package com.cso.coffeexp.testutil

import com.cso.coffeexp.domain.logger.CoffeeXpLogger
import com.cso.coffeexp.domain.model.Coffee
import com.cso.coffeexp.domain.repository.CoffeeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.LocalDate

class FakeCoffeeRepository(
    coffees: List<Coffee> = emptyList(),
) : CoffeeRepository {
    private val coffeesFlow = MutableStateFlow(coffees)

    var coffeeById: Coffee? = null
    var upsertResult: Long = 1L
    var deleteResult: Int = 1

    val requestedIds = mutableListOf<Long>()
    val upsertedCoffees = mutableListOf<Coffee>()
    val deletedIds = mutableListOf<Long>()
    var getCoffeesCalls = 0
        private set

    fun emitCoffees(coffees: List<Coffee>) {
        coffeesFlow.value = coffees
    }

    override suspend fun getCoffeeById(id: Long): Coffee? {
        requestedIds += id
        return coffeeById
    }

    override fun getCoffees(): Flow<List<Coffee>> {
        getCoffeesCalls++
        return coffeesFlow
    }

    override suspend fun upsertCoffee(coffee: Coffee): Long {
        upsertedCoffees += coffee
        return upsertResult
    }

    override suspend fun deleteCoffee(id: Long): Int {
        deletedIds += id
        return deleteResult
    }
}

class FakeCoffeeXpLogger : CoffeeXpLogger {
    override fun debug(message: String) = Unit
    override fun info(message: String) = Unit
    override fun warn(message: String) = Unit
    override fun error(message: String, throwable: Throwable?) = Unit
}

fun coffeeFixture(
    id: Long? = 1L,
    name: String = "Yellow Bourbon",
    roaster: String = "Coffee XP Roasters",
    series: String? = "Microlot Series",
) = Coffee(
    id = id,
    imageUrl = "https://example.com/coffee.jpg",
    name = name,
    roaster = roaster,
    series = series,
    origin = "Brazil",
    process = "Natural",
    elevation = "1,200m",
    roastDate = LocalDate(2026, 7, 15),
    roastLevel = "Medium",
    brewingMethod = "V60",
    grindSize = "Medium-fine",
    temperature = "93C",
    ratio = "1:16",
    brewTime = "03:00",
    rating = 8.5,
    notes = "Chocolate and caramel",
    createdAt = LocalDate(2026, 7, 20),
    lastModifiedAt = LocalDate(2026, 7, 25),
)
