package br.com.alura.panucci.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import br.com.alura.panucci.dataStore
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.screens.HighlightsListScreen
import br.com.alura.panucci.userPreferences
import kotlinx.coroutines.flow.first

internal const val highlightsListRoute = "highlight"

fun NavGraphBuilder.highlightsListScreen(
    navController: NavHostController,
) {
    composable(highlightsListRoute) {

        val context = LocalContext.current

        var user: String? by remember {
            mutableStateOf(null)
        }

        var dataState by remember {
            mutableStateOf("loading")
        }

        LaunchedEffect(null) {
            user = context.dataStore.data.first()[userPreferences]
            dataState = "finished"
        }

        when (dataState) {
            "loading" -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Carregando...",
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }
            }
            "finished" -> {
                user?.let {
                    HighlightsListScreen(products = sampleProducts,
                        onNavigateToDetails = { product ->
                            navController.navigateToProductDetails(product.id)
                        },
                        onNavigateToCheckout = {
                            navController.navigateToCheckout()
                        })
                } ?: LaunchedEffect(key1 = null) {
                    navController.navigateToAuthentication {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}

fun NavController.navigateToHighlightsList(route: String, builder: NavOptionsBuilder.() -> Unit) {
    navigate(route, navOptions(builder))
}