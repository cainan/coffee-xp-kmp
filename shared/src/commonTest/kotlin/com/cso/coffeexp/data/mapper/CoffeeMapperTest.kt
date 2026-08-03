package com.cso.coffeexp.data.mapper

import com.cso.coffeexp.core.utils.toEpochMillisString
import com.cso.coffeexp.database.entity.CoffeeEntity
import com.cso.coffeexp.domain.model.Coffee
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class CoffeeMapperTest {

    @Test
    fun `toCoffee maps all entity fields`() {
        val entity = coffeeEntity()

        assertEquals(coffee(), entity.toCoffee())
    }

    @Test
    fun `toCoffee preserves null optional entity fields`() {
        val entity = coffeeEntity(
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

        val coffee = entity.toCoffee()

        assertEquals(null, coffee.imageUrl)
        assertEquals(null, coffee.series)
        assertEquals(null, coffee.process)
        assertEquals(null, coffee.elevation)
        assertEquals(null, coffee.grindSize)
        assertEquals(null, coffee.temperature)
        assertEquals(null, coffee.ratio)
        assertEquals(null, coffee.brewTime)
        assertEquals(null, coffee.notes)
    }

    @Test
    fun `toCoffeeEntity maps all domain fields and preserves timestamps`() {
        val coffee = coffee()

        assertEquals(coffeeEntity(), coffee.toCoffeeEntity())
    }

    @Test
    fun `toCoffeeEntity preserves null optional fields and uses zero for a null id`() {
        val coffee = coffee(
            id = null,
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

        val entity = coffee.toCoffeeEntity()

        assertEquals(0, entity.id)
        assertEquals(null, entity.imageUrl)
        assertEquals(null, entity.series)
        assertEquals(null, entity.process)
        assertEquals(null, entity.elevation)
        assertEquals(null, entity.grindSize)
        assertEquals(null, entity.temperature)
        assertEquals(null, entity.ratio)
        assertEquals(null, entity.brewTime)
        assertEquals(null, entity.notes)
    }

    private fun coffee(
        id: Long? = 42,
        imageUrl: String? = "https://example.com/coffee.jpg",
        series: String? = "Microlot Series",
        process: String? = "Natural",
        elevation: String? = "1,200m",
        grindSize: String? = "Medium-fine",
        temperature: String? = "93°C",
        ratio: String? = "1:16",
        brewTime: String? = "03:00",
        notes: String? = "Chocolate and caramel",
    ) = Coffee(
        id = id,
        imageUrl = imageUrl,
        name = "Yellow Bourbon",
        roaster = "Coffee XP Roasters",
        series = series,
        origin = "Brazil",
        process = process,
        elevation = elevation,
        roastDate = ROAST_DATE,
        roastLevel = "Medium",
        brewingMethod = "V60",
        grindSize = grindSize,
        temperature = temperature,
        ratio = ratio,
        brewTime = brewTime,
        rating = 8.5,
        notes = notes,
        createdAt = CREATED_AT,
        lastModifiedAt = LAST_MODIFIED_AT,
    )

    private fun coffeeEntity(
        id: Long = 42,
        imageUrl: String? = "https://example.com/coffee.jpg",
        series: String? = "Microlot Series",
        process: String? = "Natural",
        elevation: String? = "1,200m",
        grindSize: String? = "Medium-fine",
        temperature: String? = "93°C",
        ratio: String? = "1:16",
        brewTime: String? = "03:00",
        notes: String? = "Chocolate and caramel",
    ) = CoffeeEntity(
        id = id,
        imageUrl = imageUrl,
        name = "Yellow Bourbon",
        roaster = "Coffee XP Roasters",
        series = series,
        origin = "Brazil",
        process = process,
        elevation = elevation,
        roastDate = ROAST_DATE.toEpochMillisString(),
        roastLevel = "Medium",
        brewingMethod = "V60",
        grindSize = grindSize,
        temperature = temperature,
        ratio = ratio,
        brewTime = brewTime,
        rating = 8.5,
        notes = notes,
        createdAt = CREATED_AT.toEpochMillisString(),
        lastModifiedAt = LAST_MODIFIED_AT.toEpochMillisString(),
    )

    private companion object {
        val ROAST_DATE = LocalDate(2026, 7, 15)
        val CREATED_AT = LocalDate(2026, 7, 20)
        val LAST_MODIFIED_AT = LocalDate(2026, 7, 25)
    }
}
