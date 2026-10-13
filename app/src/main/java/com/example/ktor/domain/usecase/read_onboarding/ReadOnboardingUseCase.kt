package com.example.ktor.domain.usecase.read_onboarding

import com.example.ktor.data.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadOnboardingUseCase @Inject constructor(
    private val repository: Repository
){
    operator fun invoke() : Flow<Boolean>{
        return repository.readOnboardingState() 
    }
}