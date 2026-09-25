package com.example.ktor.data.remote


import com.example.ktor.domain.model.ApiResponse
import retrofit2.http.GET
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
    
    
    
}