package com.example.ktorinfocharacterapp.data.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ktor.data.remote.KtorApi
import com.example.ktor.domain.model.Hero

class SearchHeroesSource(
    private val jjkApi: KtorApi,
    private val query: String,
    private val cacheHeroes: suspend (List<Hero>) -> Unit = {}
) : PagingSource<Int, Hero>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hero> {
        return try {
            if (query.isBlank()) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
            
            val response = jjkApi.searchHeroes(name = query)
            
            val heroes = response.heroes
            
            if (heroes.isNotEmpty()) {
                cacheHeroes(heroes)
            }
            
            LoadResult.Page(
                data = heroes,
                prevKey = response.prefPage,
                nextKey = response.nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Hero>): Int? {
        return state.anchorPosition
    }
}