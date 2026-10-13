package com.example.ktor.domain.usecase.save_onboarding

import com.example.ktor.data.repository.Repository
import javax.inject.Inject

class SaveOnboardingUseCase  @Inject constructor(
    private val repository: Repository
){
    
    suspend operator fun invoke(complete: Boolean){
        repository.saveOnboardingState(complete)
    }
}