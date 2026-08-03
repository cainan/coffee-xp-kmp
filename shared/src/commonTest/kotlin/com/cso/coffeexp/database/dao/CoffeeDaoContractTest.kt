package com.cso.coffeexp.database.dao

import com.cso.coffeexp.database.CoffeeXpDatabase
import com.cso.coffeexp.database.entity.CoffeeEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull

abstract class CoffeeDaoContractTest {

    protected abstract fun createDatabase(): CoffeeXpDatabase

    private lateinit var database: CoffeeXpDatabase
    private lateinit var dao: CoffeeDAO

    @BeforeTest
    fun setUp() {
        database = createDatabase()
        dao = database.getCoffeeDao()
    }

    @AfterTest
    fun tearDown() {
        database.close()
    }

    @Test
    fun `upsert inserts a coffee and getById returns all fields`() = runTest {
        val coffee = coffeeEntity(
            imageUrl = null,
            series = null,
            process = null,
            elevation = null,
            grindSize = null,
            temperature = null,
            ratio = null,
            brewTime = null,
            notes = null,
        )

        val generatedId = dao.upsert(coffee)

        assertEquals(coffee.copy(id = generatedId), dao.getById(generatedId))
    }

    @Test
    fun `upsert updates an existing coffee without creating a duplicate`() = runTest {
        val generatedId = dao.upsert(coffeeEntity(name = "Original"))

        dao.upsert(coffeeEntity(id = generatedId, name = "Updated"))

        assertEquals("Updated", dao.getById(generatedId)?.name)
        assertEquals(1, dao.observeAll().first().size)
    }

    @Test
    fun `observeAll orders coffees by createdAt and id descending`() = runTest {
        val oldestId = dao.upsert(coffeeEntity(name = "Oldest", createdAt = "100"))
        val firstNewestId = dao.upsert(coffeeEntity(name = "First newest", createdAt = "200"))
        val secondNewestId = dao.upsert(coffeeEntity(name = "Second newest", createdAt = "200"))

        val coffees = dao.observeAll().first()

        assertContentEquals(
            listOf(secondNewestId, firstNewestId, oldestId),
            coffees.map(CoffeeEntity::id),
        )
    }

    @Test
    fun `deleteById removes an existing coffee`() = runTest {
        val generatedId = dao.upsert(coffeeEntity())

        assertEquals(1, dao.deleteById(generatedId))
        assertNull(dao.getById(generatedId))
    }

    @Test
    fun `getById returns null when coffee does not exist`() = runTest {
        assertNull(dao.getById(NON_EXISTENT_ID))
    }

    @Test
    fun `deleteById returns zero when coffee does not exist`() = runTest {
        assertEquals(0, dao.deleteById(NON_EXISTENT_ID))
    }

    private fun coffeeEntity(
        id: Long = 0,
        imageUrl: String? = "https://example.com/coffee.jpg",
        name: String = "Test coffee",
        roaster: String = "Test roaster",
        series: String? = "Test series",
        origin: String = "Brazil",
        process: String? = "Natural",
        elevation: String? = "1,200m",
        roastDate: String = "10",
        roastLevel: String = "Medium",
        brewingMethod: String = "V60",
        grindSize: String? = "Medium-fine",
        temperature: String? = "93",
        ratio: String? = "1:16",
        brewTime: String? = "03:00",
        rating: Double = 4.5,
        notes: String? = "Chocolate",
        createdAt: String = "2000",
        lastModifiedAt: String = "2000",
    ) = CoffeeEntity(
        id = id,
        imageUrl = imageUrl,
        name = name,
        roaster = roaster,
        series = series,
        origin = origin,
        process = process,
        elevation = elevation,
        roastDate = roastDate,
        roastLevel = roastLevel,
        brewingMethod = brewingMethod,
        grindSize = grindSize,
        temperature = temperature,
        ratio = ratio,
        brewTime = brewTime,
        rating = rating,
        notes = notes,
        createdAt = createdAt,
        lastModifiedAt = lastModifiedAt,
    )

    private companion object {
        const val NON_EXISTENT_ID = 404L
    }
}
