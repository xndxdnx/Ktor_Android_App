package com.example.ktor.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ktor.domain.model.HeroRemoteKey
import com.example.ktor.util.Constants.HERO_REMOTE_KEY_DATABASE

@Dao
interface HeroRemoteKeyDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(keys: List<HeroRemoteKey>)
    
    @Query("DELETE FROM HERO_REMOTE_KEY_TABLE")
    suspend fun deleteAllRemoteKeys()
    
    @Query("SELECT * FROM HERO_REMOTE_KEY_TABLE WHERE id =:id")
    suspend fun getRemoteKey(id: Int) : HeroRemoteKey?
    
}