package com.nishant.capitalsync.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double = 0.0,
    val type: String = "Expense",
    @ColumnInfo(name = "category_name")
    val categoryName: String = "",
    @ColumnInfo(name = "date_timestamp")
    val dateTimestamp: Long = System.currentTimeMillis(),
    val note: String? = null
)