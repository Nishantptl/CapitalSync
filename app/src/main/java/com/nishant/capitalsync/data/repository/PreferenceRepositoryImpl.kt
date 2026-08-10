package com.nishant.capitalsync.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.nishant.capitalsync.domain.repository.PreferenceRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : PreferenceRepository {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_BALANCE_VISIBLE = "is_balance_visible"
        private const val KEY_SWIPE_ENABLED = "is_swipe_enabled"
        private const val KEY_ONBOARDING_COMPLETED = "is_onboarding_completed"
    }

    private fun getBooleanFlow(key: String, defaultValue: Boolean): Flow<Boolean> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, prefKey ->
            if (prefKey == key) {
                trySend(prefs.getBoolean(key, defaultValue))
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        trySend(sharedPreferences.getBoolean(key, defaultValue))
        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    override fun isBalanceVisible(): Flow<Boolean> =
        getBooleanFlow(KEY_BALANCE_VISIBLE, defaultValue = true)

    override suspend fun setBalanceVisible(visible: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_BALANCE_VISIBLE, visible) }
    }

    override fun isSwipeActionsEnabled(): Flow<Boolean> =
        getBooleanFlow(KEY_SWIPE_ENABLED, defaultValue = false)

    override suspend fun setSwipeActionsEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_SWIPE_ENABLED, enabled) }
    }

    override fun isOnboardingCompleted(): Flow<Boolean> =
        getBooleanFlow(KEY_ONBOARDING_COMPLETED, defaultValue = false)

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_ONBOARDING_COMPLETED, completed) }
    }
}