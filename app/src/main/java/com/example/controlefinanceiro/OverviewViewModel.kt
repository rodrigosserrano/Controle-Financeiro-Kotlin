package com.example.controlefinanceiro

import androidx.lifecycle.ViewModel
import com.example.controlefinanceiro.data.DummyRepository
import com.example.controlefinanceiro.data.domain.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.math.BigDecimal

class OverviewViewModel(
    private val repository: DummyRepository = DummyRepository
) : ViewModel() {

    private val filter = MutableStateFlow<String?>(null)
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun addTransaction(transaction: Transaction) {
        repository.add(transaction)
        updateUiState()
    }

    fun clearTransaction() {
        repository.clearTransactions()
        updateUiState()
    }

    fun updateTransaction(transaction: Transaction) {
        repository.clearTransactions()
        updateUiState()
    }

    fun deleteTransaction(uuid: String) {
        repository.deleteTransaction(uuid)
        updateUiState()
    }

    fun findTransaction(uuid: String) = repository.findTransaction(uuid)

    fun filterByCategory(category: String) {
        filter.value = category
        updateUiState()
    }

    fun clearFilter() {
        filter.value = null
        updateUiState()
    }

    private fun updateUiState() {
        val transactionListSaved = repository.transactions
        val transactions = if (filter.value != null) {
            transactionListSaved.filter { it.category == filter.value }
        } else {
            transactionListSaved
        }

//        listValuesByCategories()

        _uiState.value = UiState(
            transactions = transactions,
            total = transactionListSaved.sumOf { it.value },
        )
    }

    data class UiState(
        val transactions: List<Transaction> = emptyList(),
        val total: BigDecimal = transactions.sumOf { it.value },
    )
}