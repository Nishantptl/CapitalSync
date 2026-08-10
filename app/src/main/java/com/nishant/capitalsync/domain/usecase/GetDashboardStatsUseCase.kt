package com.nishant.capitalsync.domain.usecase

import com.nishant.capitalsync.domain.model.DashboardStats
import com.nishant.capitalsync.domain.repository.CapitalSyncRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDashboardStatsUseCase @Inject constructor(
    private val repository: CapitalSyncRepository
) {
    operator fun invoke(): Flow<DashboardStats> {
        return repository.getAllTransactions().map { transactions ->
            var income = 0.0
            var expense = 0.0

            transactions.forEach { transaction ->
                when {
                    transaction.type.equals("Income", ignoreCase = true) -> income += transaction.amount
                    transaction.type.equals("Expense", ignoreCase = true) -> expense += transaction.amount
                }
            }

            DashboardStats(
                totalBalance = income - expense,
                totalIncome = income,
                totalExpense = expense
            )
        }
    }
}