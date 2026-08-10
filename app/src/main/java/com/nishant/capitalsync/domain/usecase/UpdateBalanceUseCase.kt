package com.nishant.capitalsync.domain.usecase

import com.nishant.capitalsync.data.local.entity.TransactionEntity
import com.nishant.capitalsync.domain.repository.CapitalSyncRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.math.abs

class UpdateBalanceUseCase @Inject constructor(
    private val repository: CapitalSyncRepository
) {
    suspend operator fun invoke(newBalance: Double) {
        val transactions = repository.getAllTransactions().first()

        var currentIncome = 0.0
        var currentExpense = 0.0

        transactions.forEach { tx ->
            when {
                tx.type.equals("Income", ignoreCase = true) -> currentIncome += tx.amount
                tx.type.equals("Expense", ignoreCase = true) -> currentExpense += tx.amount
            }
        }

        val currentBalance = currentIncome - currentExpense
        val adjustment = newBalance - currentBalance

        if (adjustment == 0.0) return

        val transaction = TransactionEntity(
            amount = abs(adjustment),
            type = if (adjustment > 0) "Income" else "Expense",
            categoryName = "Balance Adjustment",
            dateTimestamp = System.currentTimeMillis(),
            note = "Manual balance update"
        )
        repository.insertTransaction(transaction)
    }
}