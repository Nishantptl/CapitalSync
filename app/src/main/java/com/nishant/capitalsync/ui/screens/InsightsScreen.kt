package com.nishant.capitalsync.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nishant.capitalsync.R
import com.nishant.capitalsync.domain.usecase.FinancialInsights
import com.nishant.capitalsync.ui.components.CapitalSyncTopAppBar
import com.nishant.capitalsync.ui.viewmodel.InsightsViewModel
import java.util.Locale
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    onNavigateToProfile: () -> Unit,
    viewModel: InsightsViewModel = hiltViewModel(),
    scaffoldPadding: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CapitalSyncTopAppBar(
                title = stringResource(R.string.financial_insights),
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { innerScaffoldPadding ->
        AnimatedContent(
            targetState = uiState.isLoading,
            label = "InsightsLoadingAnimation",
            modifier = Modifier.padding(
                top = innerScaffoldPadding.calculateTopPadding(),
                bottom = 0.dp
            )
        ) { isLoading ->
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.insights.highestSpendingCategory == "None") {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.search_insights_24px),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(stringResource(R.string.no_insights_available), color = Color.Gray)
                        Text(
                            stringResource(R.string.add_transactions_to_see_insights),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                InsightsList(
                    insights = uiState.insights,
                    scaffoldPadding = scaffoldPadding
                )
            }
        }
    }
}

@Composable
fun InsightsList(
    insights: FinancialInsights,
    scaffoldPadding: PaddingValues
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 16.dp,
            end = 20.dp,
            bottom = scaffoldPadding.calculateBottomPadding() + 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Condensed Hero: Monthly Overview & Projection
        item {
            MonthlyOverviewCard(insights)
        }

        // 2. Weekday vs Weekend Comparison
        item {
            WeekdayVsWeekendCard(insights)
        }

        // 3. Comparison: This Week vs Last Week
        item {
            WeeklyComparisonCard(insights)
        }

        // 4. Data Grid: Largest, Average, Frequent
        item {
            InsightsDataGrid(insights)
        }

        // 5. Weekly Trend
        item {
            WeeklyTrendCard(insights)
        }

        // 6. Category Breakdown
        item {
            CategoryBreakdownCard(insights)
        }
    }
}

@Composable
fun MonthlyOverviewCard(insights: FinancialInsights) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.monthly_projection_title),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Text(
                        String.format(Locale.US, "$%,.2f", insights.projectedMonthlySpent),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        "Est. End",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        stringResource(R.string.highest_spending_category),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                    )
                    Text(
                        insights.highestSpendingCategory,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Total Spent",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                    )
                    Text(
                        String.format(Locale.US, "$%,.0f", insights.totalSpentThisMonth),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun WeekdayVsWeekendCard(insights: FinancialInsights) {
    val total = insights.weekdaySpending + insights.weekendSpending
    val weekdayPercent = if (total > 0) (insights.weekdaySpending / total).toFloat() else 0.5f

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.weekday_vs_weekend),
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(weekdayPercent.coerceAtLeast(0.01f))
                        .background(MaterialTheme.colorScheme.primary)
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight((1f - weekdayPercent).coerceAtLeast(0.01f))
                        .background(MaterialTheme.colorScheme.tertiary)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Mon-Fri", style = MaterialTheme.typography.labelSmall)
                    }
                    Text(String.format(Locale.US, "$%,.0f", insights.weekdaySpending), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Sat-Sun", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.tertiary))
                    }
                    Text(String.format(Locale.US, "$%,.0f", insights.weekendSpending), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun InsightsDataGrid(insights: FinancialInsights) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DataSmallCard(
                title = stringResource(R.string.largest_purchase),
                value = String.format(Locale.US, "$%,.0f", insights.largestTransaction),
                icon = R.drawable.star_shine_24px,
                modifier = Modifier.weight(1f),
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
            DataSmallCard(
                title = stringResource(R.string.avg_transaction),
                value = String.format(Locale.US, "$%,.0f", insights.avgTransactionAmount),
                icon = R.drawable.search_insights_24px,
                modifier = Modifier.weight(1f),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        }
        DataSmallCard(
            title = stringResource(R.string.frequent_spending_hub),
            value = insights.frequentTransactionType,
            icon = R.drawable.list_24px,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            isHorizontal = true
        )
    }
}

@Composable
fun DataSmallCard(
    title: String,
    value: String,
    icon: Int,
    modifier: Modifier = Modifier,
    containerColor: Color,
    isHorizontal: Boolean = false
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor.copy(alpha = 0.3f))
    ) {
        if (isHorizontal) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(title, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold)
                }
            }
        } else {
            Column(modifier = Modifier.padding(12.dp)) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(title, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
fun WeeklyComparisonCard(insights: FinancialInsights) {
    val diff = insights.thisWeekSpent - insights.lastWeekSpent
    val percentDiff = if (insights.lastWeekSpent > 0) (diff / insights.lastWeekSpent) * 100 else 0.0
    val isMore = diff > 0
    val isLess = diff < 0

    val trendContainerColor = if (isMore) {
        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f)
    } else if (isLess) {
        Color(0xFF4CAF50).copy(alpha = 0.15f)
    } else {
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
    }

    val trendOnContainerColor = if (isMore) {
        MaterialTheme.colorScheme.onErrorContainer
    } else if (isLess) {
        Color(0xFF2E7D32)
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.this_week_vs_last_week),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        String.format(Locale.US, "$%,.2f", insights.thisWeekSpent),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        stringResource(R.string.this_week),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(trendContainerColor)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isMore) "↑" else if (isLess) "↓" else "•",
                            fontWeight = FontWeight.ExtraBold,
                            color = trendOnContainerColor,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = String.format(Locale.US, "%.1f%%", abs(percentDiff)),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = trendOnContainerColor
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        String.format(Locale.US, "$%,.2f", insights.lastWeekSpent),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Text(stringResource(R.string.last_week), style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun WeeklyTrendCard(insights: FinancialInsights) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.daily_spending_trend),
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                val maxAmount = insights.weeklyTrend.maxByOrNull { it.second }?.second ?: 1.0
                insights.weeklyTrend.forEach { (day, amount) ->
                    val heightFactor = if (maxAmount > 0) (amount / maxAmount).toFloat() else 0f
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(80.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .fillMaxHeight(heightFactor.coerceAtLeast(0.05f))
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            day.take(1),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryBreakdownCard(insights: FinancialInsights) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.spending_by_category),
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            val total = insights.totalSpentBalance
            insights.spendingByCategory.toList()
                .sortedByDescending { it.second }
                .take(5)
                .forEach { (category, amount) ->
                    val percentage = if (total > 0) (amount / total).toFloat() else 0f
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                category,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                String.format(Locale.US, "$%,.0f", amount),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { percentage },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        )
                    }
                }
        }
    }
}