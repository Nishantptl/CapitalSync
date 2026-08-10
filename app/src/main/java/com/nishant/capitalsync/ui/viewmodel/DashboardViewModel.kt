package com.nishant.capitalsync.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.capitalsync.data.local.entity.TransactionEntity
import com.nishant.capitalsync.domain.repository.CapitalSyncRepository
import com.nishant.capitalsync.domain.repository.PreferenceRepository
import com.nishant.capitalsync.domain.usecase.GetDashboardStatsUseCase
import com.nishant.capitalsync.domain.usecase.GetTransactionsUseCase
import com.nishant.capitalsync.domain.usecase.UpdateBalanceUseCase
import com.nishant.capitalsync.utils.CategoryUtils
import com.nishant.capitalsync.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WeeklySpendingDataPoint(
    val dayLabel: String,
    val totalAmount: Double,
    val categorySpent: List<CategorySpendingPoint> = emptyList()
)

data class CategorySpendingPoint(
    val categoryName: String,
    val amount: Double,
    val iconResId: Int? = null
)

data class DashboardUiState(
    val welcomeMessage: String = "Welcome!",
    val totalBalance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val recentTransactions: List<TransactionEntity> = emptyList(),
    val weeklySpending: List<WeeklySpendingDataPoint> = emptyList(),
    val weekOffset: Int = 0,
    val currentWeekRange: String = "",
    val isBalanceVisible: Boolean = true,
    val isSwipeEnabled: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardStatsUseCase: GetDashboardStatsUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val updateBalanceUseCase: UpdateBalanceUseCase,
    private val repository: CapitalSyncRepository,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _weekOffset = MutableStateFlow(0)

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.getWelcomeMessage(), // Direct repository call
        getDashboardStatsUseCase(),
        getTransactionsUseCase(),
        combine(
            preferenceRepository.isBalanceVisible(),
            _weekOffset,
            preferenceRepository.isSwipeActionsEnabled()
        ) { isVisible, weekOffset, isSwipeEnabled ->
            Triple(isVisible, weekOffset, isSwipeEnabled)
        }
    ) { welcome, stats, transactions, prefs ->
        val (isVisible, weekOffset, isSwipeEnabled) = prefs
        val weekDates = DateUtils.getWeekDays(weekOffset)
        val weeklySpending = calculateWeeklySpending(transactions, weekDates)

        DashboardUiState(
            welcomeMessage = welcome,
            totalBalance = stats.totalBalance,
            totalIncome = stats.totalIncome,
            totalExpense = stats.totalExpense,
            recentTransactions = transactions.take(5),
            weeklySpending = weeklySpending,
            weekOffset = weekOffset,
            currentWeekRange = DateUtils.formatWeekRange(weekDates),
            isBalanceVisible = isVisible,
            isSwipeEnabled = isSwipeEnabled,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState()
    )

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    fun toggleBalanceVisibility() {
        viewModelScope.launch {
            val currentValue = uiState.value.isBalanceVisible
            preferenceRepository.setBalanceVisible(!currentValue)
        }
    }

    fun moveWeek(delta: Int) {
        val nextOffset = _weekOffset.value + delta
        if (nextOffset <= 0) {
            _weekOffset.value = nextOffset
        }
    }

    fun updateTotalBalance(newBalance: Double) {
        viewModelScope.launch {
            updateBalanceUseCase(newBalance)
        }
    }

    private fun calculateWeeklySpending(
        transactions: List<TransactionEntity>,
        weekDates: List<Long>
    ): List<WeeklySpendingDataPoint> {
        val expenses = transactions.filter { it.type.equals("Expense", ignoreCase = true) }

        return weekDates.map { dayTimestamp ->
            val dayLabel = DateUtils.getDayLabel(dayTimestamp)
            val dayTransactions = expenses.filter { DateUtils.isSameDay(it.dateTimestamp, dayTimestamp) }

            val totalAmount = dayTransactions.sumOf { it.amount }
            val mainCategoryGrouped = dayTransactions
                .groupBy { CategoryUtils.getMainCategoryForSub(it.categoryName) }
                .map { (mainCatName, trans) ->
                    CategorySpendingPoint(
                        categoryName = mainCatName,
                        amount = trans.sumOf { it.amount },
                        iconResId = CategoryUtils.getCategoryIcon(mainCatName)
                    )
                }
                .sortedByDescending { it.amount }

            WeeklySpendingDataPoint(dayLabel, totalAmount, mainCategoryGrouped)
        }
    }
}