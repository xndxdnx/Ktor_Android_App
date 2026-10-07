package com.example.ktor.presentation.screens.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.ktor.navigation.Screens
import com.example.ktor.presentation.screens.home.components.ListContent
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.microseconds

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen (
    viewModel: SearchViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    
    val searchQuery by viewModel.searchQuery
    
    val searchedHeroes = viewModel.searchedHeroes.collectAsLazyPagingItems()
    
    val hasSearched = viewModel.hasSearched

    LaunchedEffect (searchQuery) {
        if (searchQuery.isBlank()) return@LaunchedEffect
        delay(500.microseconds)
        viewModel.searchHeroes(query = searchQuery)
    }
    
    
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            SearchTopBar(
                query = searchQuery,
                onTextChange = {viewModel.updateSearchQuery(it)},
                onSearchClick = {viewModel.searchHeroes(query = searchQuery)},
                onClose = {navHostController.popBackStack()}
            )
        }
    ) {paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
        ) { 
            if (hasSearched.value){
                ListContent(
                    heroes = searchedHeroes,
                    navHostController = navHostController,
                    
                )
            }
        }
        
    }
}