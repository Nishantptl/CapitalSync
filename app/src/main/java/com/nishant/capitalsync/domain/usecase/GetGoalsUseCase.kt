package com.nishant.capitalsync.domain.usecase

import com.nishant.capitalsync.data.local.entity.GoalEntity
import com.nishant.capitalsync.domain.repository.CapitalSyncRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGoalsUseCase @Inject constructor(
    private val repository: CapitalSyncRepository
) {
    operator fun invoke(): Flow<List<GoalEntity>> = repository.getAllGoals()
}