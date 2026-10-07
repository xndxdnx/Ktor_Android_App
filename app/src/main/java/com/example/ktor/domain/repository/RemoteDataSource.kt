package com.example.ktor.domain.repository

import androidx.paging.PagingData
import androidx.room.RawQuery
import com.example.ktor.domain.model.Hero
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource{
    
    fun getAllHeroes() : Flow <PagingData<Hero>>
    
    fun searchHeroes(query: String) : Flow <PagingData<Hero>> 
    
}