package com.cso.coffeexp.presentation.home

import app.cash.turbine.test
import com.cso.coffeexp.core.design_system.utils.UiText
import com.cso.coffeexp.core.error_handling.DataError
import com.cso.coffeexp.core.error_handling.Result
import com.cso.coffeexp.testutil.FakeCoffeeRepository
import com.cso.coffeexp.testutil.FakeCoffeeXpLogger
import com.cso.coffeexp.testutil.coffeeFixture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
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
        assertNull(viewModel.state.value.coffeeList)
        viewModel.state.test {
            val loaded = awaitItem()
            assertFalse(loaded.isLoading)
            assertEquals(coffees, loaded.coffeeList)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state stops loading when repository emits an empty list`() = runTest {
        val viewModel = HomeViewModel(FakeCoffeeRepository(), FakeCoffeeXpLogger())

        assertTrue(viewModel.state.value.isLoading)
        assertNull(viewModel.state.value.coffeeList)

        viewModel.state.test {
            val loaded = awaitItem()
            assertFalse(loaded.isLoading)
            assertEquals(emptyList(), loaded.coffeeList)
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
    fun `swipe hides coffee and asks for undo without deleting it`() = runTest {
        val first = coffeeFixture(id = 1L, name = "Yellow Bourbon")
        val second = coffeeFixture(id = 2L, name = "Geisha")
        val repository = FakeCoffeeRepository(listOf(first, second))
        val viewModel = createViewModel(repository)

        viewModel.events.test {
            viewModel.onAction(HomeAction.OnCoffeeSwipedToRemove(second))

            val event = assertIs<HomeEvent.ShowUndoDelete>(awaitItem())
            assertEquals(2L, event.coffeeId)
            assertEquals(listOf<Any>("Geisha"), assertIs<UiText.Resource>(event.message).args.toList())
            assertEquals(listOf(first), viewModel.state.value.coffeeList)
            assertTrue(repository.deletedIds.isEmpty())
        }
    }

    @Test
    fun `undo restores coffee and never deletes it`() = runTest {
        val coffee = coffeeFixture(id = 7L)
        val repository = FakeCoffeeRepository(listOf(coffee))
        val viewModel = createViewModel(repository)

        viewModel.events.test {
            viewModel.onAction(HomeAction.OnCoffeeSwipedToRemove(coffee))
            awaitItem()

            viewModel.onAction(HomeAction.OnUndoDeleteClick(7L))
            // A late dismissal for an undone coffee must not delete it.
            viewModel.onAction(HomeAction.OnUndoDeleteDismissed(7L))

            assertEquals(listOf(coffee), viewModel.state.value.coffeeList)
            assertTrue(repository.deletedIds.isEmpty())
        }
    }

    @Test
    fun `dismissed undo deletes coffee`() = runTest {
        val coffee = coffeeFixture(id = 7L)
        val repository = FakeCoffeeRepository(listOf(coffee))
        val viewModel = createViewModel(repository)

        viewModel.events.test {
            viewModel.onAction(HomeAction.OnCoffeeSwipedToRemove(coffee))
            awaitItem()

            viewModel.onAction(HomeAction.OnUndoDeleteDismissed(7L))

            assertEquals(listOf(7L), repository.deletedIds)
            assertEquals(emptyList(), viewModel.state.value.coffeeList)
            expectNoEvents()
        }
    }

    @Test
    fun `failed delete restores coffee and notifies failure`() = runTest {
        val coffee = coffeeFixture(id = 7L)
        val repository = FakeCoffeeRepository(listOf(coffee)).apply {
            deleteResult = Result.Failure(DataError.Local.DISK_FULL)
        }
        val viewModel = createViewModel(repository)

        viewModel.events.test {
            viewModel.onAction(HomeAction.OnCoffeeSwipedToRemove(coffee))
            awaitItem()

            viewModel.onAction(HomeAction.OnUndoDeleteDismissed(7L))

            assertIs<HomeEvent.DeleteFailed>(awaitItem())
            assertEquals(listOf(7L), repository.deletedIds)
            assertEquals(listOf(coffee), viewModel.state.value.coffeeList)
        }
    }

    @Test
    fun `new swipe confirms previous deletion and only latest can be undone`() = runTest {
        val first = coffeeFixture(id = 1L, name = "Yellow Bourbon")
        val second = coffeeFixture(id = 2L, name = "Geisha")
        val repository = FakeCoffeeRepository(listOf(first, second))
        val viewModel = createViewModel(repository)

        viewModel.events.test {
            viewModel.onAction(HomeAction.OnCoffeeSwipedToRemove(first))
            awaitItem()
            viewModel.onAction(HomeAction.OnCoffeeSwipedToRemove(second))
            assertEquals(2L, assertIs<HomeEvent.ShowUndoDelete>(awaitItem()).coffeeId)

            assertEquals(listOf(1L), repository.deletedIds)
            assertEquals(emptyList(), viewModel.state.value.coffeeList)

            // Stale results from the replaced snackbar are ignored.
            viewModel.onAction(HomeAction.OnUndoDeleteDismissed(1L))
            viewModel.onAction(HomeAction.OnUndoDeleteClick(1L))
            assertEquals(listOf(1L), repository.deletedIds)
            assertEquals(emptyList(), viewModel.state.value.coffeeList)

            viewModel.onAction(HomeAction.OnUndoDeleteClick(2L))
            assertEquals(listOf(second), viewModel.state.value.coffeeList)
            assertEquals(listOf(1L), repository.deletedIds)
        }
    }

    @Test
    fun `repeated swipe of the pending coffee is ignored`() = runTest {
        val coffee = coffeeFixture(id = 7L)
        val repository = FakeCoffeeRepository(listOf(coffee))
        val viewModel = createViewModel(repository)

        viewModel.events.test {
            viewModel.onAction(HomeAction.OnCoffeeSwipedToRemove(coffee))
            awaitItem()

            viewModel.onAction(HomeAction.OnCoffeeSwipedToRemove(coffee))
            expectNoEvents()

            viewModel.onAction(HomeAction.OnUndoDeleteClick(7L))
            assertEquals(listOf(coffee), viewModel.state.value.coffeeList)
            assertTrue(repository.deletedIds.isEmpty())
        }
    }

    @Test
    fun `swipe ignores coffees without id`() = runTest {
        val repository = FakeCoffeeRepository()
        val viewModel = createViewModel(repository)

        viewModel.events.test {
            viewModel.onAction(HomeAction.OnCoffeeSwipedToRemove(coffeeFixture(id = null)))

            expectNoEvents()
            assertTrue(repository.deletedIds.isEmpty())
        }
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

    // Keeps the WhileSubscribed state active so tests can read state.value.
    private fun TestScope.createViewModel(repository: FakeCoffeeRepository): HomeViewModel {
        val viewModel = HomeViewModel(repository, FakeCoffeeXpLogger())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }
        return viewModel
    }
}
