package br.com.alura.panucci.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.alura.panucci.navigation.graph.homeGraph
import br.com.alura.panucci.navigation.graph.homeGraphRoute

@Composable
fun PanucciNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = homeGraphRoute
    ) {
        homeGraph(navController)
        productDetailsScreen(navController)
        checkoutScreen(navController)
        authenticationScreen(navController)
    }
}