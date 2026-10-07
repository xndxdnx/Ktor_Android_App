package com.example.ktor.domain.usecase.search_heroes

import androidx.paging.PagingData
import androidx.room.util.query
import com.example.ktor.data.repository.Repository
import com.example.ktor.domain.model.Hero
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchHeroesUseCase @Inject constructor(
    private val repository: Repository
){
    operator fun invoke(query: String) : Flow<PagingData<Hero>> {
       return repository.searchHeroes(query = query.trim())
    }
}