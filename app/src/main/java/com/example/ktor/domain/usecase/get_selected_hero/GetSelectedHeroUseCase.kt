package com.example.ktor.domain.usecase.get_selected_hero

import com.example.ktor.data.repository.Repository
import com.example.ktor.domain.model.Hero
import javax.inject.Inject

class GetSelectedHeroUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Int): Hero {
        return repository.getSelectedHero(id)
    }
}

