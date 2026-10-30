package com.example.ktor

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ktor.domain.model.HeroCreateRequest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class CreateHeroE2ETest {

    @Test
    fun createHero_fromDownloadFolderImage_reachesKtorOnHost() {
        
        val testContext = InstrumentationRegistry.getInstrumentation().targetContext
        
        val imageBytes = testContext.assets.open("hero_e2e.jpg").use { it.readBytes() }

        val heroJson = Json.encodeToString(
            HeroCreateRequest(
                name = "E2E Emulator Hero",
                about = "Photo from emulator Downloads",
                rating = 4.2,
                power = 77,
                month = "Oct",
                day = "4",
                family = listOf("Test"),
                abilities = listOf("Smoke"),
                natureTypes = listOf("QA")
            )
        )

        val multipart = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addPart(
                MultipartBody.Part.createFormData(
                    "data",
                    null,
                    heroJson.toRequestBody("application/json".toMediaType())
                )
            )
            .addPart(
                MultipartBody.Part.createFormData(
                    "image",
                    "hero_e2e.jpg",
                    imageBytes.toRequestBody("image/jpeg".toMediaType())
                )
            )
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .build()

        val response = client.newCall(
            Request.Builder()
                .url("http://10.0.2.2:8080/post/heroes")
                .post(multipart)
                .build()
        ).execute()

        val body = response.body?.string() ?: ""
        assertTrue("HTTP ${response.code}: $body", response.isSuccessful)
        assertTrue(body.contains("E2E Emulator Hero"))
        assertTrue(body.contains("\"success\":true"))
    }
}