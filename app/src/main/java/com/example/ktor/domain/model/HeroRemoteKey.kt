package com.example.ktor.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ktor.util.Constants.HERO_REMOTE_KEY_DATABASE

@Entity(tableName = HERO_REMOTE_KEY_DATABASE)
data class HeroRemoteKey(
    val prevPage: Int?,
    val nextPage: Int?,
    @PrimaryKey(autoGenerate = false)
    val id : Int
)
