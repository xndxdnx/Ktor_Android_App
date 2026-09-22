package com.example.ktor.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ktor.presentation.screens.splash.SplashScreen
import com.example.ktor.presentation.screens.welcome.weclome_screen.WelcomeScreen
import com.example.ktor.util.Constants.DETAILS_ARGUMENT_KEY

@Composable
fun NavGraph(
    navHostController: NavHostController
) {

    NavHost(
        navController = navHostController,
        startDestination = Screens.WelcomeScreen.route
    ) {
        
        composable (Screens.SplashScreen.route) {
            SplashScreen(navHostController = navHostController)
        }
        composable (Screens.HomeScreen.route) {  }
        composable (Screens.SearchScreen.route) {  }
        composable (
            route = Screens.DetailsScreen.route,
            arguments = listOf(navArgument(DETAILS_ARGUMENT_KEY){
                type = NavType.IntType
            })
        ) { }
        composable (Screens.WelcomeScreen.route) { WelcomeScreen(navHostController) }
        
        
    }


}