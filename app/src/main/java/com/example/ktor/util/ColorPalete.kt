package com.example.ktor.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.graphics.red
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult


object PaletteGenerator {

    suspend fun convertImageUrlToBitmap(
        imageUrl: String,
        context: Context
    ): Bitmap? {
        val loader = ImageLoader(context = context)
        val request = ImageRequest.Builder(context = context)
            .data(imageUrl)
            .allowHardware(false)
            .build()
        val imageResult = loader.execute(request = request)
        return if (imageResult is SuccessResult) {
            (imageResult.drawable as BitmapDrawable).bitmap
        } else {
            null
        }
    }

    fun extractColorsFromBitmap(bitmap: Bitmap): Map<String, String> {
        return mapOf(
            "vibrant" to parseColorSwatch(
                color = Palette.from(bitmap).generate().vibrantSwatch
            ),
            "darkVibrant" to parseColorSwatch(
                color = Palette.from(bitmap).generate().darkVibrantSwatch
            ),
            "onDarkVibrant" to parseBodyColor(
                color = Palette.from(bitmap).generate().darkVibrantSwatch?.bodyTextColor
            )
        )
    }
    
    private fun toHexColor(color: Int?) : String {
        return String.format("#%08X", color)
    }


    private fun parseColorSwatch(color: Palette.Swatch?): String {
        return color?.rgb.let(::toHexColor) ?: "#000000"
    }
    
   
    private fun parseBodyColor(color: Int?): String {
        return color?.let(::toHexColor) ?: "#FFFFFF"
    }
}