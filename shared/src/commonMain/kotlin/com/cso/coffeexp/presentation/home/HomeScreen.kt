package com.cso.coffeexp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coffeexp.shared.generated.resources.Res
import coffeexp.shared.generated.resources.app_name
import coffeexp.shared.generated.resources.cd_add_new_coffee
import coffeexp.shared.generated.resources.cd_more_options
import coffeexp.shared.generated.resources.cd_search
import coffeexp.shared.generated.resources.home_empty_state
import coffeexp.shared.generated.resources.home_my_brews
import coffeexp.shared.generated.resources.home_search_placeholder
import coffeexp.shared.generated.resources.home_undo
import com.cso.coffeexp.core.design_system.components.CoffeeXpSearchBar
import com.cso.coffeexp.core.design_system.components.CoffeeXpTopBar
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme
import com.cso.coffeexp.core.design_system.utils.ObserveAsEvents
import com.cso.coffeexp.domain.logger.CoffeeXpLogger
import com.cso.coffeexp.presentation.components.SwipeToDeleteCoffeeBox
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onNewCoffeeClick: () -> Unit,
    onDetailsClick: (Long) -> Unit,
) {
    val logger: CoffeeXpLogger = koinInject()

    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarScope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HomeEvent.ShowUndoDelete -> {
                val message = event.message.asStringAsync()
                val actionLabel = getString(Res.string.home_undo)
                // Dismissing the previous undo snackbar makes it report its own dismissal.
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarScope.launch {
                    val result = try {
                        snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = actionLabel,
                            duration = SnackbarDuration.Short,
                        )
                    } catch (e: CancellationException) {
                        // Leaving the screen confirms the deletion.
                        viewModel.onAction(HomeAction.OnUndoDeleteDismissed(event.coffeeId))
                        throw e
                    }
                    viewModel.onAction(
                        when (result) {
                            SnackbarResult.ActionPerformed -> HomeAction.OnUndoDeleteClick(event.coffeeId)
                            SnackbarResult.Dismissed -> HomeAction.OnUndoDeleteDismissed(event.coffeeId)
                        }
                    )
                }
            }

            is HomeEvent.DeleteFailed -> {
                val message = event.message.asStringAsync()
                snackbarScope.launch {
                    snackbarHostState.showSnackbar(message = message)
                }
            }
        }
    }

    HomeScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = { action ->
            logger.debug("Action received: $action")
            when (action) {
                is HomeAction.OnNewCoffeeClick -> onNewCoffeeClick()
                is HomeAction.OnDetailsClick -> onDetailsClick(action.coffeeId)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    snackbarHostState: SnackbarHostState,
    onAction: (HomeAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CoffeeXpTopBar(
                title = stringResource(Res.string.app_name)
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onAction(HomeAction.OnNewCoffeeClick)
            }) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(Res.string.cd_add_new_coffee)
                )
            }
        }
    ) { innerPadding ->
        val listState = rememberLazyListState()
        // Ids are auto-incremented, so only a newly created coffee exceeds the newest id seen.
        // Restored items (undo / failed delete) or clearing the search don't scroll the list.
        var newestSeenCoffeeId by rememberSaveable { mutableStateOf<Long?>(null) }
        val newestCoffeeId = state.coffeeList?.mapNotNull { it.id }?.maxOrNull()

        LaunchedEffect(newestCoffeeId) {
            if (newestCoffeeId == null) return@LaunchedEffect
            val previous = newestSeenCoffeeId
            if (previous == null || newestCoffeeId > previous) {
                if (previous != null) listState.animateScrollToItem(0)
                newestSeenCoffeeId = newestCoffeeId
            }
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = CoffeeXpTheme.spacing.marginMobile)
        ) {
            Text(
                text = stringResource(Res.string.home_my_brews),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = CoffeeXpTheme.spacing.base)
            )

            CoffeeXpSearchBar(
                query = state.searchQuery,
                onQueryChange = { onAction(HomeAction.OnSearch(it)) },
                onSearch = { onAction(HomeAction.OnSearch(it)) },
                placeholder = { Text(stringResource(Res.string.home_search_placeholder)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(Res.string.cd_search)
                    )
                },
                trailingIcon = {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = stringResource(Res.string.cd_more_options)
                    )
                },
                modifier = Modifier.padding(top = CoffeeXpTheme.spacing.stackSm)
            )

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.coffeeList.isNullOrEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(Res.string.home_empty_state))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = CoffeeXpTheme.spacing.gutter),
                    verticalArrangement = Arrangement.spacedBy(CoffeeXpTheme.spacing.gutter),
                    state = listState
                ) {
                    items(items = state.coffeeList, key = { it.id ?: 0 }) { coffee ->
                        SwipeToDeleteCoffeeBox(
                            coffee = coffee,
                            onToggleDone = {},
                            onRemove = { onAction(HomeAction.OnCoffeeSwipedToRemove(it)) },
                            onClick = { clicked ->
                                clicked.id?.let { coffeeId ->
                                    onAction(HomeAction.OnDetailsClick(coffeeId))
                                }
                            },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CoffeeXpTheme {
        HomeScreen(
            state = HomeState(),
            snackbarHostState = SnackbarHostState(),
            onAction = {},
        )
    }
}
