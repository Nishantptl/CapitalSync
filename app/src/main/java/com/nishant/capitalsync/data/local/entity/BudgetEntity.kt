package com.nishant.capitalsync.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey
    val monthYear: String, // format "MM-YYYY"
    val limitAmount: Double = 0.0
)