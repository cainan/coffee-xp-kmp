package com.cso.coffeexp.presentation.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme

@Composable
fun DetailsRoot(
    viewModel: DetailsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DetailsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun DetailsScreen(
    state: DetailsState,
    onAction: (DetailsAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    CoffeeXpTheme {
        DetailsScreen(
            state = DetailsState(),
            onAction = {}
        )
    }
}