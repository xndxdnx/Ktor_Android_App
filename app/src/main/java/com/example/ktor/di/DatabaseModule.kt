package com.example.ktor.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ktor.data.database.HeroDatabase
import com.example.ktor.data.remote.KtorApi
import com.example.ktor.data.repository.LocalDataSourceImpl
import com.example.ktor.data.repository.RemoteDataSourceImpl
import com.example.ktor.domain.repository.LocalDataSource
import com.example.ktor.domain.repository.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HeroDatabase = Room.databaseBuilder(
        context = context,
        klass = HeroDatabase::class.java,
        name = "table"
    ).build()

    @Provides
    @Singleton
    fun provideLocalDatasource(database: HeroDatabase) :  LocalDataSource {
        return LocalDataSourceImpl(database)
    }

}