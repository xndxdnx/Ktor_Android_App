package com.example.ktor.presentation.screens.details.rating_snack_bar

import androidx.compose.ui.Modifier

data class RatingNotice(
    val errorText: String? = null,
    val heroName: String,
    val stars: Int,
    val isError: Boolean,
    val token: Long = System.nanoTime()
)
