package com.example.ktor.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ktor.data.local.dao.HeroDao
import com.example.ktor.data.local.dao.HeroRemoteKeyDao
import com.example.ktor.domain.model.Hero
import com.example.ktor.domain.model.HeroRemoteKey


@Database(
    entities = [
        Hero::class,
        HeroRemoteKey::class
    ], 
    version = 1
)
abstract class HeroDatabase() : RoomDatabase() {
    abstract fun heroDao(): HeroDao
    abstract fun heroRemoteKeyDao() : HeroRemoteKeyDao
}