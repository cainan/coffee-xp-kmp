package com.cso.coffeexp.presentation.new_coffee

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshots.Snapshot
import app.cash.turbine.test
import com.cso.coffeexp.core.error_handling.DataError
import com.cso.coffeexp.core.error_handling.Result
import com.cso.coffeexp.core.design_system.utils.UiText
import com.cso.coffeexp.core.utils.LocalDate
import com.cso.coffeexp.testutil.FakeCoffeeRepository
import com.cso.coffeexp.testutil.FakeCoffeeXpLogger
import com.cso.coffeexp.testutil.coffeeFixture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.CompletableDeferred
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
import coffeexp.shared.generated.resources.Res
import coffeexp.shared.generated.resources.error_unknown

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
            assertEquals(coffee.roastDate, state.roastDate)
            assertEquals(coffee.roastLevel, state.roastLevelState.text.toString())
            assertEquals(coffee.brewingMethod, state.brewingMethod.text.toString())
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
    fun `failure loading coffee to edit shows an error and keeps form empty`() = runTest {
        val repository = FakeCoffeeRepository().apply {
            getCoffeeByIdResult = Result.Failure(DataError.Local.UNKNOWN)
        }
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), repository)

        viewModel.state.test {
            assertNull(awaitItem().coffeeId)

            viewModel.onAction(NewCoffeeAction.OnCoffeeToEditSelected(404L))

            val failedState = awaitItem()
            assertNull(failedState.coffeeId)
            assertTrue(failedState.coffeeNameState.text.isEmpty())
            val errorMessage = failedState.errorMessage as UiText.Resource
            assertEquals(Res.string.error_unknown, errorMessage.id)
            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(listOf(404L), repository.requestedIds)
    }

    @Test
    fun `save starts disabled`() = runTest {
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), FakeCoffeeRepository())

        viewModel.state.test {
            assertFalse(awaitItem().isSaveEnabled)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `save becomes enabled when all required fields are filled`() = runTest {
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), FakeCoffeeRepository())

        viewModel.state.test {
            val state = awaitItem()

            state.coffeeNameState.replaceText("Geisha")
            state.roasterState.replaceText("Coffee Lab")
            state.originState.replaceText("Panama")
            state.roastLevelState.replaceText("Light")
            state.brewingMethod.replaceText("V60")
            Snapshot.sendApplyNotifications()

            assertTrue(awaitItem().isSaveEnabled)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `save becomes disabled when a required field is cleared`() = runTest {
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), FakeCoffeeRepository())

        viewModel.state.test {
            val state = awaitItem()

            state.coffeeNameState.replaceText("Geisha")
            state.roasterState.replaceText("Coffee Lab")
            state.originState.replaceText("Panama")
            state.roastLevelState.replaceText("Light")
            state.brewingMethod.replaceText("V60")
            Snapshot.sendApplyNotifications()
            assertTrue(awaitItem().isSaveEnabled)

            state.originState.replaceText("   ")
            Snapshot.sendApplyNotifications()

            assertFalse(awaitItem().isSaveEnabled)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `selecting existing coffee enables save when required fields are filled`() = runTest {
        val coffee = coffeeFixture(id = 22L)
        val repository = FakeCoffeeRepository().apply { coffeeById = coffee }
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), repository)

        viewModel.state.test {
            assertFalse(awaitItem().isSaveEnabled)

            viewModel.onAction(NewCoffeeAction.OnCoffeeToEditSelected(22L))
            Snapshot.sendApplyNotifications()

            var editedState = awaitItem()
            while (editedState.coffeeId != coffee.id || !editedState.isSaveEnabled) {
                editedState = awaitItem()
            }

            assertTrue(editedState.isSaveEnabled)
            cancelAndIgnoreRemainingEvents()
        }
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
            viewModel.events.test {
                viewModel.onAction(NewCoffeeAction.OnSaveClick)
                assertEquals(NewCoffeeEvent.AddedSuccessfully, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
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
            viewModel.events.test {
                viewModel.onAction(NewCoffeeAction.OnSaveClick)
                assertEquals(NewCoffeeEvent.AddedSuccessfully, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(41L, repository.upsertedCoffees.single().id)
    }

    @Test
    fun `selecting roast date updates state and persists selection`() = runTest {
        val selectedDate = kotlinx.datetime.LocalDate(2026, 8, 1)
        val repository = FakeCoffeeRepository()
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), repository)

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(NewCoffeeAction.OnRoastDateSelected(selectedDate))
            assertEquals(selectedDate, awaitItem().roastDate)

            viewModel.events.test {
                viewModel.onAction(NewCoffeeAction.OnSaveClick)
                assertEquals(NewCoffeeEvent.AddedSuccessfully, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(selectedDate, repository.upsertedCoffees.single().roastDate)
    }

    @Test
    fun `saving an edited coffee preserves its original created date`() = runTest {
        val original = coffeeFixture(id = 51L)
        val repository = FakeCoffeeRepository().apply { coffeeById = original }
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), repository)

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(NewCoffeeAction.OnCoffeeToEditSelected(51L))
            awaitItem()
            viewModel.events.test {
                viewModel.onAction(NewCoffeeAction.OnSaveClick)
                assertEquals(NewCoffeeEvent.AddedSuccessfully, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(original.createdAt, repository.upsertedCoffees.single().createdAt)
    }

    @Test
    fun `saving an edited coffee updates its last modified date`() = runTest {
        val original = coffeeFixture(id = 52L)
        val repository = FakeCoffeeRepository().apply { coffeeById = original }
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), repository)

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(NewCoffeeAction.OnCoffeeToEditSelected(52L))
            awaitItem()
            viewModel.events.test {
                viewModel.onAction(NewCoffeeAction.OnSaveClick)
                assertEquals(NewCoffeeEvent.AddedSuccessfully, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(LocalDate(), repository.upsertedCoffees.single().lastModifiedAt)
    }

    @Test
    fun `saving an edited coffee persists every changed field`() = runTest {
        val original = coffeeFixture(id = 51L)
        val repository = FakeCoffeeRepository().apply { coffeeById = original }
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), repository)

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(NewCoffeeAction.OnCoffeeToEditSelected(51L))
            val state = awaitItem()

            state.coffeeNameState.replaceText("Geisha")
            state.roasterState.replaceText("New Roaster")
            state.seriesCollectionState.replaceText("Competition Lot")
            state.originState.replaceText("Panama")
            state.processState.replaceText("Washed")
            state.elevationState.replaceText("1,800m")
            val changedRoastDate = kotlinx.datetime.LocalDate(2026, 8, 1)
            viewModel.onAction(NewCoffeeAction.OnRoastDateSelected(changedRoastDate))
            awaitItem()
            state.roastLevelState.replaceText("Light")
            state.grindSizeState.replaceText("Fine")
            state.temperatureState.replaceText("96C")
            state.ratioState.replaceText("1:15")
            state.brewDuration.replaceText("02:45")
            state.tastingNotesState.replaceText("Jasmine and bergamot")
            val changedPhotoUri = "file:///coffee/geisha.jpg"
            viewModel.onAction(NewCoffeeAction.OnPhotoSelected(changedPhotoUri))
            state.brewingMethod.replaceText("Chemex")
            awaitItem()
            viewModel.onAction(NewCoffeeAction.OnRatingChange(9.5))
            awaitItem()
            viewModel.events.test {
                viewModel.onAction(NewCoffeeAction.OnSaveClick)
                assertEquals(NewCoffeeEvent.AddedSuccessfully, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            cancelAndIgnoreRemainingEvents()

            val saved = repository.upsertedCoffees.single()
            assertEquals(original.id, saved.id)
            assertEquals(changedPhotoUri, saved.imageUrl)
            assertEquals("Geisha", saved.name)
            assertEquals("New Roaster", saved.roaster)
            assertEquals("Competition Lot", saved.series)
            assertEquals("Panama", saved.origin)
            assertEquals("Washed", saved.process)
            assertEquals("1,800m", saved.elevation)
            assertEquals(changedRoastDate, saved.roastDate)
            assertEquals("Light", saved.roastLevel)
            assertEquals("Chemex", saved.brewingMethod)
            assertEquals("Fine", saved.grindSize)
            assertEquals("96C", saved.temperature)
            assertEquals("1:15", saved.ratio)
            assertEquals("02:45", saved.brewTime)
            assertEquals(9.5, saved.rating)
            assertEquals("Jasmine and bergamot", saved.notes)
            assertEquals(original.createdAt, saved.createdAt)
            assertEquals(LocalDate(), saved.lastModifiedAt)
        }
    }

    @Test
    fun `failed save shows error and stops loading without emitting success event`() = runTest {
        val repository = FakeCoffeeRepository().apply {
            upsertResult = Result.Failure(DataError.Local.UNKNOWN)
        }
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), repository)

        viewModel.state.test {
            awaitItem()

            viewModel.events.test {
                viewModel.onAction(NewCoffeeAction.OnSaveClick)
                expectNoEvents()
                cancelAndIgnoreRemainingEvents()
            }

            val failedState = awaitItem()
            assertFalse(failedState.isSaving)
            val errorMessage = failedState.errorMessage as UiText.Resource
            assertEquals(Res.string.error_unknown, errorMessage.id)
            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(1, repository.upsertedCoffees.size)
    }

    @Test
    fun `new save attempt clears previous error while repository is working`() = runTest {
        val repository = FakeCoffeeRepository().apply {
            upsertResult = Result.Failure(DataError.Local.UNKNOWN)
        }
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), repository)

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(NewCoffeeAction.OnSaveClick)
            assertTrue(awaitItem().errorMessage != null)

            repository.upsertBarrier = CompletableDeferred()
            viewModel.onAction(NewCoffeeAction.OnSaveClick)

            val savingState = awaitItem()
            assertTrue(savingState.isSaving)
            assertNull(savingState.errorMessage)

            repository.upsertBarrier?.complete(Unit)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `successful retry leaves no error and emits success event`() = runTest {
        val repository = FakeCoffeeRepository().apply {
            upsertResult = Result.Failure(DataError.Local.UNKNOWN)
        }
        val viewModel = NewCoffeeViewModel(FakeCoffeeXpLogger(), repository)

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(NewCoffeeAction.OnSaveClick)
            assertTrue(awaitItem().errorMessage != null)

            repository.upsertResult = Result.Success(1L)
            viewModel.events.test {
                viewModel.onAction(NewCoffeeAction.OnSaveClick)
                assertEquals(NewCoffeeEvent.AddedSuccessfully, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            var successfulState = awaitItem()
            while (successfulState.isSaving) {
                successfulState = awaitItem()
            }
            assertNull(successfulState.errorMessage)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun TextFieldState.replaceText(value: String) {
        edit { replace(0, length, value) }
    }
}
