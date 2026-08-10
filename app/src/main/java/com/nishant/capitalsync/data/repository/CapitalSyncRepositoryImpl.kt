package com.nishant.capitalsync.data.repository

import com.nishant.capitalsync.data.local.dao.CapitalSyncDao
import com.nishant.capitalsync.data.local.entity.BudgetEntity
import com.nishant.capitalsync.data.local.entity.CategoryEntity
import com.nishant.capitalsync.data.local.entity.GoalEntity
import com.nishant.capitalsync.data.local.entity.SavingHabitEntity
import com.nishant.capitalsync.data.local.entity.TransactionEntity
import com.nishant.capitalsync.domain.repository.CapitalSyncRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CapitalSyncRepositoryImpl @Inject constructor(
    private val dao: CapitalSyncDao
) : CapitalSyncRepository {

    override fun getWelcomeMessage(): Flow<String> = flowOf("Welcome to CapitalSync!")

    // Transactions
    override fun getAllTransactions(): Flow<List<TransactionEntity>> = dao.getAllTransactions()
    override suspend fun getTransactionById(id: Int): TransactionEntity? = dao.getTransactionById(id)
    override suspend fun insertTransaction(transaction: TransactionEntity) = dao.insertTransaction(transaction)
    override suspend fun deleteTransaction(transaction: TransactionEntity) = dao.deleteTransaction(transaction)

    // Categories
    override fun getAllCategories(): Flow<List<CategoryEntity>> = dao.getAllCategories()
    override suspend fun insertCategory(category: CategoryEntity) = dao.insertCategory(category)

    // Goals
    override fun getActiveGoals(): Flow<List<GoalEntity>> = dao.getActiveGoals()
    override fun getAllGoals(): Flow<List<GoalEntity>> = dao.getAllGoals()
    override suspend fun insertGoal(goal: GoalEntity) = dao.insertGoal(goal)
    override suspend fun updateGoal(goal: GoalEntity) = dao.updateGoal(goal)

    // Habits
    override fun getAllHabits(): Flow<List<SavingHabitEntity>> = dao.getAllHabits()
    override suspend fun insertHabit(habit: SavingHabitEntity) = dao.insertHabit(habit)
    override suspend fun updateHabit(habit: SavingHabitEntity) = dao.updateHabit(habit)

    // Budget
    override fun getBudgetByMonth(monthYear: String): Flow<BudgetEntity?> = dao.getBudgetByMonth(monthYear)
    override suspend fun insertBudget(budget: BudgetEntity) = dao.insertBudget(budget)
}