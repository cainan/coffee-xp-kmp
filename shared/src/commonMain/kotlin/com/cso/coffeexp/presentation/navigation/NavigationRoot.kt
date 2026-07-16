package com.cso.coffeexp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cso.coffeexp.presentation.details.DetailsRoot
import com.cso.coffeexp.presentation.home.HomeRoot
import com.cso.coffeexp.presentation.new_coffee.NewCoffeeRoot

@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: Any
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable<UIRoute.Home> {
            HomeRoot(
                onNewCoffeeClick = {
                    navController.navigate(UIRoute.NewCoffee)
                },
                onDetailsClick = { coffeeId ->
                    navController.navigate(UIRoute.Details(coffeeId))
                }
            )
        }

        composable<UIRoute.NewCoffee> {
            NewCoffeeRoot()
        }

        composable<UIRoute.Details> {
            DetailsRoot()
        }
    }
}