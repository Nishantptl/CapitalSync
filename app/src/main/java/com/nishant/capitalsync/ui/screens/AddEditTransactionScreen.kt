package com.nishant.capitalsync.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nishant.capitalsync.R
import com.nishant.capitalsync.ui.components.SimpleFlowRow
import com.nishant.capitalsync.ui.theme.ExpenseColor
import com.nishant.capitalsync.ui.theme.IncomeColor
import com.nishant.capitalsync.ui.viewmodel.TransactionViewModel
import com.nishant.capitalsync.utils.CategoryUtils
import com.nishant.capitalsync.utils.DateUtils
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionScreen(
    transactionId: Int? = null,
    onBack: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.addEditState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(transactionId) {
        viewModel.loadTransaction(transactionId)
        if (transactionId == null) {
            focusRequester.requestFocus()
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.selectedDate,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= System.currentTimeMillis()
                }

                override fun isSelectableYear(year: Int): Boolean {
                    return year <= Calendar.getInstance().get(Calendar.YEAR)
                }
            }
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onDateChange(datePickerState.selectedDateMillis ?: System.currentTimeMillis())
                    showDatePicker = false
                }) {
                    Text(stringResource(R.string.ok_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val themeColor = MaterialTheme.colorScheme.primary
                    AnimatedContent(targetState = state.isEditing, label = "TitleAnimation") { isEditing ->
                        Text(
                            if (!isEditing) stringResource(R.string.add_transaction) else stringResource(R.string.edit_transaction),
                            fontWeight = FontWeight.Bold,
                            color = themeColor
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back_ios_24px),
                            contentDescription = stringResource(R.string.back),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                actions = {
                    if (state.isEditing) {
                        var showDeleteDialog by remember { mutableStateOf(false) }
                        if (showDeleteDialog) {
                            AlertDialog(
                                onDismissRequest = { showDeleteDialog = false },
                                title = { Text(stringResource(R.string.delete_transaction_title)) },
                                text = { Text(stringResource(R.string.delete_transaction_message)) },
                                confirmButton = {
                                    TextButton(onClick = {
                                        viewModel.deleteTransaction()
                                        Toast.makeText(context, context.getString(R.string.transaction_deleted), Toast.LENGTH_SHORT).show()
                                        onBack()
                                    }) {
                                        Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showDeleteDialog = false }) {
                                        Text(stringResource(R.string.cancel))
                                    }
                                }
                            )
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.delete_24px),
                                contentDescription = stringResource(R.string.delete),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(
                    selected = state.isExpense,
                    onClick = { viewModel.onTypeChange(true) },
                    label = { Text(stringResource(R.string.filter_expense)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ExpenseColor,
                        selectedLabelColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                FilterChip(
                    selected = !state.isExpense,
                    onClick = { viewModel.onTypeChange(false) },
                    label = { Text(stringResource(R.string.filter_income)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = IncomeColor,
                        selectedLabelColor = Color.White
                    )
                )
            }

            val themeColor = MaterialTheme.colorScheme.primary
            val chipContainerColor = MaterialTheme.colorScheme.primary
            val onChipContainerColor = MaterialTheme.colorScheme.onPrimary
            val chipBorderColor = MaterialTheme.colorScheme.onPrimary

            Text(
                stringResource(R.string.amount),
                style = MaterialTheme.typography.labelLarge,
                color = themeColor
            )
            OutlinedTextField(
                value = state.amount,
                onValueChange = { viewModel.onAmountChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = { Text(stringResource(R.string.amount_placeholder)) },
                prefix = { Text("$ ", color = themeColor, fontWeight = FontWeight.Bold) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = themeColor,
                    focusedLabelColor = themeColor,
                    cursorColor = themeColor
                )
            )

            Text(
                stringResource(R.string.category),
                style = MaterialTheme.typography.labelLarge,
                color = themeColor
            )
            val mainCategories = if (state.isExpense) {
                CategoryUtils.expenseCategories
            } else {
                CategoryUtils.incomeCategories
            }
            val selectedMainCatObj = mainCategories.find { it.name == state.selectedMainCategory }

            SimpleFlowRow(horizontalGap = 8.dp, verticalGap = (-8).dp) {
                mainCategories.forEach { mainCat ->
                    FilterChip(
                        selected = state.selectedMainCategory == mainCat.name,
                        onClick = { viewModel.onMainCategoryChange(mainCat.name) },
                        label = { Text(mainCat.name) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = CategoryUtils.getCategoryIcon(mainCat.icon)),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        shape = MaterialTheme.shapes.medium,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = chipContainerColor,
                            selectedLabelColor = onChipContainerColor,
                            selectedLeadingIconColor = onChipContainerColor
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = state.selectedMainCategory == mainCat.name,
                            selectedBorderColor = chipBorderColor,
                            selectedBorderWidth = 1.dp
                        )
                    )
                }
            }

            if (state.selectedMainCategory.isNotBlank()) {
                Text(stringResource(R.string.sub_category), style = MaterialTheme.typography.labelLarge)
                val subCategories = selectedMainCatObj?.subCategories ?: emptyList()

                SimpleFlowRow(horizontalGap = 8.dp, verticalGap = (-8).dp) {
                    subCategories.forEach { subCat ->
                        FilterChip(
                            selected = state.selectedCategory == subCat,
                            onClick = { viewModel.onCategoryChange(subCat) },
                            label = { Text(subCat) },
                            shape = MaterialTheme.shapes.medium,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = chipContainerColor,
                                selectedLabelColor = onChipContainerColor
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = state.selectedCategory == subCat,
                                selectedBorderColor = chipBorderColor,
                                selectedBorderWidth = 1.dp
                            )
                        )
                    }
                }
            }

            Text(stringResource(R.string.date), style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = DateUtils.formatTimestamp(state.selectedDate),
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                placeholder = { Text(stringResource(R.string.select_date)) },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar_month_24px),
                            contentDescription = stringResource(R.string.select_date)
                        )
                    }
                },
                enabled = false,
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = themeColor
                )
            )

            Text(
                stringResource(R.string.note_optional),
                style = MaterialTheme.typography.labelLarge,
                color = themeColor
            )
            OutlinedTextField(
                value = state.note,
                onValueChange = { viewModel.onNoteChange(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.note_placeholder)) },
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = themeColor,
                    focusedLabelColor = themeColor,
                    cursorColor = themeColor
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.saveTransaction()
                    Toast.makeText(context, context.getString(R.string.transaction_saved), Toast.LENGTH_SHORT).show()
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                enabled = state.isSaveEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColor,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(stringResource(R.string.save_transaction))
            }
        }
    }
}