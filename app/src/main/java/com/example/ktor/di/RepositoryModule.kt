package com.example.ktor.di

import android.content.Context
import com.example.ktor.data.repository.DatastoreOperationImpl
import com.example.ktor.data.repository.Repository
import com.example.ktor.domain.repository.DatastoreOperations
import com.example.ktor.domain.usecase.UseCases
import com.example.ktor.domain.usecase.read_onboarding.ReadOnboardingUseCase
import com.example.ktor.domain.usecase.save_onboarding.SaveOnboardingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    @Provides
    @Singleton
    fun provideDatastoreOperation(@ApplicationContext context: Context): DatastoreOperations{
        return DatastoreOperationImpl(context)
    }

    @Provides
    @Singleton
    fun provideUseCases(
        repository: Repository
    ): UseCases{
        return UseCases(
            readOnboardingUseCase = ReadOnboardingUseCase(repository),
            saveOnboardingUseCase = SaveOnboardingUseCase(repository)
        )
    }
    
    
}