package com.nishant.capitalsync.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nishant.capitalsync.R
import com.nishant.capitalsync.data.local.entity.TransactionEntity
import com.nishant.capitalsync.ui.components.CapitalSyncHeading
import com.nishant.capitalsync.ui.components.CapitalSyncText
import com.nishant.capitalsync.ui.components.CapitalSyncTopAppBar
import com.nishant.capitalsync.ui.components.TransactionItem
import com.nishant.capitalsync.ui.components.WeeklySpendingChart
import com.nishant.capitalsync.ui.theme.AppTheme
import com.nishant.capitalsync.ui.theme.ExpenseColor
import com.nishant.capitalsync.ui.theme.IncomeColor
import com.nishant.capitalsync.ui.viewmodel.CategorySpendingPoint
import com.nishant.capitalsync.ui.viewmodel.DashboardUiState
import com.nishant.capitalsync.ui.viewmodel.DashboardViewModel
import com.nishant.capitalsync.ui.viewmodel.WeeklySpendingDataPoint
import com.nishant.capitalsync.utils.DateUtils
import java.util.Locale

@Composable
fun DashboardScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    onNavigateToEditTransaction: (Int) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
    scaffoldPadding: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.uiState.collectAsState()

    DashboardContent(
        uiState = uiState,
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToTransactions = onNavigateToTransactions,
        onNavigateToEditTransaction = onNavigateToEditTransaction,
        onToggleVisibility = { viewModel.toggleBalanceVisibility() },
        onDeleteTransaction = { viewModel.deleteTransaction(it) },
        onPreviousWeek = { viewModel.moveWeek(-1) },
        onNextWeek = { viewModel.moveWeek(1) },
        scaffoldPadding = scaffoldPadding
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    uiState: DashboardUiState,
    onNavigateToProfile: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    onNavigateToEditTransaction: (Int) -> Unit,
    onToggleVisibility: () -> Unit,
    onDeleteTransaction: (TransactionEntity) -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
    scaffoldPadding: PaddingValues = PaddingValues(0.dp)
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CapitalSyncTopAppBar(
                title = uiState.welcomeMessage,
                subtitle = stringResource(R.string.hello_comma),
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { innerScaffoldPadding ->
        AnimatedContent(
            targetState = uiState.isLoading,
            label = "DashboardContent",
            modifier = Modifier.padding(
                top = innerScaffoldPadding.calculateTopPadding(),
                bottom = 0.dp
            )
        ) { isLoading ->
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 16.dp,
                        end = 20.dp,
                        bottom = scaffoldPadding.calculateBottomPadding() + 20.dp
                    )
                ) {
                    item {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize(),
                            shape = MaterialTheme.shapes.extraLarge,
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    CapitalSyncText(
                                        stringResource(R.string.total_balance),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        painter = painterResource(
                                            id = if (uiState.isBalanceVisible) R.drawable.visibility_24px else R.drawable.visibility_off_24px
                                        ),
                                        contentDescription = stringResource(R.string.toggle_balance_visibility),
                                        tint = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f),
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clickable { onToggleVisibility() }
                                    )
                                }

                                AnimatedVisibility(
                                    visible = uiState.isBalanceVisible,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        CapitalSyncText(
                                            String.format(Locale.US, "$%,.2f", uiState.totalBalance),
                                            style = MaterialTheme.typography.displayMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onSecondary,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            StatsCard(
                                title = stringResource(R.string.income),
                                amount = uiState.totalIncome,
                                icon = R.drawable.arrow_drop_up_24px,
                                color = IncomeColor,
                                modifier = Modifier.weight(1f)
                            )

                            StatsCard(
                                title = stringResource(R.string.expenses),
                                amount = uiState.totalExpense,
                                icon = R.drawable.arrow_drop_down_24px,
                                color = ExpenseColor,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        WeeklySpendingChart(
                            spendingData = uiState.weeklySpending,
                            weekRange = uiState.currentWeekRange,
                            onPreviousWeek = onPreviousWeek,
                            onNextWeek = onNextWeek,
                            isNextEnabled = uiState.weekOffset < 0,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CapitalSyncHeading(
                                stringResource(R.string.recent_activity),
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = onNavigateToTransactions) {
                                Text(
                                    stringResource(R.string.see_all),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }

                    if (uiState.recentTransactions.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CapitalSyncText(stringResource(R.string.no_transactions_yet), color = Color.Gray)
                            }
                        }
                    } else {
                        val groupedRecent = uiState.recentTransactions.groupBy {
                            DateUtils.formatTimestamp(it.dateTimestamp)
                        }

                        groupedRecent.forEach { (date, transactions) ->
                            @OptIn(ExperimentalFoundationApi::class)
                            stickyHeader {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                                ) {
                                    Text(
                                        text = date.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Gray.copy(alpha = 0.6f),
                                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                                    )
                                }
                            }

                            items(transactions, key = { it.id }) { transaction ->
                                if (uiState.isSwipeEnabled) {
                                    val dismissState = rememberSwipeToDismissBoxState(
                                        confirmValueChange = { value ->
                                            when (value) {
                                                SwipeToDismissBoxValue.EndToStart -> {
                                                    onDeleteTransaction(transaction)
                                                    Toast.makeText(
                                                        context,
                                                        context.getString(R.string.transaction_deleted),
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    true
                                                }
                                                SwipeToDismissBoxValue.StartToEnd -> {
                                                    onNavigateToEditTransaction(transaction.id)
                                                    false
                                                }
                                                else -> false
                                            }
                                        }
                                    )

                                    SwipeToDismissBox(
                                        state = dismissState,
                                        backgroundContent = {
                                            val color = when (dismissState.dismissDirection) {
                                                SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.secondary
                                                SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
                                                else -> Color.Transparent
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(vertical = 4.dp)
                                                    .background(color, MaterialTheme.shapes.medium),
                                                contentAlignment = if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
                                                    Alignment.CenterStart
                                                } else {
                                                    Alignment.CenterEnd
                                                }
                                            ) {
                                                val icon = if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
                                                    R.drawable.edit_24px
                                                } else {
                                                    R.drawable.delete_24px
                                                }
                                                Icon(
                                                    painter = painterResource(id = icon),
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.padding(horizontal = 20.dp)
                                                )
                                            }
                                        },
                                        modifier = Modifier.animateItem()
                                    ) {
                                        TransactionItem(
                                            transaction = transaction,
                                            onClick = { onNavigateToEditTransaction(transaction.id) }
                                        )
                                    }
                                } else {
                                    TransactionItem(
                                        transaction = transaction,
                                        onClick = { onNavigateToEditTransaction(transaction.id) },
                                        modifier = Modifier.animateItem()
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    amount: Double,
    icon: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = color.copy(alpha = 0.15f),
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text(
                    String.format(Locale.US, "$%,.0f", amount),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = color.darken(0.2f)
                )
            }
        }
    }
}

fun Color.darken(factor: Float): Color {
    return Color(
        red = this.red * (1 - factor),
        green = this.green * (1 - factor),
        blue = this.blue * (1 - factor),
        alpha = this.alpha
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    AppTheme {
        DashboardContent(
            uiState = DashboardUiState(
                totalBalance = 1250.50,
                totalIncome = 2000.0,
                totalExpense = 749.50,
                recentTransactions = listOf(
                    TransactionEntity(amount = 50.0, type = "Expense", categoryName = "Food", dateTimestamp = System.currentTimeMillis()),
                    TransactionEntity(amount = 1000.0, type = "Income", categoryName = "Salary", dateTimestamp = System.currentTimeMillis())
                ),
                weeklySpending = listOf(
                    WeeklySpendingDataPoint("Mon", 20.0, listOf(CategorySpendingPoint("Food", 20.0))),
                    WeeklySpendingDataPoint("Tue", 45.0, listOf(CategorySpendingPoint("Food", 25.0), CategorySpendingPoint("Transport", 20.0))),
                    WeeklySpendingDataPoint("Wed", 15.0, listOf(CategorySpendingPoint("Shopping", 15.0))),
                    WeeklySpendingDataPoint("Thu", 80.0, listOf(CategorySpendingPoint("Food", 30.0), CategorySpendingPoint("Rent", 50.0))),
                    WeeklySpendingDataPoint("Fri", 30.0, listOf(CategorySpendingPoint("Transport", 30.0))),
                    WeeklySpendingDataPoint("Sat", 60.0, listOf(CategorySpendingPoint("Food", 40.0), CategorySpendingPoint("Entertainment", 20.0))),
                    WeeklySpendingDataPoint("Sun", 25.0, listOf(CategorySpendingPoint("Food", 25.0)))
                ),
                currentWeekRange = "5th - 11th Apr 26",
                isLoading = false
            ),
            onNavigateToProfile = {},
            onNavigateToTransactions = {},
            onNavigateToEditTransaction = { _ -> },
            onToggleVisibility = {},
            onDeleteTransaction = { _ -> },
            onPreviousWeek = {},
            onNextWeek = {},
        )
    }
}