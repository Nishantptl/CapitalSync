package com.nishant.capitalsync.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    @ColumnInfo(name = "target_amount")
    val targetAmount: Double = 0.0,
    @ColumnInfo(name = "current_amount")
    val currentAmount: Double = 0.0,
    @ColumnInfo(name = "deadline_timestamp")
    val deadlineTimestamp: Long = 0L,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true
)