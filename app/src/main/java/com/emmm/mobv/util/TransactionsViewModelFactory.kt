package com.emmm.mobv.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emmm.mobv.data.DataRepository
import com.emmm.mobv.screens.transactions.TransactionsViewModel

class TransactionsViewModelFactory(
    private val repository: DataRepository,
    private val accountId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(TransactionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionsViewModel(repository, accountId) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}