package com.example.ktor.domain.usecase.create_hero_use_case

import androidx.paging.PagingData
import com.example.ktor.data.repository.Repository
import com.example.ktor.domain.model.Hero
import com.example.ktor.domain.model.HeroCreateRequest
import kotlinx.coroutines.flow.Flow

class CreateHeroUseCase (
    private val repository: Repository
) {
   suspend operator fun invoke(
       request: HeroCreateRequest,
       imageBytes: ByteArray,
       filename: String
   ) : Hero { 
        return repository.createHero(
            request,
            imageBytes,
            filename
        )
    }
}