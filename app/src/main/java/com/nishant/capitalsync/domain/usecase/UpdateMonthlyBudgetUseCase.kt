package com.nishant.capitalsync.domain.usecase

import com.nishant.capitalsync.data.local.entity.BudgetEntity
import com.nishant.capitalsync.domain.repository.CapitalSyncRepository
import java.util.Locale
import javax.inject.Inject

class UpdateMonthlyBudgetUseCase @Inject constructor(
    private val repository: CapitalSyncRepository
) {
    suspend operator fun invoke(month: Int, year: Int, limit: Double) {
        val monthYear = String.format(Locale.US, "%02d-%d", month, year)
        repository.insertBudget(BudgetEntity(monthYear, limit))
    }
}