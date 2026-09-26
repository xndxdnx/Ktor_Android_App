package com.example.ktor.di

import com.example.ktor.data.remote.KtorApi
import com.example.ktor.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    
    @Provides
    @Singleton
    fun provideOkHttpClient() : OkHttpClient = OkHttpClient.Builder()
        .readTimeout(timeout = 15, TimeUnit.MINUTES)
        .connectTimeout(timeout = 15, TimeUnit.MINUTES)
        .build()


    @Provides
    @Singleton
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient
    ): Retrofit {
        
        val contentType = "application/json".toMediaType()
        
        val response = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
        
        return response
    }

    @Provides
    @Singleton
    fun provideKtorApi(retrofit: Retrofit) : KtorApi = retrofit.create(KtorApi::class.java)
        
    
}