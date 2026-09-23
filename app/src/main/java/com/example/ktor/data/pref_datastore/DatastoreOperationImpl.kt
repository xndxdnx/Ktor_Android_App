package com.example.ktor.data.pref_datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.ktor.domain.repository.DatastoreOperations
import com.example.ktor.util.Constants.ONBOARDING_PREF_KEY
import com.example.ktor.util.Constants.ONBOARDING_PREF_TABLE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val Context.datastore : DataStore<Preferences> by preferencesDataStore(ONBOARDING_PREF_TABLE_NAME)

class DatastoreOperationImpl(
    context: Context
) : DatastoreOperations{
    
    private object PreferenceKey {
        val onBoardingKey = booleanPreferencesKey(ONBOARDING_PREF_KEY)
    }
    
    private val dataStore = context.datastore
    
    override suspend fun saveOnboardingState(complete: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKey.onBoardingKey] = complete
        }
    }
    
    override fun readOnboardingState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val onBoardingState = preferences[PreferenceKey.onBoardingKey] ?: false
                onBoardingState
            }
    }
}