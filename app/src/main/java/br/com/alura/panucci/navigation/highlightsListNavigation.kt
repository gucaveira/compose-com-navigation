package br.com.alura.panucci.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.alura.panucci.dataStore
import br.com.alura.panucci.ui.screens.HighlightsListScreen
import br.com.alura.panucci.ui.viewmodels.HighlightsListViewModel
import br.com.alura.panucci.userPreferences
import kotlinx.coroutines.flow.first

internal const val highlightsListRoute = "highlight"

fun NavGraphBuilder.highlightsListScreen(
    navController: NavHostController,
) {
    composable(highlightsListRoute) {

        val viewModel = viewModel<HighlightsListViewModel>()
        val uiState by viewModel.uiState.collectAsState()

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

     /*   when (dataState) {
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
            "loading" -> {
                user?.let {*/
                    HighlightsListScreen(uiState = uiState,
                        onNavigateToDetails = { product ->
                            navController.navigateToProductDetails(product.id)
                        },
                        onNavigateToCheckout = {
                            navController.navigateToCheckout()
                        })
                }/* ?: LaunchedEffect(key1 = null) {
                    navController.navigateToAuthentication {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }*/
}

/*fun NavController.navigateToHighlightsList(builder: NavOptionsBuilder.() -> Unit) {
    navigate(highlightsListRoute, navOptions(builder))
}*/

fun NavController.navigateToHighlightsList(navOptions: NavOptions? = null) {
    navigate(highlightsListRoute, navOptions)
}