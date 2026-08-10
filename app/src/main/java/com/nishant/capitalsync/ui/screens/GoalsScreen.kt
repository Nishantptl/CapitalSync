package com.nishant.capitalsync.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nishant.capitalsync.R
import com.nishant.capitalsync.data.local.entity.SavingHabitEntity
import com.nishant.capitalsync.domain.usecase.BudgetStatus
import com.nishant.capitalsync.ui.components.CapitalSyncTopAppBar
import com.nishant.capitalsync.ui.viewmodel.GoalsViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    onNavigateToProfile: () -> Unit,
    viewModel: GoalsViewModel = hiltViewModel(),
    scaffoldPadding: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.uiState.collectAsState()
    var showBudgetDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CapitalSyncTopAppBar(
                title = stringResource(R.string.goals_challenges),
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { innerScaffoldPadding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = innerScaffoldPadding.calculateTopPadding() + 16.dp,
                    end = 20.dp,
                    bottom = scaffoldPadding.calculateBottomPadding() + 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    SectionTitle(stringResource(R.string.monthly_budget_label))
                    BudgetCard(
                        status = uiState.budgetStatus,
                        onEditClick = { showBudgetDialog = true }
                    )
                }

                item {
                    SectionTitle(stringResource(R.string.saving_habits_label))
                }

                items(uiState.habits) { habit ->
                    SavingHabitStreakCard(habit = habit)
                }
            }
        }
    }

    if (showBudgetDialog) {
        BudgetLimitDialog(
            currentLimit = uiState.budgetStatus?.budget?.limitAmount ?: 0.0,
            onDismiss = { showBudgetDialog = false },
            onConfirm = { limit ->
                viewModel.updateBudgetLimit(limit)
                showBudgetDialog = false
            }
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun BudgetCard(status: BudgetStatus?, onEditClick: () -> Unit) {
    val budget = status?.budget
    val spent = status?.spentAmount ?: 0.0
    val limit = budget?.limitAmount ?: 0.0
    val progress = if (limit > 0) (spent / limit).toFloat().coerceIn(0f, 1f) else 0f

    val color = if (spent > limit && limit > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (limit > 0) String.format(Locale.US, "Budget Limit: $%,.2f", limit) else "No Budget Set",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = String.format(Locale.US, "Spent: $%,.2f", spent),
                        style = MaterialTheme.typography.bodyMedium,
                        color = color
                    )
                }
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = if (limit > 0) Icons.Default.Edit else Icons.Default.Add,
                        contentDescription = "Edit Budget"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape),
                color = color,
                trackColor = color.copy(alpha = 0.1f),
            )

            if (limit > 0) {
                val remaining = (limit - spent).coerceAtLeast(0.0)
                Text(
                    text = String.format(Locale.US, "$%,.2f remaining", remaining),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textAlign = TextAlign.End,
                    color = if (remaining == 0.0 && spent > limit) MaterialTheme.colorScheme.error else Color.Gray
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SavingHabitStreakCard(habit: SavingHabitEntity) {
    val today = LocalDate.now()
    // Show last 14 days (2 weeks)
    val daysRange = (0..13).map { today.minusDays(it.toLong()) }.reversed()

    // Calculate current streak
    val currentStreak = calculateStreak(habit.completedDays)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(modifier = Modifier.padding(vertical = 20.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = habit.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = habit.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Surface(
                    color = Color(0xFFFFF2E6), // Light warm orange background
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "🔥 $currentStreak days",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6D00) // Deep fire orange text
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Horizontal scrollable streak
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState(initial = 1000)) // Start scrolled to today
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                daysRange.forEach { date ->
                    val isCompleted = habit.completedDays.contains(date.format(DateTimeFormatter.ISO_LOCAL_DATE))
                    val isToday = date == today
                    val dayName = date.dayOfWeek.name.take(1)
                    val dateNum = date.dayOfMonth.toString()

                    DayCircle(
                        dayName = dayName,
                        dateNum = dateNum,
                        isCompleted = isCompleted,
                        isToday = isToday
                    )
                }
            }
        }
    }
}

@Composable
fun DayCircle(dayName: String, dateNum: String, isCompleted: Boolean, isToday: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    if (isCompleted) MaterialTheme.colorScheme.primary
                    else if (isToday) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (isCompleted) MaterialTheme.colorScheme.onPrimary
                else if (isToday) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
        Text(
            text = dateNum,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isToday) FontWeight.ExtraBold else FontWeight.SemiBold,
            color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun calculateStreak(completedDaysStrings: List<String>): Int {
    if (completedDaysStrings.isEmpty()) return 0
    val today = LocalDate.now()
    val completedDays = completedDaysStrings.map { LocalDate.parse(it) }.toSet()

    // If you logged an expense today, the streak is immediately reset
    if (!completedDays.contains(today)) return 0

    var streak = 0
    var checkDate = today

    // Count backward consecutively from today
    while (completedDays.contains(checkDate)) {
        streak++
        checkDate = checkDate.minusDays(1)
    }
    return streak
}

@Composable
fun BudgetLimitDialog(currentLimit: Double, onDismiss: () -> Unit, onConfirm: (Double) -> Unit) {
    var limitText by remember { mutableStateOf(if (currentLimit > 0) currentLimit.toString() else "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Monthly Budget") },
        text = {
            OutlinedTextField(
                value = limitText,
                onValueChange = { input ->
                    if (input.isEmpty() || (input.count { it == '.' } <= 1 && input.all { it.isDigit() || it == '.' })) {
                        limitText = input
                    }
                },
                label = { Text("Limit Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = {
                val limit = limitText.toDoubleOrNull() ?: 0.0
                onConfirm(limit)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}