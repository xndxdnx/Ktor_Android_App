package com.example.ktor.domain.usecase.update_hero_rating

import com.example.ktor.data.repository.Repository
import com.example.ktor.domain.model.Hero

class UpdateHeroRatingUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(
        heroId : Int,
        rating : Int
    ) : Hero {
        return repository.updateHeroRating(
            heroId = heroId,
            rating = rating
        )
    }
}