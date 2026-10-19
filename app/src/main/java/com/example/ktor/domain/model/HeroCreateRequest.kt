package com.example.ktor.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HeroCreateRequest (
    val name: String,
    val image: String,
    val about: String,
    val rating: Double,
    val power: Int,
    val month: String,
    val day: String,
    val family: List<String>,
    val natureTypes: List<String>,
    val abilities: List<String>
)