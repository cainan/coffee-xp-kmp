package com.cso.coffeexp.data.repository

import com.cso.coffeexp.core.utils.LocalDate
import com.cso.coffeexp.data.mapper.toCoffee
import com.cso.coffeexp.data.mapper.toCoffeeEntity
import com.cso.coffeexp.database.CoffeeXpDatabase
import com.cso.coffeexp.domain.model.Coffee
import com.cso.coffeexp.domain.repository.CoffeeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFirstCoffeeRepository(
    private val db: CoffeeXpDatabase
) : CoffeeRepository {

    override suspend fun getCoffeeById(id: Long): Coffee? =
        db.getCoffeeDao().getById(id)?.toCoffee()

    override fun getCoffees(): Flow<List<Coffee>> =
        db.getCoffeeDao().observeAll().map { coffeeEntityList ->
            coffeeEntityList.map { it.toCoffee() }
        }

    override suspend fun upsertCoffee(coffee: Coffee): Long =
        db.getCoffeeDao().upsert(coffee.toCoffeeEntity())

    override suspend fun deleteCoffee(id: Long) = db.getCoffeeDao().deleteById(id)

}
