package com.cso.coffeexp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cso.coffeexp.core.design_system.components.CoffeeXpSearchBar
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme
import com.cso.coffeexp.presentation.components.SwipeToDeleteCoffeeBox

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {

    // Manage query state
    var query by rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CoffeeXpSearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { /* Handle search submission */
//                    onEvent(HomeEvent.OnSearch(query))
                },
                placeholder = { Text("Search coffees") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More options"
                    )
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
//                onClickonNavigateToDetails(null)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add new coffee")
            }
        }
    ) { innerPadding ->
        if (state.coffeeList.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No coffees yet. Add one with the + button!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = state.coffeeList, key = { it.id ?: 0 }) { coffee ->
                    SwipeToDeleteCoffeeBox(
                        coffee = coffee,
                        onToggleDone = {
//                            Toast.makeText(context, "onToggleDone", Toast.LENGTH_SHORT).show()
                        },
                        onRemove = {
//                            onEvent(HomeEvent.OnRemoveCoffee(it))
                        },
                        onClick = { coffee ->
//                            onNavigateToDetails(coffee.id)
                        },
                        modifier = Modifier.animateItem()
                    )
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
            onAction = {}
        )
    }
}