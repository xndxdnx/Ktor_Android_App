package com.example.ktor.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.ktor.data.database.HeroDatabase
import com.example.ktor.data.paging_sourse.HeroRemoteMediator
import com.example.ktor.data.remote.KtorApi
import com.example.ktor.domain.model.Hero
import com.example.ktor.domain.repository.RemoteDataSource
import com.example.ktorinfocharacterapp.data.paging_source.SearchHeroesSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RemoteDataSourceImpl @Inject constructor(
    private val api: KtorApi,
    private val database: HeroDatabase
) : RemoteDataSource {

    private val heroDao = database.heroDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun getAllHeroes(): Flow<PagingData<Hero>> {
        return Pager(
            config = PagingConfig(5),
            remoteMediator = HeroRemoteMediator(api = api, database = database),
            pagingSourceFactory = { heroDao.getAllHeroes() }
        ).flow
    }

    override fun searchHeroes(query: String): Flow<PagingData<Hero>> {
        return Pager(
            config = PagingConfig(pageSize = 6),
            pagingSourceFactory = {
                SearchHeroesSource(jjkApi = api, 
                    query = query,
                    cacheHeroes = { heroes ->
                    heroDao.insertHeroes(heroes)
                })
            }
        ).flow
    }
}