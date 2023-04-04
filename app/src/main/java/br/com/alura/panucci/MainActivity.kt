package br.com.alura.panucci

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.alura.panucci.navigation.PanucciNavHost
import br.com.alura.panucci.navigation.bottomAppBarItems
import br.com.alura.panucci.navigation.drinksRoute
import br.com.alura.panucci.navigation.highlightsListRoute
import br.com.alura.panucci.navigation.menuRoute
import br.com.alura.panucci.navigation.navigateToAuthentication
import br.com.alura.panucci.navigation.navigateToCheckout
import br.com.alura.panucci.ui.components.BottomAppBarItem
import br.com.alura.panucci.ui.components.PanucciBottomAppBar
import br.com.alura.panucci.ui.screens.*
import br.com.alura.panucci.ui.theme.PanucciTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()
            val backStackEntryState by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntryState?.destination

            //Use o LauccheDEffect para logica que não se aplica a click ou eventos
            // Pq o compose executa a criacao de tela com muita frequencia e se essa logica não for
            // tratada entrarar em loop de navegacao
            /*LaunchedEffect(key1 = Unit){
                navController.navigate("menu")
            }*/

            // Considerando que é uma ação que pode ser afetada pela recomposição,
            // precisamos realizar a navegação utilizando a API de Effect
            // ou a partir de um evento de um composable que utilizar a API de Effect internamente.

            PanucciTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val currentRoute = currentDestination?.route
                    val selectedItem by remember(currentDestination) {
                        val item = when (currentRoute) {
                            highlightsListRoute -> BottomAppBarItem.HighlightsList
                            menuRoute -> BottomAppBarItem.Menu
                            drinksRoute -> BottomAppBarItem.Drinks
                            else -> BottomAppBarItem.HighlightsList
                        }
                        mutableStateOf(item)
                    }

                    val containsInBottomAppBarItems = when (currentRoute) {
                        highlightsListRoute, menuRoute, drinksRoute -> true
                        else -> false
                    }

                    val isShowFab = when (currentRoute) {
                        menuRoute, drinksRoute -> true
                        else -> false
                    }

                    PanucciApp(
                        bottomAppBarItemSelected = selectedItem,
                        onBottomAppBarItemSelectedChange = {
                            val route = it.destination
                            navController.navigate(route) {
                                launchSingleTop = true
                                popUpTo(route)
                            }
                        },
                        onFabClick = { navController.navigateToCheckout() },
                        isShowTopBar = containsInBottomAppBarItems,
                        isShowBottomBar = containsInBottomAppBarItems,
                        isShowFab = isShowFab,
                        onLogout = {
                            scope.launch {
                                context.dataStore.edit {
                                    it.remove(userPreferences)
                                }
                            }

                            navController.navigateToAuthentication {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        PanucciNavHost(navController = navController)
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
    onLogout: () -> Unit = {},
    isShowTopBar: Boolean = false,
    isShowBottomBar: Boolean = false,
    isShowFab: Boolean = false,
    content: @Composable () -> Unit,
) {
    Scaffold(topBar = {
        if (isShowTopBar) {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Ristorante Panucci")
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.Filled.ExitToApp,
                            contentDescription = "sair do app"
                        )
                    }
                }
            )
        }
    }, bottomBar = {
        if (isShowBottomBar) {
            PanucciBottomAppBar(
                item = bottomAppBarItemSelected,
                items = bottomAppBarItems,
                onItemChange = onBottomAppBarItemSelectedChange,
            )
        }
    }, floatingActionButton = {
        if (isShowFab) {
            FloatingActionButton(
                onClick = onFabClick
            ) {
                Icon(
                    Icons.Filled.PointOfSale, contentDescription = null
                )
            }
        }
    }) {
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