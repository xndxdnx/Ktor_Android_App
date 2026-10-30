package com.example.ktor.paging_source
import com.example.ktor.data.paging_sourse.MediatorInitAction
import com.example.ktor.data.paging_sourse.resolveInitializeAction
import org.junit.Assert.assertEquals
import org.junit.Test


class HeroRemoteMediatorTest {

    @Test
    fun ` ` () {
        val action = resolveInitializeAction(
            lastUpdatedMillis = 0L,
            nowMillis = 1_000_000L,
            cacheTimeoutMinutes = 1440
        )
        assertEquals(MediatorInitAction.Refresh, action)
    }

    @Test
    fun `skip refresh inside cache timeout` () {
        val now = 1_000_000L
        val action = resolveInitializeAction(
            lastUpdatedMillis = now - 60_000,
            nowMillis = now,
            cacheTimeoutMinutes = 1440
        )
        assertEquals(MediatorInitAction.Skip, action)
    }

    @Test
    fun `refresh when cache expired` () {
        val now = 1_000_000L
        val action = resolveInitializeAction(
            lastUpdatedMillis = now - (1441L * 60_000),
            nowMillis = now,
            cacheTimeoutMinutes = 1440
        )
        assertEquals(MediatorInitAction.Refresh, action)
    }
}