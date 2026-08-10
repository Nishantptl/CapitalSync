package com.nishant.capitalsync.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nishant.capitalsync.R
import com.nishant.capitalsync.ui.components.CapitalSyncTopAppBar
import com.nishant.capitalsync.ui.components.TransactionItem
import com.nishant.capitalsync.ui.theme.ExpenseColor
import com.nishant.capitalsync.ui.theme.IncomeColor
import com.nishant.capitalsync.ui.viewmodel.TransactionViewModel
import com.nishant.capitalsync.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToEditTransaction: (Int) -> Unit,
    viewModel: TransactionViewModel = hiltViewModel(),
    scaffoldPadding: PaddingValues = PaddingValues(0.dp)
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CapitalSyncTopAppBar(
                title = stringResource(R.string.transactions_history),
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { innerScaffoldPadding ->
        AnimatedContent(
            targetState = uiState.isLoading,
            label = "TransactionsContent",
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
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Spacer(modifier = Modifier.height(16.dp))
                        TextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChange(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.search_transactions)) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            shape = MaterialTheme.shapes.medium,
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = uiState.selectedFilter == "All",
                                onClick = { viewModel.onFilterChange("All") },
                                label = { Text(stringResource(R.string.filter_all)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                            FilterChip(
                                selected = uiState.selectedFilter == "Income",
                                onClick = { viewModel.onFilterChange("Income") },
                                label = { Text(stringResource(R.string.filter_income)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = IncomeColor,
                                    selectedLabelColor = Color.White
                                )
                            )
                            FilterChip(
                                selected = uiState.selectedFilter == "Expense",
                                onClick = { viewModel.onFilterChange("Expense") },
                                label = { Text(stringResource(R.string.filter_expense)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ExpenseColor,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (uiState.filteredTransactions.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    stringResource(R.string.no_transactions_found),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Gray
                                )
                            }
                        } else {
                            val groupedTransactions = remember(uiState.filteredTransactions) {
                                uiState.filteredTransactions.groupBy {
                                    DateUtils.formatTimestamp(it.dateTimestamp)
                                }
                            }

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = scaffoldPadding.calculateBottomPadding() + 88.dp)
                            ) {
                                groupedTransactions.forEach { (date, transactions) ->
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

                                    items(
                                        transactions,
                                        key = { it.id }
                                    ) { transaction ->
                                        if (uiState.isSwipeEnabled) {
                                            @OptIn(ExperimentalMaterial3Api::class)
                                            val dismissState = rememberSwipeToDismissBoxState(
                                                confirmValueChange = { value ->
                                                    when (value) {
                                                        SwipeToDismissBoxValue.EndToStart -> {
                                                            viewModel.deleteTransaction(transaction)
                                                            Toast.makeText(context, context.getString(R.string.transaction_deleted), Toast.LENGTH_SHORT).show()
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
                                                        contentAlignment = if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) Alignment.CenterStart else Alignment.CenterEnd
                                                    ) {
                                                        val icon = if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) R.drawable.edit_24px else R.drawable.delete_24px
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
    }
}