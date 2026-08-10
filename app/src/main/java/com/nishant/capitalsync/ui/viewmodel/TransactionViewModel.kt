package com.nishant.capitalsync.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.capitalsync.data.local.entity.TransactionEntity
import com.nishant.capitalsync.domain.repository.CapitalSyncRepository
import com.nishant.capitalsync.domain.repository.PreferenceRepository
import com.nishant.capitalsync.domain.usecase.GetTransactionsUseCase
import com.nishant.capitalsync.utils.CategoryUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionsUiState(
    val transactions: List<TransactionEntity> = emptyList(),
    val filteredTransactions: List<TransactionEntity> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: String = "All",
    val isSwipeEnabled: Boolean = false,
    val isLoading: Boolean = true
)

data class AddEditTransactionUiState(
    val id: Int = 0,
    val amount: String = "",
    val note: String = "",
    val selectedMainCategory: String = "",
    val selectedCategory: String = "",
    val isExpense: Boolean = true,
    val selectedDate: Long = System.currentTimeMillis(),
    val isEditing: Boolean = false,
    val isSaveEnabled: Boolean = false
)

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val repository: CapitalSyncRepository,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState: StateFlow<TransactionsUiState> = combine(
        getTransactionsUseCase(),
        _uiState,
        preferenceRepository.isSwipeActionsEnabled()
    ) { transactions, state, isSwipeEnabled ->
        val filtered = when (state.selectedFilter) {
            "Income" -> transactions.filter { it.type == "Income" }
            "Expense" -> transactions.filter { it.type == "Expense" }
            else -> transactions
        }.filter {
            it.categoryName.contains(state.searchQuery, ignoreCase = true) ||
                    (it.note?.contains(state.searchQuery, ignoreCase = true) ?: false)
        }
        state.copy(
            transactions = transactions,
            filteredTransactions = filtered,
            isSwipeEnabled = isSwipeEnabled,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TransactionsUiState()
    )

    private val _addEditState = MutableStateFlow(AddEditTransactionUiState())
    val addEditState = _addEditState.asStateFlow()

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onFilterChange(filter: String) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun onAmountChange(amount: String) {
        val filtered = amount.filter { it.isDigit() || it == '.' }
        if (filtered.count { it == '.' } <= 1) {
            _addEditState.update {
                it.copy(
                    amount = filtered,
                    isSaveEnabled = validateForm(filtered, it.selectedDate, it.selectedMainCategory)
                )
            }
        }
    }

    fun onNoteChange(note: String) {
        _addEditState.update { it.copy(note = note) }
    }

    fun onMainCategoryChange(mainCategory: String) {
        _addEditState.update {
            it.copy(
                selectedMainCategory = mainCategory,
                selectedCategory = "",
                isSaveEnabled = validateForm(it.amount, it.selectedDate, mainCategory)
            )
        }
    }

    fun onCategoryChange(category: String) {
        _addEditState.update {
            it.copy(
                selectedCategory = category,
                isSaveEnabled = validateForm(it.amount, it.selectedDate, it.selectedMainCategory)
            )
        }
    }

    fun onTypeChange(isExpense: Boolean) {
        _addEditState.update {
            it.copy(
                isExpense = isExpense,
                selectedMainCategory = "",
                selectedCategory = "",
                isSaveEnabled = false
            )
        }
    }

    fun onDateChange(date: Long) {
        _addEditState.update {
            it.copy(
                selectedDate = date,
                isSaveEnabled = validateForm(it.amount, date, it.selectedMainCategory)
            )
        }
    }

    private fun validateForm(amount: String, date: Long, mainCategory: String): Boolean {
        return amount.isNotBlank() &&
                amount.toDoubleOrNull() != null &&
                date <= System.currentTimeMillis() &&
                mainCategory.isNotBlank()
    }

    fun loadTransaction(id: Int?) {
        if (id == null) {
            _addEditState.value = AddEditTransactionUiState()
            return
        }
        viewModelScope.launch {
            repository.getTransactionById(id)?.let { t ->
                val allCats = CategoryUtils.expenseCategories + CategoryUtils.incomeCategories
                val mainCat = allCats.find { it.subCategories.contains(t.categoryName) || it.name == t.categoryName }?.name ?: ""

                _addEditState.value = AddEditTransactionUiState(
                    id = t.id,
                    amount = t.amount.toString(),
                    note = t.note ?: "",
                    selectedMainCategory = mainCat,
                    selectedCategory = t.categoryName,
                    isExpense = t.type == "Expense",
                    selectedDate = t.dateTimestamp,
                    isEditing = true,
                    isSaveEnabled = true
                )
            }
        }
    }

    fun saveTransaction() {
        val state = _addEditState.value
        val amountValue = state.amount.toDoubleOrNull() ?: 0.0
        viewModelScope.launch {
            val transaction = TransactionEntity(
                id = state.id,
                amount = amountValue,
                type = if (state.isExpense) "Expense" else "Income",
                categoryName = if (state.selectedCategory.isNotBlank()) state.selectedCategory else state.selectedMainCategory,
                dateTimestamp = state.selectedDate,
                note = state.note.takeIf { it.isNotBlank() }
            )
            repository.insertTransaction(transaction)
        }
    }

    fun deleteTransaction() {
        val state = _addEditState.value
        viewModelScope.launch {
            val transaction = TransactionEntity(
                id = state.id,
                amount = state.amount.toDoubleOrNull() ?: 0.0,
                type = if (state.isExpense) "Expense" else "Income",
                categoryName = if (state.selectedCategory.isNotBlank()) state.selectedCategory else state.selectedMainCategory,
                dateTimestamp = state.selectedDate,
                note = state.note
            )
            repository.deleteTransaction(transaction)
        }
    }
}