package br.com.alura.panucci.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.alura.panucci.dataStore
import br.com.alura.panucci.ui.screens.AuthenticationScreen
import br.com.alura.panucci.userPreferences
import kotlinx.coroutines.launch

fun NavGraphBuilder.authenticationScreen(
    navController: NavHostController,
) {
    composable(AppDestination.Authentication.route) {

        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        AuthenticationScreen { user ->
            scope.launch {
                context.dataStore.edit {
                    it[userPreferences] = user
                }
            }

            navController.navigate(AppDestination.Highlight.route) {
                popUpTo(navController.graph.id)
            }
        }
    }
}