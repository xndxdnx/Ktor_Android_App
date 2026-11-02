package com.example.ktor.data.remote


import com.example.ktor.domain.model.ApiResponse
import com.example.ktor.domain.model.HeroRatingRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface KtorApi {
    
    @GET("/heroes")
    suspend fun getAllHeroes(
        @Query("page") page: Int = 1
    ) : ApiResponse
    
    
    @GET("/heroes/search")
    suspend fun searchHeroes(
        @Query("name") name: String
    ) : ApiResponse
    
    @Multipart
    @POST("/post/heroes")
    suspend fun createHero(
        @Part data: MultipartBody.Part,
        @Part image: MultipartBody.Part
    ) : ApiResponse
    
    @PUT("/heroes/{heroId}/rating")
    suspend fun updateHeroRating (
        @Path("heroId") heroId : Int,
        @Body request: HeroRatingRequest
    ) : ApiResponse
    
}