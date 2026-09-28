package com.example.ktor.domain.usecase

import com.example.ktor.domain.usecase.get_all_heroes.GetAllHeroesUseCase
import com.example.ktor.domain.usecase.read_onboarding.ReadOnboardingUseCase
import com.example.ktor.domain.usecase.save_onboarding.SaveOnboardingUseCase

data class UseCases(
    val saveOnboardingUseCase : SaveOnboardingUseCase,
    val readOnboardingUseCase : ReadOnboardingUseCase,
    val getAllHeroesUseCase: GetAllHeroesUseCase
)
