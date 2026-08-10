package com.nishant.capitalsync.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    @ColumnInfo(name = "icon_res_id")
    val iconResId: Int = 0,
    @ColumnInfo(name = "is_income_category")
    val isIncomeCategory: Boolean = false
)