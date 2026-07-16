package com.cso.coffeexp.presentation.new_coffee

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NewCoffeeRoot(
    viewModel: NewCoffeeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NewCoffeeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun NewCoffeeScreen(
    state: NewCoffeeState,
    onAction: (NewCoffeeAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    CoffeeXpTheme {
        NewCoffeeScreen(
            state = NewCoffeeState(),
            onAction = {}
        )
    }
}