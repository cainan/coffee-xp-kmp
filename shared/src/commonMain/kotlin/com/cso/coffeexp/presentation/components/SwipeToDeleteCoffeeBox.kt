package com.cso.coffeexp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import coffeexp.shared.generated.resources.Res
import coffeexp.shared.generated.resources.cd_remove_item
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme
import com.cso.coffeexp.domain.mock.mockCoffee
import com.cso.coffeexp.domain.model.Coffee
import org.jetbrains.compose.resources.stringResource

@Composable
fun SwipeToDeleteCoffeeBox(
    coffee: Coffee,
    onToggleDone: (Coffee) -> Unit,
    onRemove: (Coffee) -> Unit,
    onClick: (Coffee) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Plain remember instead of rememberSwipeToDismissBoxState (rememberSaveable): LazyColumn
    // restores saveable state by item key, so an item that comes back (undo / failed delete)
    // would be restored already dismissed and fire onDismiss again.
    val positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold
    val swipeToDismissBoxState = remember {
        SwipeToDismissBoxState(SwipeToDismissBoxValue.Settled, positionalThreshold)
    }

    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        modifier = modifier.fillMaxSize(),
        onDismiss = {
            when (it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onRemove(coffee)
                }

                else -> Unit
            }
        },
        backgroundContent = {
            when (swipeToDismissBoxState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> {}

                SwipeToDismissBoxValue.EndToStart -> {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(Res.string.cd_remove_item),
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(CoffeeXpTheme.spacing.stackSm),
                        tint = Color.White
                    )
                }

                SwipeToDismissBoxValue.Settled -> {}
            }
        }
    ) {
        CoffeeCard(coffee = coffee, onClick = { coffee ->
            onClick(coffee)
        })
    }
}

@Preview
@Composable
fun SwipeToDeleteCoffeePreview() {
    CoffeeXpTheme {
        SwipeToDeleteCoffeeBox(
            coffee = mockCoffee,
            onToggleDone = {},
            onRemove = {},
            onClick = {}
        )
    }
}