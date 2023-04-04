package br.com.alura.panucci.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import br.com.alura.panucci.dataStore
import br.com.alura.panucci.ui.screens.AuthenticationScreen
import br.com.alura.panucci.userPreferences
import kotlinx.coroutines.launch

private const val authenticationRoute = "authentication"

fun NavGraphBuilder.authenticationScreen(
    navController: NavHostController,
) {
    composable(authenticationRoute) {

        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        AuthenticationScreen { user ->
            scope.launch {
                context.dataStore.edit {
                    it[userPreferences] = user
                }
            }

        /*    navController.navigateToHighlightsList {
                popUpTo(navController.graph.id)
            }*/


        }
    }
}

fun NavController.navigateToAuthentication(builder: NavOptionsBuilder.() -> Unit) {
    navigate(authenticationRoute, navOptions(builder))
}