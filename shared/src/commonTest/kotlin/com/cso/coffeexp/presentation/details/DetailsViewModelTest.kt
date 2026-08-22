package com.cso.coffeexp.presentation.details

import app.cash.turbine.test
import coffeexp.shared.generated.resources.Res
import coffeexp.shared.generated.resources.message_load_empty
import coffeexp.shared.generated.resources.message_load_error
import com.cso.coffeexp.core.design_system.utils.UiText
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
import kotlin.test.assertFalse
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
            assertEquals(DetailsState(isLoading = false, coffee = null), awaitItem())
            viewModel.onAction(DetailsAction.OnCoffeeIdSelected(12L))
            assertEquals(DetailsState(isLoading = true, coffee = null), awaitItem())
            assertEquals(DetailsState(isLoading = false, coffee = coffee), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        assertEquals(listOf(12L), repository.requestedIds)
    }

    @Test
    fun `selecting a missing coffee sets error state`() = runTest {
        val repository = FakeCoffeeRepository()
        val viewModel = DetailsViewModel(repository, FakeCoffeeXpLogger())

        viewModel.state.test {
            assertEquals(DetailsState(isLoading = false, coffee = null), awaitItem())
            viewModel.onAction(DetailsAction.OnCoffeeIdSelected(404L))
            assertEquals(DetailsState(isLoading = true, coffee = null), awaitItem())
            val failedState = awaitItem()
            assertFalse(failedState.isLoading)
            assertNull(failedState.coffee)
            val errorMessage = failedState.errorMessage as UiText.Resource
            assertEquals(Res.string.message_load_empty, errorMessage.id)
            cancelAndIgnoreRemainingEvents()
        }
        assertEquals(listOf(404L), repository.requestedIds)
    }

    @Test
    fun `repository failure sets error state`() = runTest {
        val repository = FakeCoffeeRepository().apply {
            getCoffeeByIdResult = Result.Failure(DataError.Local.UNKNOWN)
        }
        val viewModel = DetailsViewModel(repository, FakeCoffeeXpLogger())

        viewModel.state.test {
            assertEquals(DetailsState(isLoading = false, coffee = null), awaitItem())
            viewModel.onAction(DetailsAction.OnCoffeeIdSelected(12L))
            assertEquals(DetailsState(isLoading = true, coffee = null), awaitItem())
            val failedState = awaitItem()
            assertFalse(failedState.isLoading)
            assertNull(failedState.coffee)
            val errorMessage = failedState.errorMessage as UiText.Resource
            assertEquals(Res.string.message_load_error, errorMessage.id)
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


