package com.cso.coffeexp.presentation.new_coffee

import app.cash.turbine.test
import com.cso.coffeexp.core.utils.toEpochMillisString
import com.cso.coffeexp.testutil.FakeCoffeeRepository
import com.cso.coffeexp.testutil.FakeCoffeeXpLogger
import com.cso.coffeexp.testutil.coffeeFixture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class NewCoffeeViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `form actions update expansion brewing method and rating`() = runTest {
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), FakeCoffeeRepository())

        viewModel.state.test {
            awaitItem()

            viewModel.onAction(NewCoffeeAction.OnBrewingMethodExpandedChange(true))
            assertEquals(true, awaitItem().isBrewingMethodExpanded)

            viewModel.onAction(NewCoffeeAction.OnBrewingMethodSelected("Aeropress"))
            val selected = awaitItem()
            assertEquals("Aeropress", selected.brewingMethod)
            assertFalse(selected.isBrewingMethodExpanded)

            viewModel.onAction(NewCoffeeAction.OnRatingChange(9.0))
            assertEquals(9.0, awaitItem().overallRating)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `selecting existing coffee fills every form field`() = runTest {
        val coffee = coffeeFixture(id = 21L)
        val repository = FakeCoffeeRepository().apply { coffeeById = coffee }
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), repository)

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(NewCoffeeAction.OnCoffeeToEditSelected(21L))
            val state = awaitItem()

            assertEquals(coffee.id, state.coffeeId)
            assertEquals(coffee.imageUrl, state.photoUri)
            assertEquals(coffee.name, state.coffeeNameState.text.toString())
            assertEquals(coffee.roaster, state.roasterState.text.toString())
            assertEquals(coffee.series, state.seriesCollectionState.text.toString())
            assertEquals(coffee.origin, state.originState.text.toString())
            assertEquals(coffee.process, state.processState.text.toString())
            assertEquals(coffee.elevation, state.elevationState.text.toString())
            assertEquals(coffee.roastDate.toEpochMillisString(), state.roastDateState.text.toString())
            assertEquals(coffee.roastLevel, state.roastLevelState.text.toString())
            assertEquals(coffee.brewingMethod, state.brewingMethod)
            assertEquals(coffee.grindSize, state.grindSizeState.text.toString())
            assertEquals(coffee.temperature, state.temperatureState.text.toString())
            assertEquals(coffee.ratio, state.ratioState.text.toString())
            assertEquals(coffee.brewTime, state.brewDuration.text.toString())
            assertEquals(coffee.rating, state.overallRating)
            assertEquals(coffee.notes, state.tastingNotesState.text.toString())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `selecting missing coffee keeps empty state`() = runTest {
        val repository = FakeCoffeeRepository()
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), repository)

        viewModel.state.test {
            val initial = awaitItem()
            viewModel.onAction(NewCoffeeAction.OnCoffeeToEditSelected(404L))
            expectNoEvents()
            assertNull(initial.coffeeId)
            cancelAndIgnoreRemainingEvents()
        }
        assertEquals(listOf(404L), repository.requestedIds)
    }

    @Test
    fun `save converts current form state and calls repository`() = runTest {
        val coffee = coffeeFixture(id = 31L)
        val repository = FakeCoffeeRepository().apply { coffeeById = coffee }
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), repository)

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(NewCoffeeAction.OnCoffeeToEditSelected(31L))
            awaitItem()
            viewModel.onAction(NewCoffeeAction.OnSaveClick)
            cancelAndIgnoreRemainingEvents()
        }

        val saved = repository.upsertedCoffees.single()
        assertEquals(coffee.imageUrl, saved.imageUrl)
        assertEquals(coffee.name, saved.name)
        assertEquals(coffee.roaster, saved.roaster)
        assertEquals(coffee.series, saved.series)
        assertEquals(coffee.origin, saved.origin)
        assertEquals(coffee.process, saved.process)
        assertEquals(coffee.elevation, saved.elevation)
        assertEquals(coffee.roastLevel, saved.roastLevel)
        assertEquals(coffee.brewingMethod, saved.brewingMethod)
        assertEquals(coffee.grindSize, saved.grindSize)
        assertEquals(coffee.temperature, saved.temperature)
        assertEquals(coffee.ratio, saved.ratio)
        assertEquals(coffee.brewTime, saved.brewTime)
        assertEquals(coffee.rating, saved.rating)
        assertEquals(coffee.notes, saved.notes)
    }

    @Test
    fun `saving an edited coffee preserves its id`() = runTest {
        val coffee = coffeeFixture(id = 41L)
        val repository = FakeCoffeeRepository().apply { coffeeById = coffee }
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), repository)

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(NewCoffeeAction.OnCoffeeToEditSelected(41L))
            awaitItem()
            viewModel.onAction(NewCoffeeAction.OnSaveClick)
            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(41L, repository.upsertedCoffees.single().id)
    }
}
