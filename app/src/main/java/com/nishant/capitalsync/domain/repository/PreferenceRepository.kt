package com.nishant.capitalsync.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    fun isBalanceVisible(): Flow<Boolean>
    suspend fun setBalanceVisible(visible: Boolean)

    fun isSwipeActionsEnabled(): Flow<Boolean>
    suspend fun setSwipeActionsEnabled(enabled: Boolean)

    fun isOnboardingCompleted(): Flow<Boolean>
    suspend fun setOnboardingCompleted(completed: Boolean)
}