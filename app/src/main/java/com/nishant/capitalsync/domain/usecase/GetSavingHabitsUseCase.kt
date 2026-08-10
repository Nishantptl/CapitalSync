package com.nishant.capitalsync.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.nishant.capitalsync.data.local.entity.SavingHabitEntity
import com.nishant.capitalsync.data.local.entity.TransactionEntity
import com.nishant.capitalsync.domain.repository.CapitalSyncRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetSavingHabitsUseCase @Inject constructor(
    private val repository: CapitalSyncRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(): Flow<List<SavingHabitEntity>> {
        return combine(
            repository.getAllHabits().onEach { habits ->
                if (habits.isEmpty()) {
                    repository.insertHabit(
                        SavingHabitEntity(
                            title = "No Spend Streak",
                            description = "Keep your daily expenses at zero to build a streak!"
                        )
                    )
                }
            },
            repository.getAllTransactions()
        ) { habits, transactions ->
            habits.map { habit ->
                if (habit.title == "No Spend Streak" || habit.title == "Daily Savings") {
                    calculateNoSpendDays(habit, transactions)
                } else {
                    habit
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateNoSpendDays(
        habit: SavingHabitEntity,
        transactions: List<TransactionEntity>
    ): SavingHabitEntity {
        val zoneId = ZoneId.systemDefault()
        val expensesByDate = transactions
            .filter { it.type.equals("Expense", ignoreCase = true) }
            .groupBy {
                Instant.ofEpochMilli(it.dateTimestamp)
                    .atZone(zoneId)
                    .toLocalDate()
                    .format(DateTimeFormatter.ISO_LOCAL_DATE)
            }

        val today = LocalDate.now()
        val allTransactionDates = transactions.map {
            Instant.ofEpochMilli(it.dateTimestamp).atZone(zoneId).toLocalDate()
        }
        val earliestDate = allTransactionDates.minOrNull() ?: return habit.copy(completedDays = emptyList())

        val completedDays = mutableListOf<String>()
        var checkDate = earliestDate

        while (!checkDate.isAfter(today)) {
            val dateStr = checkDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
            if (!expensesByDate.containsKey(dateStr)) {
                completedDays.add(dateStr)
            }
            checkDate = checkDate.plusDays(1)
        }

        return habit.copy(
            title = "No Spend Streak",
            description = "No expenses recorded on these days!",
            completedDays = completedDays
        )
    }
}