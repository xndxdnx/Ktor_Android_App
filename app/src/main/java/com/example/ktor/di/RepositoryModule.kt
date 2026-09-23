package com.example.ktor.di

import android.content.Context
import com.example.ktor.data.pref_datastore.DatastoreOperationImpl
import com.example.ktor.domain.repository.DatastoreOperations
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
    
}