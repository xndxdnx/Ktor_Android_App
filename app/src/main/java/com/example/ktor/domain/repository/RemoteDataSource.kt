package com.example.ktor.domain.repository

import androidx.paging.PagingData
import androidx.room.RawQuery
import com.example.ktor.domain.model.Hero
import com.example.ktor.domain.model.HeroCreateRequest
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.http.Part

interface RemoteDataSource{
    
    fun getAllHeroes() : Flow <PagingData<Hero>>
    
    fun searchHeroes(query: String) : Flow <PagingData<Hero>> 
    
    suspend fun createHero(
        request: HeroCreateRequest,
        imageBytes: ByteArray,
        filename: String
    ) : Hero
}