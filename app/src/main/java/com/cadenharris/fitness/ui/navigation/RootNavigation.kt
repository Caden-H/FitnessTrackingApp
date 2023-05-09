package com.cadenharris.fitness.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.cadenharris.fitness.ui.screens.*
import com.cadenharris.fitness.ui.viewmodels.RootNavigationViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RootNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val viewModel: RootNavigationViewModel = viewModel()


    Scaffold(topBar = {
//        TopAppBar() {
//            Text(text = "My App")
//        }
    },

//        bottomBar = {
//            if (currentDestination?.hierarchy?.none { it.route == Routes.launchNavigation.route || it.route == Routes.splashScreen.route } == true) {
//                BottomBar(navController = navController)
//            }
//        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.splashScreen.route,
            modifier = Modifier.padding(paddingValues = it)
        ) {
            navigation(route = Routes.launchNavigation.route, startDestination = Routes.launch.route) {
                composable(Routes.launch.route) { LaunchScreen(navController) }
                composable(Routes.signIn.route) { SignInScreen(navController) }
                composable(Routes.signUp.route) { SignUpScreen(navController) }
            }
            navigation(route = Routes.fitnessNavigation.route, startDestination = Routes.home.route) {
                composable(
                    route = "editfitness?id={id}",
                    arguments = listOf(navArgument("id") { defaultValue = "new" })
                ) {navBackStackEntry ->
                    FitnessModificationScreen(navController, navBackStackEntry.arguments?.get("id").toString())
                }
                composable(Routes.home.route) { HomeScreen(navController = navController) }
//                composable(Routes.profile.route) { HomeScreen(navController = navController) }
//                composable(Routes.history.route) { HomeScreen(navController = navController) }
////                composable(Routes.profile.route) { ProfileScreen(navController = navController) }
////                composable(Routes.history.route) { HistoryScreen(navController = navController) }
            }
            composable(route = Routes.splashScreen.route) { SplashScreen(navController) }
        }
    }

}
