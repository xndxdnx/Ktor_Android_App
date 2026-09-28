package com.example.ktor.data.repository

import androidx.paging.PagingData
import com.example.ktor.domain.model.Hero
import com.example.ktor.domain.repository.DatastoreOperations
import com.example.ktor.domain.repository.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val datastoreOperations: DatastoreOperations ,
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun saveOnboardingState(completed: Boolean) {
        datastoreOperations.saveOnboardingState(completed)
    }
    
    fun readOnboardingState() : Flow<Boolean> {
        return datastoreOperations.readOnboardingState()
    }
    
    fun getAllHeroes() : Flow<PagingData<Hero>> = remoteDataSource.getAllHeroes()
    
    
}