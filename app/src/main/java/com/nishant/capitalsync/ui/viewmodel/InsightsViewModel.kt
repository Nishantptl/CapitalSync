package com.nishant.capitalsync.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.capitalsync.domain.usecase.FinancialInsights
import com.nishant.capitalsync.domain.usecase.GetInsightsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class InsightsUiState(
    val insights: FinancialInsights = FinancialInsights(),
    val isLoading: Boolean = true
)

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val getInsightsUseCase: GetInsightsUseCase
) : ViewModel() {

    val uiState: StateFlow<InsightsUiState> = getInsightsUseCase()
        .map { insights ->
            InsightsUiState(
                insights = insights,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InsightsUiState()
        )
}