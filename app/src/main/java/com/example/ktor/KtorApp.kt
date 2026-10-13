package com.example.ktor

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.ktor.navigation.NavGraph

@Composable
fun KtorApp (){
    
    val navController = rememberNavController()

    NavGraph(navController)
}