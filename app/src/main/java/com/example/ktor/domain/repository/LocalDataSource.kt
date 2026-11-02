package com.example.ktor.domain.repository

import com.example.ktor.domain.model.Hero

interface LocalDataSource {
    
    suspend fun getSelectedHero(id: Int): Hero
    
    suspend fun updateHeroRating(heroId: Int, rating: Double)
}