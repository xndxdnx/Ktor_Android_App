package com.example.ktor.presentation.screens.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
                    val bitmap = convertImageUrlToBitmap(
                        imageUrl = "$BASE_URL${selectedHero?.image}",
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
   
    if (colorPalette.value.isNotEmpty()) {
        DetailsContent(
            navHostController = navHostController,
            selectedHero = selectedHero,
            colors = colorPalette.value
        )
    } else {
        viewModel.generateColorPalette()
    }


}