package com.cso.coffeexp.data.repository

import com.cso.coffeexp.core.error_handling.DataError
import com.cso.coffeexp.core.error_handling.EmptyResult
import com.cso.coffeexp.core.error_handling.Result
import com.cso.coffeexp.core.error_handling.asEmptyResult
import com.cso.coffeexp.core.error_handling.safeDatabaseUpdate
import com.cso.coffeexp.data.mapper.toCoffee
import com.cso.coffeexp.data.mapper.toCoffeeEntity
import com.cso.coffeexp.database.CoffeeXpDatabase
import com.cso.coffeexp.domain.model.Coffee
import com.cso.coffeexp.domain.repository.CoffeeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFirstCoffeeRepository(
    private val db: CoffeeXpDatabase,
    private val applicationScope: CoroutineScope,
) : CoffeeRepository {

    override suspend fun getCoffeeById(id: Long): Result<Coffee?, DataError.Local> =
        safeDatabaseUpdate { db.getCoffeeDao().getById(id)?.toCoffee() }

    override fun getCoffees(): Flow<List<Coffee>> =
        db.getCoffeeDao().observeAll().map { coffeeEntityList ->
            coffeeEntityList.map { it.toCoffee() }
        }

    override suspend fun upsertCoffee(coffee: Coffee): Result<Long, DataError.Local> {
        return safeDatabaseUpdate {
            db.getCoffeeDao().upsert(coffee.toCoffeeEntity())
        }
    }

    override suspend fun deleteCoffee(id: Long): EmptyResult<DataError.Local> {
        return applicationScope.async {
            safeDatabaseUpdate {
                db.getCoffeeDao().deleteById(id)
            }.asEmptyResult()
        }.await()
    }

}
