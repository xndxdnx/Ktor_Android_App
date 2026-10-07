package com.example.ktor.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.ktor.navigation.Screens
import com.example.ktor.presentation.screens.home.components.ListContent

@Composable
fun HomeScreen (
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navHostController: NavHostController
){
    
    val allHeroes = viewModel.getAllHeroes().collectAsLazyPagingItems()
    
    
    Scaffold(
        modifier = Modifier
            .systemBarsPadding(),
        topBar = {HomeTopBar(
            onSearchClicked = {navHostController.navigate(Screens.SearchScreen.route)}
        )},
        content = {paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                ListContent(
                    heroes = allHeroes,
                    navHostController = navHostController
                )
            }
        }
    ) 
   
}