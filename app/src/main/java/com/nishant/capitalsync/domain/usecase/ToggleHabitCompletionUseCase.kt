package com.nishant.capitalsync.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.nishant.capitalsync.data.local.entity.SavingHabitEntity
import com.nishant.capitalsync.domain.repository.CapitalSyncRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ToggleHabitCompletionUseCase @Inject constructor(
    private val repository: CapitalSyncRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(habit: SavingHabitEntity, date: LocalDate) {
        val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val updatedCompletedDays = if (dateString in habit.completedDays) {
            habit.completedDays - dateString
        } else {
            habit.completedDays + dateString
        }

        repository.updateHabit(habit.copy(completedDays = updatedCompletedDays))
    }
}