package com.nishant.capitalsync.domain.usecase

import com.nishant.capitalsync.data.local.entity.BudgetEntity
import com.nishant.capitalsync.domain.repository.CapitalSyncRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

data class BudgetStatus(
    val budget: BudgetEntity?,
    val spentAmount: Double
)

class GetMonthlyBudgetUseCase @Inject constructor(
    private val repository: CapitalSyncRepository
) {
    operator fun invoke(month: Int, year: Int): Flow<BudgetStatus> {
        val monthYear = String.format(Locale.US, "%02d-%d", month, year)

        return combine(
            repository.getBudgetByMonth(monthYear),
            repository.getAllTransactions()
        ) { budget, transactions ->
            val cal = Calendar.getInstance()
            val spent = transactions.filter { transaction ->
                if (!transaction.type.equals("Expense", ignoreCase = true)) return@filter false

                cal.timeInMillis = transaction.dateTimestamp
                val txMonth = cal.get(Calendar.MONTH) + 1
                val txYear = cal.get(Calendar.YEAR)

                txMonth == month && txYear == year
            }.sumOf { it.amount }

            BudgetStatus(budget, spent)
        }
    }
}