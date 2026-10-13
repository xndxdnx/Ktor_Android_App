package com.example.ktor.navigation

sealed class Screens (
   val route: String
){
    object SplashScreen : Screens(route = "splash_screen")
    object HomeScreen : Screens(route = "home_screen")
    object SearchScreen : Screens(route = "search_screen")
    object DetailsScreen : Screens(route = "details_screen/{heroId}") {
        fun passHeroId(id: Int) : String {
            return "details_screen/$id"
        }
    }
    object WelcomeScreen : Screens(route = "welcome_screen")
    
}