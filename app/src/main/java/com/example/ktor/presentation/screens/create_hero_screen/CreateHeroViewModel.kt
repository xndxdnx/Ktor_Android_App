package com.example.ktor.presentation.screens.create_hero_screen

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ktor.domain.model.HeroCreateRequest
import com.example.ktor.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CreateHeroViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {
    var isLoading = mutableStateOf(false)

    var errorMessage = mutableStateOf<String?>(null)

    fun createHero(
        imageBytes: ByteArray,
        fileName: String,
        name: String,
        about: String,
        rating: String,
        power: String,
        month: String,
        day: String,
        family: String,
        abilities: String,
        natureTypes: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            try {

                val request = HeroCreateRequest(
                    name = name.trim(),
                    about = about.trim(),
                    rating = rating.toDoubleOrNull() ?: 0.0,
                    power = power.toIntOrNull() ?: 0,
                    month = month.trim(),
                    day = day.trim(),
                    family = family.splitCsv(),
                    abilities = abilities.splitCsv(),
                    natureTypes = natureTypes.splitCsv()
                )

                useCases.createHeroUseCase(
                    request = request,
                    imageBytes = imageBytes,
                    filename = fileName
                )

                onSuccess()

            } catch (e: HttpException) {
                val serverMessage = e.response()?.errorBody()?.string()
                    ?.takeIf { it.isNotBlank() }
                errorMessage.value = serverMessage ?: "Server error (${e.code()})."
            } catch (e: IOException) {
                errorMessage.value =
                    "Cannot reach server at 10.0.2.2:8080. Start Ktor on your Mac and use the emulator (not a physical device without changing BASE_URL)."
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Failed to create hero."
            } finally {
                isLoading.value = false
            }

        }
        
    }
    
    suspend fun loadImageFromUri(
        uri: Uri,
        context: Context,
    ): Pair<ByteArray, String>? = withContext(Dispatchers.IO) {

        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: return@withContext null

        bytes to resolveFileName(uri, context)

    }

    
}

private fun resolveFileName(
    uri: Uri,
    context: Context,
): String {

    val mime = context.contentResolver.getType(uri)

    val extension = when {
        mime?.contains("png") == true -> "png"
        mime?.contains("webp") == true -> "webp"
        mime?.contains("heic") == true || mime?.contains("heif") == true -> "jpg"
        else -> "jpg"
    }
    return "hero_image.$extension"
}


private fun String.splitCsv(): List<String> {
    return split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
}





