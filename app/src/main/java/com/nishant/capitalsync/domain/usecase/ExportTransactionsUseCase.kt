package com.nishant.capitalsync.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.nishant.capitalsync.domain.repository.CapitalSyncRepository
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class ExportTransactionsUseCase @Inject constructor(
    private val repository: CapitalSyncRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(): String {
        val transactions = repository.getAllTransactions().first()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.systemDefault())

        val csvBuilder = StringBuilder()
        // Header
        csvBuilder.append("ID,Date,Type,Category,Amount,Note\n")

        transactions.forEach { tx ->
            val formattedDate = dateFormatter.format(Instant.ofEpochMilli(tx.dateTimestamp))
            val formattedAmount = String.format(Locale.US, "%.2f", tx.amount)
            val escapedNote = (tx.note ?: "").replace("\n", " ").replace("\"", "\"\"")

            csvBuilder.append("${tx.id},")
            csvBuilder.append("$formattedDate,")
            csvBuilder.append("\"${tx.type}\",")
            csvBuilder.append("\"${tx.categoryName}\",")
            csvBuilder.append("$formattedAmount,")
            csvBuilder.append("\"$escapedNote\"\n")
        }

        return csvBuilder.toString()
    }
}