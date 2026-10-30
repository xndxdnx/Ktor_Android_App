package com.example.ktor.data.paging_sourse

import com.example.ktor.data.database.HeroDatabase


internal enum class MediatorInitAction (){
    Skip,
    Refresh
}

internal fun resolveInitializeAction(
    lastUpdatedMillis : Long,
    nowMillis : Long,
    cacheTimeoutMinutes: Int
) :  MediatorInitAction {
    
    if (lastUpdatedMillis == 0L) {
        return MediatorInitAction.Refresh
    }
    
    val diffInMinutes = (nowMillis - lastUpdatedMillis) / 1000 / 60
    
    return if (diffInMinutes <= cacheTimeoutMinutes) MediatorInitAction.Skip else MediatorInitAction.Refresh
    
}

