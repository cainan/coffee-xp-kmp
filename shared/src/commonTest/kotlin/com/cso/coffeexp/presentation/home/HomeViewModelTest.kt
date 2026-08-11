package com.cso.coffeexp.presentation.home

import app.cash.turbine.test
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
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
    fun `state emits loading then repository coffees`() = runTest {
        val coffees = listOf(coffeeFixture())
        val viewModel = HomeViewModel(FakeCoffeeRepository(coffees), FakeCoffeeXpLogger())

        assertTrue(viewModel.state.value.isLoading)
        viewModel.state.test {
            val loaded = awaitItem()
            assertFalse(loaded.isLoading)
            assertEquals(coffees, loaded.coffeeList)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search filters name and roaster ignoring case and clearing restores list`() = runTest {
        val first = coffeeFixture(id = 1L, name = "Yellow Bourbon", roaster = "North Roasters")
        val second = coffeeFixture(id = 2L, name = "Geisha", roaster = "Coffee XP")
        val viewModel = HomeViewModel(
            FakeCoffeeRepository(listOf(first, second)),
            FakeCoffeeXpLogger(),
        )

        viewModel.state.test {
            assertEquals(listOf(first, second), awaitItem().coffeeList)

            viewModel.onAction(HomeAction.OnSearch("bOuRbOn"))
            assertEquals(listOf(first), awaitItem().coffeeList)

            viewModel.onAction(HomeAction.OnSearch("COFFEE xp"))
            assertEquals(listOf(second), awaitItem().coffeeList)

            viewModel.onAction(HomeAction.OnSearch(""))
            assertEquals(listOf(first, second), awaitItem().coffeeList)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `remove deletes only coffees that have an id`() = runTest {
        val repository = FakeCoffeeRepository()
        val viewModel = HomeViewModel(repository, FakeCoffeeXpLogger())

        viewModel.onAction(HomeAction.OnCoffeeRemoved(coffeeFixture(id = 7L)))
        viewModel.onAction(HomeAction.OnCoffeeRemoved(coffeeFixture(id = null)))

        assertEquals(listOf(7L), repository.deletedIds)
    }

    @Test
    fun `navigation actions do not call repository`() = runTest {
        val repository = FakeCoffeeRepository()
        val viewModel = HomeViewModel(repository, FakeCoffeeXpLogger())

        viewModel.onAction(HomeAction.OnNewCoffeeClick)
        viewModel.onAction(HomeAction.OnDetailsClick(8L))

        assertTrue(repository.requestedIds.isEmpty())
        assertTrue(repository.upsertedCoffees.isEmpty())
        assertTrue(repository.deletedIds.isEmpty())
    }
}
