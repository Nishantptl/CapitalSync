package com.nishant.capitalsync.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.capitalsync.data.local.entity.SavingHabitEntity
import com.nishant.capitalsync.domain.usecase.BudgetStatus
import com.nishant.capitalsync.domain.usecase.GetMonthlyBudgetUseCase
import com.nishant.capitalsync.domain.usecase.GetSavingHabitsUseCase
import com.nishant.capitalsync.domain.usecase.ToggleHabitCompletionUseCase
import com.nishant.capitalsync.domain.usecase.UpdateMonthlyBudgetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import javax.inject.Inject

data class GoalsUiState(
    val habits: List<SavingHabitEntity> = emptyList(),
    val budgetStatus: BudgetStatus? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val getSavingHabitsUseCase: GetSavingHabitsUseCase,
    private val toggleHabitCompletionUseCase: ToggleHabitCompletionUseCase,
    private val getMonthlyBudgetUseCase: GetMonthlyBudgetUseCase,
    private val updateMonthlyBudgetUseCase: UpdateMonthlyBudgetUseCase
) : ViewModel() {

    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    @RequiresApi(Build.VERSION_CODES.O)
    val uiState: StateFlow<GoalsUiState> = combine(
        getSavingHabitsUseCase(),
        getMonthlyBudgetUseCase(currentMonth, currentYear)
    ) { habits, budgetStatus ->
        GoalsUiState(
            habits = habits,
            budgetStatus = budgetStatus,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GoalsUiState()
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleHabit(habit: SavingHabitEntity, date: LocalDate) {
        viewModelScope.launch {
            toggleHabitCompletionUseCase(habit, date)
        }
    }

    fun updateBudgetLimit(limit: Double) {
        viewModelScope.launch {
            updateMonthlyBudgetUseCase(currentMonth, currentYear, limit)
        }
    }
}