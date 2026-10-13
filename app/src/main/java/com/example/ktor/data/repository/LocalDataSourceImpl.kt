package com.example.ktor.data.repository

import com.example.ktor.data.database.HeroDatabase
import com.example.ktor.domain.model.Hero
import com.example.ktor.domain.repository.LocalDataSource

class LocalDataSourceImpl(
    private val database: HeroDatabase
) : LocalDataSource {
    override suspend fun getSelectedHero(id: Int): Hero {
        return database.heroDao().getSelectedHero(id)
    }
}