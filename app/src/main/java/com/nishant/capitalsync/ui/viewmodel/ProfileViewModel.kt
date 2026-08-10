package com.nishant.capitalsync.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.capitalsync.domain.repository.PreferenceRepository
import com.nishant.capitalsync.domain.usecase.ExportTransactionsUseCase
import com.nishant.capitalsync.domain.usecase.GetDashboardStatsUseCase
import com.nishant.capitalsync.domain.usecase.UpdateBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val currentBalance: Double = 0.0,
    val isSwipeEnabled: Boolean = false,
    val isBalanceVisible: Boolean = true,
    val isLoading: Boolean = true
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getDashboardStatsUseCase: GetDashboardStatsUseCase,
    private val updateBalanceUseCase: UpdateBalanceUseCase,
    private val preferenceRepository: PreferenceRepository,
    private val exportTransactionsUseCase: ExportTransactionsUseCase
) : ViewModel() {

    private val _exportEvent = MutableSharedFlow<String>()
    val exportEvent = _exportEvent.asSharedFlow()

    val uiState: StateFlow<ProfileUiState> = combine(
        getDashboardStatsUseCase(),
        preferenceRepository.isSwipeActionsEnabled(),
        preferenceRepository.isBalanceVisible()
    ) { stats, isSwipeEnabled, isBalanceVisible ->
        ProfileUiState(
            currentBalance = stats.totalBalance,
            isSwipeEnabled = isSwipeEnabled,
            isBalanceVisible = isBalanceVisible,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileUiState()
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun exportTransactions() {
        viewModelScope.launch {
            val csv = exportTransactionsUseCase()
            _exportEvent.emit(csv)
        }
    }

    fun updateBalance(newBalance: Double) {
        viewModelScope.launch {
            updateBalanceUseCase(newBalance)
        }
    }

    fun toggleSwipeEnabled() {
        viewModelScope.launch {
            preferenceRepository.setSwipeActionsEnabled(!uiState.value.isSwipeEnabled)
        }
    }

    fun toggleBalanceVisible() {
        viewModelScope.launch {
            preferenceRepository.setBalanceVisible(!uiState.value.isBalanceVisible)
        }
    }
}