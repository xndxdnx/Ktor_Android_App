package com.example.ktor.domain.repository

import kotlinx.coroutines.flow.Flow


interface DatastoreOperations {
    
    suspend fun saveOnboardingState (complete : Boolean)
    
    fun readOnboardingState () : Flow<Boolean>
}