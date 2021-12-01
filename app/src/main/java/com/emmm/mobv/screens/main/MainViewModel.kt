package com.emmm.mobv.screens.main;

import androidx.lifecycle.*
import com.emmm.mobv.data.DataRepository
import kotlinx.coroutines.launch
import java.math.BigDecimal

class MainViewModel(private val repository: DataRepository) : ViewModel() {

    val currentUserID: MutableLiveData<String> = MutableLiveData("")
    val moneyBalanceTextView: MutableLiveData<String> = MutableLiveData("")

    private val usdXlmRate: MutableLiveData<BigDecimal> = MutableLiveData()
    val tranUsdXlmRate: LiveData<String> = Transformations.map(usdXlmRate) { "$ ${it ?: ""}" }

    fun fetchActualBalance(accountId: String) {
        viewModelScope.launch {
            val actualBalance = repository.getActualBalance(accountId)
            val xml = " XLM"
            moneyBalanceTextView.value = String.format("%.2f", actualBalance.toFloat()) + xml
            usdXlmRate.value = repository.calculateUsdBalance(BigDecimal(actualBalance))
        }
    }

    fun fetchCurrentUserID(accountId: String) {
        viewModelScope.launch {
            currentUserID.value = repository.getUserAccountItem(accountId).accountId
        }
    }
}