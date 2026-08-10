package com.nishant.capitalsync.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nishant.capitalsync.data.local.dao.CapitalSyncDao
import com.nishant.capitalsync.data.local.entity.BudgetEntity
import com.nishant.capitalsync.data.local.entity.CategoryEntity
import com.nishant.capitalsync.data.local.entity.GoalEntity
import com.nishant.capitalsync.data.local.entity.SavingHabitEntity
import com.nishant.capitalsync.data.local.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        CategoryEntity::class,
        GoalEntity::class,
        SavingHabitEntity::class,
        BudgetEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CapitalSyncDatabase : RoomDatabase() {
    abstract val dao: CapitalSyncDao

    companion object {
        const val DATABASE_NAME = "capitalsync_secure.db"
    }
}