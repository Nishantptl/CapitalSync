package com.nishant.capitalsync.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saving_habits")
data class SavingHabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val completedDays: List<String> = emptyList() // List of dates in YYYY-MM-DD format
)