package com.cso.coffeexp.presentation.details

import app.cash.turbine.test
import com.cso.coffeexp.core.error_handling.DataError
import com.cso.coffeexp.core.error_handling.Result
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {
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
    fun `selecting an existing coffee loads details`() = runTest {
        val coffee = coffeeFixture(id = 12L)
        val repository = FakeCoffeeRepository().apply { coffeeById = coffee }
        val viewModel = DetailsViewModel(repository, FakeCoffeeXpLogger())

        viewModel.state.test {
            assertNull(awaitItem().coffee)
            viewModel.onAction(DetailsAction.OnCoffeeIdSelected(12L))
            assertEquals(coffee, awaitItem().coffee)
            cancelAndIgnoreRemainingEvents()
        }
        assertEquals(listOf(12L), repository.requestedIds)
    }

    @Test
    fun `selecting a missing coffee keeps empty state`() = runTest {
        val repository = FakeCoffeeRepository()
        val viewModel = DetailsViewModel(repository, FakeCoffeeXpLogger())

        viewModel.state.test {
            assertNull(awaitItem().coffee)
            viewModel.onAction(DetailsAction.OnCoffeeIdSelected(404L))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
        assertEquals(listOf(404L), repository.requestedIds)
    }

    @Test
    fun `repository failure keeps details empty`() = runTest {
        val repository = FakeCoffeeRepository().apply {
            getCoffeeByIdResult = Result.Failure(DataError.Local.UNKNOWN)
        }
        val viewModel = DetailsViewModel(repository, FakeCoffeeXpLogger())

        viewModel.state.test {
            assertNull(awaitItem().coffee)
            viewModel.onAction(DetailsAction.OnCoffeeIdSelected(12L))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(listOf(12L), repository.requestedIds)
    }

    @Test
    fun `back and edit actions do not call repository`() = runTest {
        val repository = FakeCoffeeRepository()
        val viewModel = DetailsViewModel(repository, FakeCoffeeXpLogger())

        viewModel.onAction(DetailsAction.OnBackClick)
        viewModel.onAction(DetailsAction.OnEditClick(5L))

        assertTrue(repository.requestedIds.isEmpty())
        assertTrue(repository.upsertedCoffees.isEmpty())
        assertTrue(repository.deletedIds.isEmpty())
    }
}
