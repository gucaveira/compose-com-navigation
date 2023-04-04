package br.com.alura.panucci.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun PanucciNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Highlight.route
    ) {
        highlightsListScreen(navController)
        menuListScreen(navController)
        drinksListScreen(navController)
        productDetailsScreen(navController)
        checkoutScreen(navController)
        authenticationScreen(navController)
    }
}

