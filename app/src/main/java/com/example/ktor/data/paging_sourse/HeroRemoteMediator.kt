package com.example.ktor.data.paging_sourse

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.ktor.data.database.HeroDatabase
import com.example.ktor.data.remote.KtorApi
import com.example.ktor.domain.model.Hero
import com.example.ktor.domain.model.HeroRemoteKey
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalPagingApi::class)
class HeroRemoteMediator @Inject constructor(
    private val database: HeroDatabase,
    private val api: KtorApi
) : RemoteMediator<Int, Hero>() {
    private val heroDao = database.heroDao()
    private val heroRemoteKeysDao = database.heroRemoteKeyDao()

    override suspend fun initialize(): InitializeAction {

//        val currentTime = System.currentTimeMillis()
//        val lastUpdater = heroRemoteKeysDao.getRemoteKey(1)?.lastUpdater ?: 0L
//
//        val cacheTimeout = 1440
//        
//        val diffInMinutes = (currentTime - lastUpdater) / 1000 / 60
//        
//        return if (diffInMinutes.toInt() <= cacheTimeout) {
//            InitializeAction.SKIP_INITIAL_REFRESH
//        }else {
//            InitializeAction.LAUNCH_INITIAL_REFRESH
//        }

        val heroCount = heroDao.getHeroCount()
        val heroLastRemoteKey = heroRemoteKeysDao.getLastRemoteKey()

        val cacheIsComplete = heroCount > 0 && heroLastRemoteKey?.nextPage == null

        if (!cacheIsComplete) {
            Log.d("RemoteMediator", "REFRESH! (cache empty or incomplete)")
            return InitializeAction.LAUNCH_INITIAL_REFRESH
        }
        Log.d("RemoteMediator", "SKIP refresh, local list is complete ($heroCount heroes)")
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Hero>
    ): MediatorResult {

        if (loadType == LoadType.REFRESH) {
            return loadPages()
        }
        
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    1
                }

                LoadType.APPEND -> {
                    val remoteKeys =
                        getRemoteKeyForLastItem(state) ?: heroRemoteKeysDao.getLastRemoteKey()
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = true
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


            if (!response.success) {

                if (loadType == LoadType.APPEND) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                return MediatorResult.Error(
                    IllegalStateException(
                        response.message?: "Failed to load heroes"
                    )
                )
            }


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
                            id = hero.id,
                            lastUpdater = response.lastUpdater
                        )
                    }

                    heroDao.insertHeroes(heroesList = response.heroes)
                    heroRemoteKeysDao.addAllRemoteKeys(keys)
                }
            }

            MediatorResult.Success(endOfPaginationReached = response.nextPage == null)

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }catch (e: HttpException) {
            
            if (e.code() == 404 && loadType == LoadType.APPEND) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            Log.e("RemoteMediator", "HTTP ${e.code()} while loading page", e)
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            Log.e("RemoteMediator", "Failed to load heroes", e)
            return MediatorResult.Error(e)
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
    
    private suspend fun loadPages() : MediatorResult {
       return try {
            var page = 1
            
            val heroes = mutableListOf<Hero>()
            
            val keys = mutableListOf<HeroRemoteKey>()
            
            while (page in 1..50) {
                val response = api.getAllHeroes(page)
                
                if (!response.success || response.heroes.isEmpty()) {
                    break
                }
                
                heroes+=response.heroes
                
                keys += response.heroes.map { hero ->
                    HeroRemoteKey(
                        prevPage = response.prefPage,
                        nextPage = response.nextPage,
                        id = hero.id,
                        lastUpdater = response.lastUpdater
                    )
                }
                val next = response.nextPage ?: break
                page = next
                
                database.withTransaction { 
                    heroDao.deleteAllHeroes()
                    heroRemoteKeysDao.deleteAllRemoteKeys()
                    
                    if (heroes.isNotEmpty()){
                        heroRemoteKeysDao.addAllRemoteKeys(keys)
                        heroDao.insertHeroes(heroes)
                    }
                    
                    
                }
                
            }

            MediatorResult.Success(true)
            
        }catch (e: Exception) {
            Log.e("RemoteMediator", "Failed to load full hero list", e)
            MediatorResult.Error(e)
        }
        
        
        
    }

}