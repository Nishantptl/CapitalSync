package com.nishant.capitalsync.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nishant.capitalsync.data.local.entity.BudgetEntity
import com.nishant.capitalsync.data.local.entity.CategoryEntity
import com.nishant.capitalsync.data.local.entity.GoalEntity
import com.nishant.capitalsync.data.local.entity.SavingHabitEntity
import com.nishant.capitalsync.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CapitalSyncDao {

    // Transactions
    @Query("SELECT * FROM transactions ORDER BY date_timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    // Categories
    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    // Goals
    @Query("SELECT * FROM goals WHERE is_active = 1")
    fun getActiveGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals")
    fun getAllGoals(): Flow<List<GoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity)

    @Update
    suspend fun updateGoal(goal: GoalEntity)

    // Habits
    @Query("SELECT * FROM saving_habits")
    fun getAllHabits(): Flow<List<SavingHabitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: SavingHabitEntity)

    @Update
    suspend fun updateHabit(habit: SavingHabitEntity)

    // Budget
    @Query("SELECT * FROM budgets WHERE monthYear = :monthYear")
    fun getBudgetByMonth(monthYear: String): Flow<BudgetEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity)
}