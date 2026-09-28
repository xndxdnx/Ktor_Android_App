package com.example.ktor.domain.usecase.get_all_heroes

import androidx.paging.PagingData
import com.example.ktor.data.repository.Repository
import com.example.ktor.domain.model.Hero
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllHeroesUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke() : Flow<PagingData<Hero>>{
        return repository.getAllHeroes()
    }
}