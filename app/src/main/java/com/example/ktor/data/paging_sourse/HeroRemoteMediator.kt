package com.example.ktor.data.paging_sourse

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.ktor.data.database.HeroDatabase
import com.example.ktor.data.remote.KtorApi
import com.example.ktor.domain.model.Hero
import com.example.ktor.domain.model.HeroRemoteKey
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class HeroRemoteMediator @Inject constructor(
    private val database: HeroDatabase,
    private val api: KtorApi
) : RemoteMediator<Int, Hero>() {
    private val heroDao = database.heroDao()
    private val heroRemoteKeysDao = database.heroRemoteKeyDao()
    
//    override suspend fun initialize(): InitializeAction {
//        
//        val currentTime = System.currentTimeMillis()
//        val lastUpdater = heroRemoteKeysDao.getRemoteKey(1)?.lastUpdater
//        
//        
//        
//        
//        return super.initialize()
//    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Hero>
    ): MediatorResult {
        
        return try {
            
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextPage
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)

                    val prevPage = remoteKeys?.prevPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevPage
                }
            }

            val response = api.getAllHeroes(page)

            if (response.heroes.isNotEmpty()) {
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        heroDao.deleteAllHeroes()
                        heroRemoteKeysDao.deleteAllRemoteKeys()
                    }
                    
                    val prevPage = response.prefPage
                    val nextPage = response.nextPage

                    val keys = response.heroes.map { hero ->
                        HeroRemoteKey(
                            prevPage = prevPage,
                            nextPage = nextPage,
                            id = hero.id
                        )
                    }
                    
                    heroDao.insertHeroes(heroesList = response.heroes)
                    heroRemoteKeysDao.addAllRemoteKeys(keys)
                }
            }
            
            MediatorResult.Success(endOfPaginationReached = response.nextPage == null)
            
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
        
    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Hero>): HeroRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                heroRemoteKeysDao.getRemoteKey(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Hero>): HeroRemoteKey? {
        return state.pages.firstOrNull { pagingResource ->
            pagingResource.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { hero ->
            heroRemoteKeysDao.getRemoteKey(hero.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Hero>): HeroRemoteKey? {
        return state.pages.lastOrNull { resource ->
            resource.data.isNotEmpty()
        }?.data?.lastOrNull()
            ?.let { hero ->
                heroRemoteKeysDao.getRemoteKey(hero.id)
            }
    }

}