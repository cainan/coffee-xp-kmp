package com.cso.coffeexp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
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
                    navController.navigate(UIRoute.NewCoffee(null))
                },
                onDetailsClick = { coffeeId ->
                    navController.navigate(UIRoute.Details(coffeeId))
                }
            )
        }

        composable<UIRoute.NewCoffee> { backStackEntry ->
            val newCoffee: UIRoute.NewCoffee = backStackEntry.toRoute()
            val coffeeToEdit = newCoffee.coffeeId
            NewCoffeeRoot(
                coffeeToEdit = coffeeToEdit,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable<UIRoute.Details> { backStackEntry ->
            val details: UIRoute.Details = backStackEntry.toRoute()
            val coffeeId = details.coffeeId
            DetailsRoot(
                coffeeId = coffeeId,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = { coffeeId ->
                    navController.navigate(UIRoute.NewCoffee(coffeeId))
                }
            )
        }
    }
}