package br.com.alura.panucci

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.alura.panucci.navigation.PanucciNavHost
import br.com.alura.panucci.navigation.drinksRoute
import br.com.alura.panucci.navigation.graph.navigateSingleTopWithPopUpTo
import br.com.alura.panucci.navigation.highlightsListRoute
import br.com.alura.panucci.navigation.menuRoute
import br.com.alura.panucci.navigation.navigateToAuthentication
import br.com.alura.panucci.navigation.navigateToCheckout
import br.com.alura.panucci.ui.components.BottomAppBarItem.Drinks
import br.com.alura.panucci.ui.components.BottomAppBarItem.HighlightsList
import br.com.alura.panucci.ui.components.BottomAppBarItem.Menu
import br.com.alura.panucci.ui.screens.PanucciAppScreen
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

            val orderDoneMessage =
                backStackEntryState?.savedStateHandle
                    ?.getStateFlow<String?>("order_done", null)
                    ?.collectAsState()

            val snackbarHostState = remember {
                SnackbarHostState()
            }

            orderDoneMessage?.value?.let { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(message = message)
                }
            }

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
                            highlightsListRoute -> HighlightsList
                            menuRoute -> Menu
                            drinksRoute -> Drinks
                            else -> HighlightsList
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

                    PanucciAppScreen(
                        snackbarHostState = snackbarHostState,
                        bottomAppBarItemSelected = selectedItem,
                        onBottomAppBarItemSelectedChange = { item ->
                            navController.navigateSingleTopWithPopUpTo(item)
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