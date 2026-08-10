package com.nishant.capitalsync.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nishant.capitalsync.data.local.entity.TransactionEntity
import com.nishant.capitalsync.ui.theme.ExpenseColor
import com.nishant.capitalsync.ui.theme.IncomeColor
import com.nishant.capitalsync.utils.CategoryUtils
import java.util.Locale

@Composable
fun TransactionItem(
    transaction: TransactionEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val themeColor = MaterialTheme.colorScheme.primary
    val displayAmountColor = if (transaction.type == "Income") IncomeColor else ExpenseColor
    val prefix = if (transaction.type == "Income") "+" else "-"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = themeColor.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = CategoryUtils.getCategoryIcon(transaction.categoryName)),
                        contentDescription = null,
                        tint = themeColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                val mainCat = CategoryUtils.getMainCategoryForSub(transaction.categoryName)
                val hasSubCategory = mainCat != transaction.categoryName && transaction.categoryName.isNotBlank()

                CapitalSyncText(
                    text = if (hasSubCategory) transaction.categoryName else mainCat,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                if (hasSubCategory) {
                    CapitalSyncText(
                        text = mainCat,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            CapitalSyncText(
                text = String.format(Locale.US, "%s$%,.2f", prefix, transaction.amount),
                fontWeight = FontWeight.ExtraBold,
                color = displayAmountColor,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}