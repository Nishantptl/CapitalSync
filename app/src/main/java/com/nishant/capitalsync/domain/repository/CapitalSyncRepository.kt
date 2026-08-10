package com.nishant.capitalsync.domain.repository

import com.nishant.capitalsync.data.local.entity.BudgetEntity
import com.nishant.capitalsync.data.local.entity.CategoryEntity
import com.nishant.capitalsync.data.local.entity.GoalEntity
import com.nishant.capitalsync.data.local.entity.SavingHabitEntity
import com.nishant.capitalsync.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface CapitalSyncRepository {
    fun getWelcomeMessage(): Flow<String>

    // Transactions
    fun getAllTransactions(): Flow<List<TransactionEntity>>
    suspend fun getTransactionById(id: Int): TransactionEntity?
    suspend fun insertTransaction(transaction: TransactionEntity)
    suspend fun deleteTransaction(transaction: TransactionEntity)

    // Categories
    fun getAllCategories(): Flow<List<CategoryEntity>>
    suspend fun insertCategory(category: CategoryEntity)

    // Goals
    fun getActiveGoals(): Flow<List<GoalEntity>>
    fun getAllGoals(): Flow<List<GoalEntity>>
    suspend fun insertGoal(goal: GoalEntity)
    suspend fun updateGoal(goal: GoalEntity)

    // Habits
    fun getAllHabits(): Flow<List<SavingHabitEntity>>
    suspend fun insertHabit(habit: SavingHabitEntity)
    suspend fun updateHabit(habit: SavingHabitEntity)

    // Budget
    fun getBudgetByMonth(monthYear: String): Flow<BudgetEntity?>
    suspend fun insertBudget(budget: BudgetEntity)
}