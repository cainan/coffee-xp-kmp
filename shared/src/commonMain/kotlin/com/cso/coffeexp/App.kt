package com.cso.coffeexp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme
import com.cso.coffeexp.presentation.navigation.NavigationRoot
import com.cso.coffeexp.presentation.navigation.UIRoute

@Composable
@Preview
fun App() {

    val navController = rememberNavController()

    CoffeeXpTheme {
        NavigationRoot(
            navController = navController,
            startDestination = UIRoute.Home
        )
    }
}