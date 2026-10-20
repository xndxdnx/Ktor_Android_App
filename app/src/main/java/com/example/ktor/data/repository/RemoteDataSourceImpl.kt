package com.example.ktor.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.example.ktor.data.database.HeroDatabase
import com.example.ktor.data.paging_sourse.HeroRemoteMediator
import com.example.ktor.data.remote.KtorApi
import com.example.ktor.domain.model.Hero
import com.example.ktor.domain.model.HeroCreateRequest
import com.example.ktor.domain.repository.RemoteDataSource
import com.example.ktorinfocharacterapp.data.paging_source.SearchHeroesSource
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
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
                SearchHeroesSource(
                    jjkApi = api,
                    query = query,
                    cacheHeroes = { heroes ->
                        heroDao.insertHeroes(heroes)
                    })
            }
        ).flow
    }

    override suspend fun createHero(
        request: HeroCreateRequest,
        imageBytes: ByteArray,
        filename: String
    ): Hero {

        val json = Json.encodeToString(request)

        //
        val dataPart = MultipartBody.Part.createFormData(
            name = "data",
            filename = null,
            body = json.toRequestBody("application/json".toMediaType())
            )
        
        val imageBody = imageBytes.toRequestBody(
            "image/*".toMediaType()
        )
        
        val imagePart = MultipartBody.Part.createFormData(
            name = "image",
            filename = filename,
            body = imageBody
        )
        
        
        val response = api.createHero(
            data = dataPart,
            image = imagePart
        )
        
        if (!response.success || response.heroes.isEmpty()) {
            throw IllegalStateException(response.message)
        }
        
        val hero = response.heroes.first()
        
        database.withTransaction { 
            heroDao.insertHeroes(heroesList = listOf(hero))
        }
        
        return hero


    }
}