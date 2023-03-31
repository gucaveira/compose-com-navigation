package br.com.alura.panucci

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.alura.panucci.navigation.AppDestination
import br.com.alura.panucci.navigation.bottomAppBarItems
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.components.BottomAppBarItem
import br.com.alura.panucci.ui.components.PanucciBottomAppBar
import br.com.alura.panucci.ui.screens.*
import br.com.alura.panucci.ui.theme.PanucciTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val backStackEntryState by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntryState?.destination

            //Use o LauccheDEffect para logica que não se aplica a click ou eventos
            // Pq o compose executa a criacao de tela com muita frequencia e se essa logica não for
            // tratada entrarar em loop de navegacao
            /*LaunchedEffect(key1 = Unit){
                navController.navigate("menu")
            }*/

            PanucciTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val selectedItem by remember(currentDestination) {
                        val item = currentDestination?.let { destination ->
                            bottomAppBarItems.find {
                                it.destination.route == destination.route
                            }
                        } ?: bottomAppBarItems.first()
                        mutableStateOf(item)
                    }
                    PanucciApp(
                        bottomAppBarItemSelected = selectedItem,
                        onBottomAppBarItemSelectedChange = {
                            val route = it.destination.route
                            navController.navigate(route) {
                                launchSingleTop = true
                                popUpTo(route)
                            }
                        },
                        onFabClick = { navController.navigate(AppDestination.Checkout.route) }) {
                        NavHost(
                            navController = navController,
                            startDestination = AppDestination.Highlight.route
                        ) {
                            composable(AppDestination.Highlight.route) {
                                HighlightsListScreen(
                                    products = sampleProducts,
                                    onNavigateToDetails = {
                                        navController.navigate(AppDestination.ProductDetails.route)
                                    }, onNavigateToCheckout = {
                                        navController.navigate(AppDestination.Checkout.route)
                                    })
                            }
                            composable(AppDestination.Menu.route) {
                                MenuListScreen(products = sampleProducts) {
                                    navController.navigate(AppDestination.ProductDetails.route)
                                }
                            }
                            composable(AppDestination.Drinks.route) {
                                DrinksListScreen(products = sampleProducts) {
                                    navController.navigate(AppDestination.ProductDetails.route)
                                }
                            }
                            composable(AppDestination.ProductDetails.route) {
                                ProductDetailsScreen(product = sampleProducts.random()) {
                                    navController.navigate(AppDestination.Checkout.route)
                                }
                            }
                            composable(AppDestination.Checkout.route) {
                                CheckoutScreen(products = sampleProducts)
                            }
                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanucciApp(
    bottomAppBarItemSelected: BottomAppBarItem = bottomAppBarItems.first(),
    onBottomAppBarItemSelectedChange: (BottomAppBarItem) -> Unit = {},
    onFabClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Ristorante Panucci")
                },
            )
        },
        bottomBar = {
            PanucciBottomAppBar(
                item = bottomAppBarItemSelected,
                items = bottomAppBarItems,
                onItemChange = onBottomAppBarItemSelectedChange,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick
            ) {
                Icon(
                    Icons.Filled.PointOfSale,
                    contentDescription = null
                )
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun PanucciAppPreview() {
    PanucciTheme {
        Surface {
            PanucciApp {}
        }
    }
}