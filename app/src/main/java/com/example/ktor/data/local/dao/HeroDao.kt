package com.example.ktor.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ktor.domain.model.Hero

@Dao
interface HeroDao {
    // ASC - порядок по возрастанию
    @Query("SELECT * FROM HERO_DATABASE_TABLE ORDER BY id ASC")
    fun getAllHeroes() : PagingSource<Int, Hero>
    
    @Query("SELECT * FROM HERO_DATABASE_TABLE WHERE id = :heroId")
    fun getSelectedHero(heroId: Int) : Hero
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeroes(heroesList: List<Hero>)

    @Query("DELETE FROM HERO_DATABASE_TABLE")
    suspend fun deleteAllHeroes()
    
}