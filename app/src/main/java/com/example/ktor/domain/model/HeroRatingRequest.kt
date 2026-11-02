package com.example.ktor.domain.model

import com.example.ktor.util.Constants.MAX_RATING
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
data class HeroRatingRequest(
    val rating: Int,
)

fun displayStarCount(rating: Double) : Int {
    return rating.roundToInt().coerceIn(0,MAX_RATING)
}

fun isSelectableStarRating(stars: Int) : Boolean{
    return stars in 1..MAX_RATING
}
