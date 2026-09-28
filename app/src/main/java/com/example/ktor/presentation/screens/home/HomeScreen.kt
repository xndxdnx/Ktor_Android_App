package com.example.ktor.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun HomeScreen (
    viewModel: HomeScreenViewModel = hiltViewModel()
){
    
    val allHeroes = viewModel.getAllHeroes().collectAsLazyPagingItems()
    
    
    Scaffold(
        topBar = {HomeTopBar()}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color.White)
        ) { }
        
    }
   
}