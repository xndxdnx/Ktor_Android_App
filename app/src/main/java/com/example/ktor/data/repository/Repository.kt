package com.example.ktor.data.repository

import com.example.ktor.domain.repository.DatastoreOperations
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val datastoreOperations: DatastoreOperations 
) {
    suspend fun saveOnboardingState(completed: Boolean) {
        datastoreOperations.saveOnboardingState(completed)
    }
    
    fun readOnboardingState() : Flow<Boolean> {
        return datastoreOperations.readOnboardingState()
    }
    
}