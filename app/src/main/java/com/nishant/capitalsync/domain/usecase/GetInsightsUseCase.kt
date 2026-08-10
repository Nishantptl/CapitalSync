package com.nishant.capitalsync.domain.usecase

import com.nishant.capitalsync.data.local.entity.TransactionEntity
import com.nishant.capitalsync.domain.repository.CapitalSyncRepository
import com.nishant.capitalsync.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

data class FinancialInsights(
    val highestSpendingCategory: String = "None",
    val highestSpendingAmount: Double = 0.0,
    val totalSpentBalance: Double = 0.0,
    val totalSpentThisMonth: Double = 0.0,
    val thisWeekSpent: Double = 0.0,
    val lastWeekSpent: Double = 0.0,
    val weeklyTrend: List<Pair<String, Double>> = emptyList(),
    val spendingByCategory: Map<String, Double> = emptyMap(),
    val frequentTransactionType: String = "None",
    val projectedMonthlySpent: Double = 0.0,
    val weekdaySpending: Double = 0.0,
    val weekendSpending: Double = 0.0,
    val largestTransaction: Double = 0.0,
    val avgTransactionAmount: Double = 0.0
)

class GetInsightsUseCase @Inject constructor(
    private val repository: CapitalSyncRepository
) {
    operator fun invoke(): Flow<FinancialInsights> {
        return repository.getAllTransactions().map { transactions ->
            val expenses = transactions.filter { it.type.equals("Expense", ignoreCase = true) }
            val categories = expenses.groupBy { it.categoryName }
            val highest = categories.maxByOrNull { it.value.sumOf { t -> t.amount } }

            // Weeks calculation
            val thisWeekDays = DateUtils.getWeekDays(0)
            val lastWeekDays = DateUtils.getWeekDays(-1)

            val thisWeekStart = thisWeekDays.firstOrNull() ?: 0L
            val lastWeekStart = lastWeekDays.firstOrNull() ?: 0L

            val thisWeekTransactions = expenses.filter { it.dateTimestamp >= thisWeekStart }
            val lastWeekTransactions = expenses.filter {
                it.dateTimestamp >= lastWeekStart && it.dateTimestamp < thisWeekStart
            }

            val thisWeekSpent = thisWeekTransactions.sumOf { it.amount }
            val lastWeekSpent = lastWeekTransactions.sumOf { it.amount }

            // Weekly Trend (Day label to amount for this week)
            val weeklyTrend = thisWeekDays.map { dayTimestamp ->
                val dayLabel = DateUtils.getDayLabel(dayTimestamp)
                val dayAmount = thisWeekTransactions
                    .filter { DateUtils.isSameDay(it.dateTimestamp, dayTimestamp) }
                    .sumOf { it.amount }
                dayLabel to dayAmount
            }

            // Spending By Category
            val spendingByCategory = categories.mapValues { entry -> entry.value.sumOf { it.amount } }

            // Monthly Trend (Total Spent This Month)
            val currentMonthCalendar = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val startOfMonth = currentMonthCalendar.timeInMillis
            val totalSpentThisMonth = expenses.filter { it.dateTimestamp >= startOfMonth }.sumOf { it.amount }

            // Frequent Category (by transaction count)
            val mostFrequent = categories.maxByOrNull { it.value.size }

            // Metrics
            val largestTransaction = expenses.maxOfOrNull { it.amount } ?: 0.0
            val avgTransactionAmount = if (expenses.isNotEmpty()) expenses.sumOf { it.amount } / expenses.size else 0.0

            // Weekday vs Weekend
            var weekdaySpending = 0.0
            var weekendSpending = 0.0
            val cal = Calendar.getInstance()

            expenses.forEach { transaction ->
                cal.timeInMillis = transaction.dateTimestamp
                val day = cal.get(Calendar.DAY_OF_WEEK)
                if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
                    weekendSpending += transaction.amount
                } else {
                    weekdaySpending += transaction.amount
                }
            }

            // Monthly Projection
            val nowCal = Calendar.getInstance()
            val dayOfMonth = nowCal.get(Calendar.DAY_OF_MONTH)
            val daysInMonth = nowCal.getActualMaximum(Calendar.DAY_OF_MONTH)
            val projectedMonthlySpent = if (dayOfMonth > 0) (totalSpentThisMonth / dayOfMonth) * daysInMonth else totalSpentThisMonth

            FinancialInsights(
                highestSpendingCategory = highest?.key ?: "None",
                highestSpendingAmount = highest?.value?.sumOf { it.amount } ?: 0.0,
                totalSpentBalance = expenses.sumOf { it.amount },
                totalSpentThisMonth = totalSpentThisMonth,
                thisWeekSpent = thisWeekSpent,
                lastWeekSpent = lastWeekSpent,
                weeklyTrend = weeklyTrend,
                spendingByCategory = spendingByCategory,
                frequentTransactionType = mostFrequent?.key ?: "None",
                projectedMonthlySpent = projectedMonthlySpent,
                weekdaySpending = weekdaySpending,
                weekendSpending = weekendSpending,
                largestTransaction = largestTransaction,
                avgTransactionAmount = avgTransactionAmount
            )
        }
    }
}