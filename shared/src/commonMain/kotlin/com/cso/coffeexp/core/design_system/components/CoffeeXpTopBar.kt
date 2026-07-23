package com.cso.coffeexp.core.design_system.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeXpTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    backContentDescription: String? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = backContentDescription
                )
            }
        },
        actions = actions
    )
}

@Preview
@Composable
private fun CoffeeXpTopBarPreview() {
    CoffeeXpTheme {
        CoffeeXpTopBar(
            title = "Coffee Xp",
            onBackClick = {},
            backContentDescription = "Back"
        )
    }
}
