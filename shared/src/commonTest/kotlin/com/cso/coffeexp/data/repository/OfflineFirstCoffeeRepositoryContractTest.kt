package com.cso.coffeexp.data.repository

import androidx.room.execSQL
import androidx.room.useWriterConnection
import app.cash.turbine.test
import com.cso.coffeexp.core.error_handling.DataError
import com.cso.coffeexp.core.error_handling.Result
import com.cso.coffeexp.database.CoffeeXpDatabase
import com.cso.coffeexp.domain.model.Coffee
import com.cso.coffeexp.testutil.coffeeFixture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

abstract class OfflineFirstCoffeeRepositoryContractTest {
    protected abstract fun createDatabase(): CoffeeXpDatabase

    private lateinit var database: CoffeeXpDatabase
    private lateinit var applicationScope: CoroutineScope
    private lateinit var repository: OfflineFirstCoffeeRepository

    @BeforeTest
    fun setUp() {
        database = createDatabase()
        applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
        repository = OfflineFirstCoffeeRepository(database, applicationScope)
    }

    @AfterTest
    fun tearDown() {
        applicationScope.cancel()
        database.close()
    }

    @Test
    fun `upsert and get by id preserve every coffee field`() = runTest {
        val coffee = coffeeFixture(id = null)

        val id = upsertSuccessfully(coffee)

        assertEquals(coffee.copy(id = id), getByIdSuccessfully(id))
    }

    @Test
    fun `observing coffees emits mapped database updates`() = runTest {
        repository.getCoffees().test {
            assertEquals(emptyList(), awaitItem())

            val first = coffeeFixture(id = null, name = "First")
            val firstId = upsertSuccessfully(first)
            assertEquals(listOf(first.copy(id = firstId)), awaitItem())

            val second = coffeeFixture(id = null, name = "Second")
            val secondId = upsertSuccessfully(second)
            assertEquals(
                listOf(second.copy(id = secondId), first.copy(id = firstId)),
                awaitItem(),
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `get by id returns null when coffee does not exist`() = runTest {
        assertNull(getByIdSuccessfully(404L))
    }

    @Test
    fun `upsert updates an existing coffee`() = runTest {
        val id = upsertSuccessfully(coffeeFixture(id = null, name = "Original"))

        upsertSuccessfully(coffeeFixture(id = id, name = "Updated"))

        assertEquals("Updated", getByIdSuccessfully(id)?.name)
        assertEquals(1, database.getCoffeeDao().observeAll().testItemCount())
    }

    @Test
    fun `delete removes coffee and is idempotent`() = runTest {
        val id = upsertSuccessfully(coffeeFixture(id = null))

        assertIs<Result.Success<Unit>>(repository.deleteCoffee(id))
        assertNull(getByIdSuccessfully(id))
        assertIs<Result.Success<Unit>>(repository.deleteCoffee(id))
    }

    @Test
    fun `sqlite errors other than disk full are reported as unknown`() = runTest {
        // A real SQLiteException ("no such table", SQLITE_ERROR) from the platform driver.
        database.useWriterConnection { it.execSQL("DROP TABLE coffee") }

        val result = repository.upsertCoffee(coffeeFixture(id = null))

        assertEquals(DataError.Local.UNKNOWN, assertIs<Result.Failure<DataError.Local>>(result).error)
    }

    private suspend fun <T> Flow<List<T>>.testItemCount(): Int {
        var count = 0
        test {
            count = awaitItem().size
            cancelAndIgnoreRemainingEvents()
        }
        return count
    }

    private suspend fun upsertSuccessfully(coffee: Coffee): Long {
        val result = repository.upsertCoffee(coffee)
        return assertIs<Result.Success<Long>>(result).data
    }

    private suspend fun getByIdSuccessfully(id: Long): Coffee? {
        val result = repository.getCoffeeById(id)
        return assertIs<Result.Success<Coffee?>>(result).data
    }
}
