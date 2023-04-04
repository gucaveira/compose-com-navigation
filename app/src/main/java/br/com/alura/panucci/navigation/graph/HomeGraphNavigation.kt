package br.com.alura.panucci.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import androidx.navigation.navigation
import br.com.alura.panucci.navigation.drinksListScreen
import br.com.alura.panucci.navigation.drinksRoute
import br.com.alura.panucci.navigation.highlightsListRoute
import br.com.alura.panucci.navigation.highlightsListScreen
import br.com.alura.panucci.navigation.menuListScreen
import br.com.alura.panucci.navigation.menuRoute
import br.com.alura.panucci.navigation.navigateToDrinks
import br.com.alura.panucci.navigation.navigateToHighlightsList
import br.com.alura.panucci.navigation.navigateToMenu
import br.com.alura.panucci.ui.components.BottomAppBarItem

internal const val homeGraphRoute = "home"

fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    navigation(startDestination = highlightsListRoute, route = homeGraphRoute) {
        highlightsListScreen(navController)
        menuListScreen(navController)
        drinksListScreen(navController)
    }
}

fun NavController.navigateToHomeGraph() {
    navigate(homeGraphRoute)
}

fun NavController.navigateSingleTopWithPopUpTo(item: BottomAppBarItem) {
    val (route, navigate) = when (item) {
        BottomAppBarItem.HighlightsList -> Pair(highlightsListRoute, ::navigateToHighlightsList)
        BottomAppBarItem.Drinks -> Pair(drinksRoute, ::navigateToDrinks)
        BottomAppBarItem.Menu -> Pair(menuRoute, ::navigateToMenu)
    }

    val navOptions = navOptions {
        launchSingleTop = true
        popUpTo(route)
    }

    navigate(navOptions)
}