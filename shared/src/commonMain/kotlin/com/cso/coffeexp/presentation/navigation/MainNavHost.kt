//package com.cso.coffeexp.presentation.navigation
//
//import android.util.Log
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.cso.coffeexp.ui.screen.details.DetailsScreen
//import com.cso.coffeexp.ui.screen.details.DetailsViewModel
//import com.cso.coffeexp.ui.screen.home.HomeScreen
//import com.cso.coffeexp.ui.screen.home.HomeViewModel
//import com.cso.coffeexp.ui.theme.CoffeeXpTheme
//import org.koin.androidx.compose.koinViewModel
//
//private const val TAG = "MainNavHost"
//
//@Composable
//fun MainNavHost(modifier: Modifier = Modifier) {
//    val navController = rememberNavController()
//
//    val homeViewModel = koinViewModel<HomeViewModel>()
//    val homeUIState by homeViewModel.uiState.collectAsStateWithLifecycle()
//
//    val detailsViewModel = koinViewModel<DetailsViewModel>()
//    val detailsUIState by detailsViewModel.uiState.collectAsStateWithLifecycle()
//
//    NavHost(modifier = modifier, navController = navController, startDestination = UIRoute.Home) {
//
//        composable<UIRoute.Home> {
//            HomeScreen(
//                modifier = Modifier,
//                uiState = homeUIState,
//                onEvent = homeViewModel::onEvent,
//                onNavigateToDetails = { coffeeId ->
//                    Log.d(TAG, "Navigate from HomeScreen to DetailsScreen - CoffeeId: $coffeeId")
//                    navController.navigate(UIRoute.Details(coffeeId))
//                }
//            )
//        }
//
//        composable<UIRoute.Details> { navBackStackEntry ->
//            val coffeeId = navBackStackEntry.arguments?.getLong(UIArgument.COFFEE_ID.key)
//            Log.d(TAG, "Open DetailsScreen - CoffeeId: $coffeeId")
//            DetailsScreen(
//                modifier = Modifier,
//                uiState = detailsUIState,
//                coffeeId = coffeeId,
//                onEvent = detailsViewModel::onEvent,
//                onBackPressed = { navController.popBackStack() },
//            )
//
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MainNavHostPreview() {
//    CoffeeXpTheme {
//        MainNavHost()
//    }
//}