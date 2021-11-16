package com.emmm.mobv.screens.main;

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.data.DataRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DataRepository) : ViewModel() {

    val moneyBalanceTextView: MutableLiveData<String> = MutableLiveData("")
    val actAccountId: MutableLiveData<String> = MutableLiveData()

    fun fetchActualBalance(accountId: String) {
        viewModelScope.launch {
            val actualBalance = repository.getActualBalance(accountId)
            moneyBalanceTextView.value = actualBalance
        }
    }

    fun getActualUserAccountId() {

    }
}