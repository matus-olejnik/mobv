package com.emmm.mobv.screens.transactions;

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.data.DataRepository
import com.emmm.mobv.data.db.model.TransactionItem
import kotlinx.coroutines.launch

class TransactionsViewModel(
    private val repository: DataRepository,
    private val accountId: String
) : ViewModel() {

    val transactions: LiveData<List<TransactionItem>>
        get() = repository.getAllTransactions(accountId)

    fun fetchTransactions(accountId: String) {
        viewModelScope.launch {
            repository.fetchTransactions(accountId)
        }
    }
}