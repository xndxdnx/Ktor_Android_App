package com.example.ktor.presentation.screens.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ktor.presentation.screens.details.components.DetailsContent
import com.example.ktor.util.Constants.BASE_URL
import com.example.ktor.util.PaletteGenerator.convertImageUrlToBitmap
import com.example.ktor.util.PaletteGenerator.extractColorsFromBitmap
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailsScreen(
    viewModel: DetailsScreenViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val context = LocalContext.current

    val selectedHero by viewModel.selectedHero.collectAsState()

    val colorPalette = viewModel.colorPalette
    
    LaunchedEffect(true) {
        viewModel.uiEvents.collectLatest { event ->
            when (event) {
                is DetailsUIEvents.GenerateColorPalette -> {
                    selectedHero?.let { hero ->
                        val bitmap = convertImageUrlToBitmap(
                            imageUrl = "$BASE_URL${hero.image}",
                            context
                        )
                        val colors = bitmap?.let {
                            extractColorsFromBitmap(it)
                        } ?: mapOf(
                            "vibrant" to "#6650a4",
                            "darkVibrant" to "#2A2A2A",
                            "onDarkVibrant" to "#FFFFFF"
                        )
                        viewModel.setColorPalette(colors)
                    }
                }
            }
        }

    }
    LaunchedEffect(key1 = selectedHero) {
        if (selectedHero != null && colorPalette.value.isEmpty()) {
            viewModel.generateColorPalette()
        }
    }

    if (selectedHero != null && colorPalette.value.isNotEmpty()) {
        DetailsContent(
            navHostController = navHostController,
            selectedHero = selectedHero,
            colors = colorPalette.value
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier 
                    .size(150.dp)
            )
        }
    }
}