package com.nishant.capitalsync.domain.usecase

import com.nishant.capitalsync.data.local.entity.TransactionEntity
import com.nishant.capitalsync.domain.repository.CapitalSyncRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: CapitalSyncRepository
) {
    operator fun invoke(): Flow<List<TransactionEntity>> = repository.getAllTransactions()
}